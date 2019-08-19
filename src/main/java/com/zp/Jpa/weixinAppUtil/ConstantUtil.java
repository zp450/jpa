package com.zp.Jpa.weixinAppUtil;

public class ConstantUtil {
	public final static  String appid="wx42661ad619b7676b";
    public final static String appsecret = "55e6dad131ff56deb8da522587789c59";
	public final static String mch_id = "1387649702";
	//秘钥key为商户平台设置的密钥key
	public final static String PARTNER_KEY = "shanghaiweiyouyouxiangongsiiuju5";
	//api
	public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//回调地址  http://120.26.71.141:8081/WeixinLogin/getWeixinToken
	//
	//
	/**正式服务器
	 * 充值
	 */
	public final static String WEI_XIN_NOTIFY_URL="http://120.26.71.141:8081/weixinPay/notifyWeiXinPay";
	
	/**
	 * 消费
	 */
	//public final static String WEI_XIN_NOTIFY_URL_CONSUME="http://120.26.71.141:8081/weixinPay/notifyWeiXinPayConsume";
	
	//临时花生壳服务器
//	public final static String WEI_XIN_NOTIFY_URL="http://2a49921k65.zicp.vip:55938/weixinPay/notifyWeiXinPay";
	
	public final static String SUCCESS="success";
	public final static Integer HUIYUANPRICE=999;
	public final static String AdinfoPICURL="http://www.iuju58.com/Pictures/TrustSoft/AdInfo/";
	//航线保险单价
	public final static long BAOXIAN=50;
	
	public final static String API_KEY = "e10adc3949ba59abbe56e057f20f883e";
	//当入股超过15万后,往上查,算上爷爷的代数
	public final static long GROUNDFATHER=4;
	//算上自己,往上数几代  自己算一代 父亲第二代,爷爷三代,类推
	public final static long TEAMEQUITY=4;
	//rsa秘钥
	public final static String RSAsecret="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJHBL4sAbMC3gaXXGxyQxTmTELYBFf3m/Hg9nFxaxGOFg1qctRxU0vV0kmBPK7qGF1dkFsu5cjufh9oD8nQFWfJtR0UYI7whYBoV3i5ixzlIPtoGwlDTXrF6bjHxZEZE8yli7Zsk6nHwHk5FbPC4pqM6NX2grPpF5HyWX3L2SH0TAgMBAAECgYAtCmHCtpLkytBJiztZjHMl44hadeNx3ptOHNvgvjvJ9UwuCBb/dkbqiudg05ZTwXQdEVTqB5iBTD8S4/1cVPsCMH3Y0lX9bCTcUqszVfPWlni4HJSF6AaPC1Dnr1WeHpu5kHaLWuKuR6v0cpPAGtebE2X40Fqn+5c9xvYD6XJ4oQJBAPSxovfLw725DlejKYN0H4uyrC4pZwWqhh+NxLB918/rB8zqpEFbHvosfmfMfoe7R7RtZyhRzhQXq6hqNckfCBcCQQCYfT7Rh/UgUw4TLqgGN8or/zpEpGkDS2ouVNXQiGqpfiSRGwjfCLX71QpI0U51/fC/Dl4Wn+8WDQOD4S4Dq5RlAkB5Y6Z1R6DTffqff1IY5ILByuGgBOoW5YGkJbBt3gAyJWa5Qa46vfmgInKTC9+5di8cUynZ1rtlPFjsM8R7AeoBAkAkJqUq5ZGOfUI79/arqrRKY/K7bULcHhfpLgGWs5Cd6CLhJ8idn6INNv9+Lejs/iwCD3Ts5jO3RZSMwmi3RAklAkAbutyPBRsBbWGPxTcA1dP8uZF8v+ftDPPt4wH116xFIX9GeXzvV+aKNDne8mz+xGUx7H6FtFli9zeK1z1nKIiE";
	//老系统订单状态PayStatus
	//public final static Integer YOUXIAODINGDAN=null;
//	public final static Integer SUOYOUDINGDAN=-1;//所有订单
//	public final static Integer WEIZHIFU=0;//未支付
//	public final static Integer YIZHIFU=1;//已支付
//	public final static Integer YIQUXIAO=2;//已取消 
//	public final static Integer DAIFAHUO=4;//待发货
//	public final static Integer YIFAHUO=5;//已发货
}
