package com.zp.tools.wexin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import com.zp.Jpa.weixinAppUtil.ConstantUtil;

public class Bxh {
	private static Logger log = Logger.getLogger(CommonUtil.class);
    //统一接口地址
//    private static final String REQUEST_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//    private static final String APPID = "wx42661ad619b7676b";
//    private static final String MCH_ID = "1387649702";//
//    private static final String API_KEY = "e10adc3949ba59abbe56e057f20f883e";
//    private static final String WECHAT_NOTIFY_URL = "http://xxxx/weixin_notify.do";
 
    /**
     * Summary
     * step 1  加密单号等信息   调用统一下单接口
     * step 2  加密数据回发给app 发起支付
     * author bxh
     */
 
    public static void step1(){
        init(wechatParamMap(ConstantUtil.appid));
    }
    public static void step2(Map<String,String> appParamMap){
        init(appParamMap);
    }
    private static String init(Map<String, String> wechatParams ) {
        String sign = getSign(wechatParams);
        wechatParams.put("sign", sign);
        String orderParam = buildXmlOrderParam(wechatParams);
        parseXml2Map(orderParam,new HashMap<String, String>());
        return orderParam;
    }
 
    private static String buildXmlOrderParam(Map<String, String> param) {
        StringBuffer xmlStr = new StringBuffer("<xml>");
        for (String key : param.keySet()) {
            xmlStr.append("<").append(key).append(">");
            xmlStr.append(param.get(key));
            xmlStr.append("</").append(key).append(">\n\r");
        }
        xmlStr.append("</xml>");
        return xmlStr.toString();
    }
    private static Map<String, String> wechatParamMap(String appid) {
        Map<String, String> keyValues = new HashMap<String, String>();
        keyValues.put("appid", appid);
        //商户号
        keyValues.put("mch_id", ConstantUtil.mch_id);
        //随机字符串
        keyValues.put("nonce_str", "E10ADC3949BA59ABBE56E057F20F883E");
        //签名
        //keyValues.put("sign", "");
        //商品描述
        keyValues.put("body", "123");
        //商户订单号
        keyValues.put("out_trade_no", "88888888");
        //总金额
        keyValues.put("total_fee", "1");
        //终端IP
        keyValues.put("spbill_create_ip", "139.159.220.125");
        //通知地址
        keyValues.put("notify_url", ConstantUtil.WEI_XIN_NOTIFY_URL);
        //交易类型
        keyValues.put("trade_type", "APP");
 
 
        return keyValues;
    }
 
    /**
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
 
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }
    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }
 
    private static String getSign(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());
        // key排序
        Collections.sort(keys);
 
        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value,  false));
            authInfo.append("&");
        }
 
        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));
        authInfo.append("&key=").append(ConstantUtil.API_KEY);
        String oriSign = CommonUtil.MD5.sign(authInfo.toString());
        		//SignUtils.signMD5(authInfo.toString());
       // Log.i("bxh", "-> " + authInfo);
        String encodedSign = "";
 
        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedSign.toUpperCase();
    }
    /*
    *  请求统一接口
     */
    @SuppressWarnings({ "deprecation", "resource" })
	static String requestHttp(String url, String strParams) {
 
        HttpPost post = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
 
            // 设置超时时间
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
 
            post = new HttpPost(url);
            // 构造消息头
            post.setHeader("Content-type", "text/xml; charset=utf-8");
            post.setHeader("Connection", "Close");
 
            // 构建消息实体
            StringEntity entity = new StringEntity(strParams, "UTF-8");
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("text/xml");
            post.setEntity(entity);
 
            HttpResponse response = httpClient.execute(post);

            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
 
            } else {
                //404...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
 
    /**
     * @param respData xmlStr
     * @param map  要填入的map
     */
    private static void parseXml2Map(String respData ,Map<String,String> map){
        if (respData==null|| map ==null){
            return;
        }
        try {
            Document doc = DocumentHelper.parseText(respData);
            Element root = doc.getRootElement();
            @SuppressWarnings("unchecked")
			List<Node> eles= root.elements();
            for(int i = 0 ;i<eles.size();i++){
                Node node = eles.get(i);
                String key = node.getName();
                String val = node.getStringValue();
                map.put(key,val);
                //Log.i("bxh", "parseXml2Map ->" + key + val);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
 
 
    }
 
}
