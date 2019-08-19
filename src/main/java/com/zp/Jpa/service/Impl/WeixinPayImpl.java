package com.zp.Jpa.service.Impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.wxpay.sdk.WXPayUtil;
import com.zp.Jpa.entity.PayApply;
import com.zp.Jpa.entity.Users;
import com.zp.Jpa.repository.PayApplyRepository;
import com.zp.Jpa.repository.UsersRepository;
import com.zp.Jpa.service.WeixinPay;
import com.zp.Jpa.tools.StringUtils;
import com.zp.Jpa.tools.Suijishu;
//@Transactional(rollbackFor= Exception.class)

@Service

public class WeixinPayImpl implements WeixinPay {
	@Autowired
	private PayApplyRepository payApplyRepository;
	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	//事务
	@Transactional(rollbackFor = Exception.class)
	//基于类的代理模式
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	//@Transactional(rollbackOn = AcceptPendingException.class)
	public Map<String, Object> addPayApply(PayApply payApply) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		//给时间
		Date date=new Date();
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.fff");
//		String time2=sdf.format(date);
		payApply.setApplyDate(date);

		//获取当前时间戳，单位秒
		String cardId = String.valueOf(WXPayUtil.getCurrentTimestamp());
		payApply.setCardId(cardId);
		String remark = "无";
		if (!StringUtils.isEmpty(payApply.getRemark())) {
			remark = payApply.getRemark();
		}
		payApply.setRemark(remark);
		payApply.setPayType(0);
		// 0未处理 1已处理 2已驳回
		payApply.setSates(0);
		payApply.setIsDel(0);
		payApply.setONumber(payApply.getONumber());
		payApply.setPAid(Suijishu.getUuid());
		if (payApplyRepository != null) {
			try {
				//如果报错,就回滚数据
				PayApply payApply2 = payApplyRepository.save(payApply);
				// 提交提现申请后,准备减去改用户的余额
				Integer uid = payApply2.getApplyUser();
				Users users = usersRepository.getOne(uid);
				// 原有余额
				long money = users.getBalance();
				// 减去提现的金额
				long moneyThen = money - payApply.getApplyMoney();			
				//模拟项目意外停止,本次数据处理出现停电什么的
				//int a=1/0;			
				users.setBalance(moneyThen);
				// 提现后应该显示的金额
				Users users2 = usersRepository.save(users);
				if (users2 != null) {
					map.put("msg", "提现申请提交成功");
				} else {
					map.put("msg", "提现申请提交失败,请稍后再试");
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("在执行本次事务中出现意外异常,数据已回滚");
				e.printStackTrace();     
		          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//如果updata2()抛了异常,updata()会回滚,不影响事物正常执行  
			map.put("msg", "本次提现出现异常,请联系官方微信,或稍后再试");
			}
			
		}
		return map;
	}
}
