package com.zp.Jpa.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.wxpay.sdk.WXPayUtil;
import com.zp.Jpa.entity.Equity;
import com.zp.Jpa.entity.Order;
import com.zp.Jpa.entity.PayApply;
import com.zp.Jpa.entity.Users;
import com.zp.Jpa.repository.OrderRepository;
import com.zp.Jpa.repository.UsersRepository;
import com.zp.Jpa.service.EquityService;
import com.zp.Jpa.service.Impl.WeixinPayImpl;
import com.zp.Jpa.tools.Date.DateUtil;
import com.zp.Jpa.weixinAppUtil.ConstantUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@CrossOrigin
@RestController
@RequestMapping(value = "/weixinPay", name = "微信支付模块")
public class WeixinPay {
	@Autowired
	public OrderRepository orderRepository;
	@Autowired
	public UsersRepository usersRepository;

	@Autowired
	public WeixinPayImpl weixinPayImpl;
	
	@Autowired
	public EquityService equityService;
	
//	 1.生成商户订单
//	 2.调用统一下单api接口
//	 3.生成jsAPI页面调用的支付参数并签名();
//	 4.通知商户支付结果
//	 5.告知微信通知处理结果
//	 6.调用查询api,查询支付结果,返回支付结果
	/**
	 * http://localhost:8080/weixinPay/notifyWeiXinPay
	 * 
	 * 此函数会被执行多次，如果支付状态已经修改为已支付，则下次再调的时候判断是否已经支付，如果已经支付了，则什么也执行
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws JDOMException
	 */
	@ApiOperation(value = "微信充值支付回调地址(前台无需调用,腾讯调用)", notes = "腾讯调用,此函数会被执行多次，如果支付状态已经修改为已支付，则下次再调的时候判断是否已经支付，如果已经支付了，则什么也执行")

	@RequestMapping(value = "notifyWeiXinPay", produces = MediaType.APPLICATION_JSON_VALUE)
	// @RequestDescription("支付回调地址")
	@ResponseBody
	public String notifyWeiXinPay(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		System.out.println("微信支付回调");
		InputStream inStream = request.getInputStream();
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		String resultxml = new String(outSteam.toByteArray(), "utf-8");
		Map<String, String> params = WXPayUtil.xmlToMap(resultxml);
		outSteam.close();
		inStream.close();
		Map<String, String> return_data = new HashMap<String, String>();
		// 签名验证,
		if (!WXPayUtil.isSignatureValid(params, ConstantUtil.PARTNER_KEY)) {
			// 支付失败
			return_data.put("return_code", "FAIL");
			return_data.put("return_msg", "return_code不正确");
			return WXPayUtil.mapToXml(return_data);
		} else {
			System.out.println("===============付款成功==============");
			// ------------------------------
			// 处理业务开始
			// ------------------------------
			// 此处处理订单状态，结合自己的订单数据完成订单状态的更新
			// ------------------------------
			// 商户订单号 string 32位置
            //String转uniqueidentifier
			String uuid2 = params.get("out_trade_no");
			StringBuffer bufferA = new StringBuffer(uuid2);

			bufferA.insert(8, "-");
			bufferA.insert(13, "-");
			bufferA.insert(18, "-");
			bufferA.insert(23, "-");

			String out_trade_no = String.valueOf(bufferA).toUpperCase();
			// 找到数据库中对应的订单修改状态
			Order order = orderRepository.findOne(out_trade_no);
			// 订单金额
			long total_fee = Long.parseLong(params.get("total_fee")) / 100;// 订单金额 订单总金额，单位转换为元
			if (total_fee != order.getTotalPrice()) {
				return "订单总金额数据错误,可能出现拦截篡改,本次支付对于本数据库无效";
			} else {
				order.setTotalPrice(total_fee);
			}
			// 支付完成时间
			Date accountTime = DateUtil.stringtoDate(params.get("time_end"), "yyyyMMddHHmmss");
			order.setPayTime(accountTime);
			// 现在时间
			// String ordertime = DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
			// 返回的appid,需要做验证
			String appId = params.get("appid");
			if (!appId.equals(ConstantUtil.appid)) {
				System.out.println("开发者id没能对应,可能出现拦截篡改,本次支付对于本数据库无效");
				return "开发者id没能对应,可能出现拦截篡改,本次支付对于本数据库无效";
			}
			// transaction_id 微信订单id
			String tradeNo = params.get("transaction_id");
			order.setOutNo(tradeNo);
			// 支付类型 微信支付
			order.setPid(9);
			// 修改订单状态
			order.setPayStatus(1);
			//如果是订单类型是充值订单,那么进行加余额
			if(order.getOrderType()==9) {
				System.out.println("是充值订单");
				// 修改用户余额    因为是充值订单  所以增加余额
				Users users = usersRepository.getOne(order.getUid());
				long balance = users.getBalance();
				
				//增加改用户的
				long nextBalance = balance + total_fee;
				users.setBalance(nextBalance);
				usersRepository.save(users);
			}
			if(order.getOrderType()==11) {
				System.out.println("是股份购买订单");
			 	Integer recommendId=order.getEquityFatherId();
			 	Integer uid=order.getUid();
			 	BigDecimal money=new BigDecimal(order.getTotalPrice().toString());
			 	Equity equity=new Equity(uid,recommendId,money);
			 	equityService.addEquity(equity);
			} 
			System.out.println("是普通消费订单");
			Order order2 = orderRepository.save(order);
			if (order2.getPayStatus() == 1) {
				return_data.put("return_code", "SUCCESS");
				return_data.put("return_msg", "OK");
			}
			return_data.put("return_code", "FAIL");
			return_data.put("return_msg", "return_code不正确");
			return WXPayUtil.mapToXml(return_data);
		}
	}

	
	/**
	 * http://localhost:8080/weixinPay/addPayApply?applyUser=6035&applyName=哈哈&applyMoney=100&mobile=18897924015&cardId=1558950011&bankCardId=3443&bank=中国爱存不存&bankName=zhansna
	 * 
	 * @param payApply
	 * @return
	 */
	@ApiOperation(value = "提交提现申请", notes = "添加提现申请")
	@ApiImplicitParam(name = "payApply", value = "提现申请", required = false, dataType = "PayApply")
	@RequestMapping(value = "/addPayApply", name = "提交提现申请", method = RequestMethod.POST)
	public Map<String, Object> addPayApply(PayApply payApply) {

		Map<String, Object> map = weixinPayImpl.addPayApply(payApply);
		return map;
	}
	

}
