package com.zp.tools.wexin;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.apache.log4j.Logger;

import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.zp.Jpa.entity.Order;
import com.zp.Jpa.tools.Suijishu;
import com.zp.Jpa.weixinAppUtil.ConstantUtil;
import com.zp.Jpa.weixinAppUtil.PayCommonUtil;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;


public class Weixin {
	private static Logger log = Logger.getLogger(CommonUtil.class);
	public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    public final static String GetPageAccessTokenUrlForCode = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public final static String GetPageAccessTokenUrlForaccessTokenAndOpenid="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public final static String getTicket="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public final static String wxPay="https://api.mch.weixin.qq.com/pay/unifiedorder"; 
    
	//首先要获取到access token：公众号的全局唯一票据 。
//	然后根据access token获取到jsapi_ticket：公众号用于调用微信JS接口的临时票据。
//	再用jsapi_ticket获取到签名。
	/**根据appid和appsecret获取token
	 * https
	 * @author zhangpu
	 * @param appid
	 * @param appsecret
	 * @return Map<String, String> result
	 * @throws JSONException
	 */
	public static Map<String, String> getToken(String appid, String appsecret) throws JSONException {
		          Map<String, String> result = new HashMap<String, String>();
		          String requestUrl = GetPageAccessTokenUrl.replace("APPID", appid).replace("SECRET", appsecret);
		          // 发起GET请求获取凭证
		          JSONObject jsonObject = CommonUtil.httpsRequestToJsonObject(requestUrl, "GET", null,false);
		          if (null != jsonObject) {
					  result.put("access_token", jsonObject.getString("access_token"));				  
					  result.put("expires_in", Integer.toString(jsonObject.getInt("expires_in")));
		         }
		         return result;
		     }
	/**根据token获取ticket 
	 * @author zhangpu
	 * @param token
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, String> getTicket(String token) throws JSONException {       
      //请求rul
        String requestUrl = getTicket.replace("ACCESS_TOKEN", token);
       // System.out.println("ticket url =>"+requestUrl);
        // 发起GET请求获取凭证
        JSONObject jsonObject = CommonUtil.httpsRequestToJsonObject(requestUrl, "GET", null,false);
       // Ticket ticket = null;
        Map<String, String> result = new HashMap<String, String>();
        String ticket = "";
        int expires_in = 0;
        if (null != jsonObject) {
            ticket = jsonObject.getString("ticket");
			expires_in = jsonObject.getInt("expires_in");
			result.put("ticket", ticket);
			result.put("expires_in", Integer.toString(expires_in));
        }
        return result;
    }
	
	
	/**
     * 算出签名
     * @param jsapi_ticket 
     * @param url 业务中调用微信js的地址
     * @return
     */
    public static Map<String, String> sign(String jsapi_ticket, String url) {
        String nonce_str = CommonUtil.create_nonce_str();
        String timestamp = CommonUtil.create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
           
            signature = CommonUtil.MD5.byteToStr(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        Map<String, String> result=new HashMap<>();
        result.put("url", url);
        result.put("jsapi_ticket", jsapi_ticket);
        result.put("nonce_str", nonce_str);
        result.put("timestamp", timestamp);
        result.put("signature", signature);
        return result;
    }
    
    
//    public static Map<String, String> sign(String appid, String mch_id,String device_info,String body) {
//        String nonce_str = CommonUtil.create_nonce_str();
//        //String timestamp = CommonUtil.create_timestamp();
//        String string1;
//        String signature = "";
//
//        //注意这里参数名必须全部小写，且必须有序
//        string1 = "appid=" + appid +
//                  "&mch_id=" + mch_id +
//                  "&device_info=WEB" +
//                  "&body=" + body +
//                  "&nonce_str=" + nonce_str;
//        try
//        {
//            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
//            crypt.reset();
//            crypt.update(string1.getBytes("UTF-8"));
//           
//            signature = CommonUtil.MD5.byteToStr(crypt.digest());
//        }
//        catch (NoSuchAlgorithmException e)
//        {
//            e.printStackTrace();
//        }
//        catch (UnsupportedEncodingException e)
//        {
//            e.printStackTrace();
//        }
//        Map<String, String> result=new HashMap<>();
////        result.put("url", url);
////        result.put("jsapi_ticket", jsapi_ticket);
////        result.put("nonce_str", nonce_str);
////        result.put("timestamp", timestamp);
//        result.put("signature", signature);
////        Signature result = new Signature();
////       
////        result.setUrl(url);
////        result.setJsapi_ticket(jsapi_ticket);
////        result.setNonceStr(nonce_str);
////        result.setTimestamp(timestamp);
////        result.setSignature(signature);
//
//        return result;
//    }
    
    /** 统一api
     * @param order
     * @param appid
     * @param mch_id
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("static-access")
	public static Map<String, Object> wxPayApi(Order order,String appid,String mch_id,String spbill_create_ip,String openid) throws Exception {
         Map<String, Object> result = new HashMap<String, Object>();
 
         //生成签名
         String random=Suijishu.getRandomString(32);
         //树形集合,自动排列
         //做访问时需要将他们排列abcdefg...
         SortedMap<String, String> signParams = new java.util.TreeMap<String, String>();

         signParams.put("appid", ConstantUtil.appid);//app_id
         signParams.put("body",order.getName());//商品参数信息
         signParams.put("mch_id", mch_id);//微信商户账号
         signParams.put("nonce_str", random);//32位不重复的编号
         signParams.put("notify_url", ConstantUtil.WEI_XIN_NOTIFY_URL);//回调页面
         String out_trade_no=order.getOid().toString().replace("-", "");
         
         signParams.put("out_trade_no", out_trade_no);//订单编号
         System.out.println("spbill_create_ip==="+spbill_create_ip);
         signParams.put("spbill_create_ip",spbill_create_ip );//请求的实际ip地址
         long total_fee=order.getTotalPrice()*100;
         signParams.put("total_fee",(String.valueOf(total_fee)));//支付金额 单位为分
         signParams.put("trade_type", "JSAPI");//付款类型为JSAPI
         signParams.put("openid", openid);
         String sign = PayCommonUtil.createSign("UTF-8", signParams);//生成签名
         signParams.put("sign", sign);
         signParams.remove("key");//调用统一下单无需key（商户应用密钥）
         //将封装好的参数转换成Xml格式类型的字符串
         String apiXML= PayCommonUtil.getRequestXml(signParams);

         //xml返回结果样式
         //提交
         String str=com.zp.Jpa.weixinAppUtil.CommonUtil.httpsRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",apiXML);
      //   result.put("msg", str);
         //xml-->map-->json->string
         //map返回结果样式
         Map<String, String> newSign=WXPayUtil.xmlToMap(str);
         //转换为json对象
         JSONObject json = JSONObject.fromObject(newSign);
         result.put("mapMsg", json.toString());
         //结果状态
         String result_code=newSign.get("result_code");
         String prepay_id=null;
         //如果结果正确
         if (result_code.contains("SUCCESS")) {
 			 prepay_id = (String) newSign.get("prepay_id");// 获取到prepay_id
 		}
         
         
         // 支付方法调用所需参数map
         Map<String, String> chooseWXPayMap = new HashMap<>();
         chooseWXPayMap.put("appId", ConstantUtil.appid);//appid
         chooseWXPayMap.put("timeStamp", String.valueOf(WXPayUtil.getCurrentTimestamp()));//时间戳
         chooseWXPayMap.put("nonceStr", random);//生成的随机数
         chooseWXPayMap.put("package", "prepay_id=" + prepay_id);//固定格式
         chooseWXPayMap.put("signType", WXPayConstants.MD5);//加密方式
         //线程在往下跑,回调还没拿到,暂时让主线程停一下;
         Thread.currentThread().sleep(500);//莫名報錯,,,,
         WXPayUtil.getLogger().info("wxPay.chooseWXPayMap:" + chooseWXPayMap.toString());
         // 生成支付签名
         String paySign = WXPayUtil.generateSignature(chooseWXPayMap, ConstantUtil.PARTNER_KEY);
         chooseWXPayMap.put("paySign", paySign);
         //记录一下日志
         WXPayUtil.getLogger().info("wxPay.paySign:" + paySign);
         //转为json对象
         JSONObject json2 = JSONObject.fromObject(chooseWXPayMap);
         result.put("msg", json2);
        return result;
    }
    /**
     * 获取页面上weixin支付JS所需的参数
     * @param map
     * @return
     */
//    private WxPay getWxPayInfo(String prepay_id,String appid) {
//        String nonce = CommonUtil.create_nonce_str().replace("-", "");
//        String timeStamp = CommonUtil.create_timestamp();
//        //再算签名
//        String newPrepay_id = "prepay_id="+prepay_id;
//        String args = "appId="+appid
//                      +"&nonceStr="+nonce
//                      +"&package="+newPrepay_id
//                      +"&signType=MD5"
//                      +"&timeStamp="+timeStamp
//                      +"&key="+key;
//        String paySign=CommonUtil.MD5.sign(args);
//      //  String paySign = SignUtil.getSign(args, "MD5");
//        WxPay wxPay = new WxPay();
//        wxPay.setNonce_str(nonce);
//        wxPay.setPaySign(paySign);
//        wxPay.setPrepay_id(newPrepay_id);
//        wxPay.setTimeStamp(timeStamp);
//        return wxPay;
//    }
    
    
}
