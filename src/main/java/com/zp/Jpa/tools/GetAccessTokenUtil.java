package com.zp.Jpa.tools;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import net.sf.json.JSONObject;

/**
 * 微信小程序获取accessToken
 *
 * @author Mr.Wen
 * @time 2017年8月28日
 */
@SuppressWarnings("deprecation")
public class GetAccessTokenUtil {
    // 网页授权接口
	
    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
    public final static String GetPageAccessTokenUrlForCode = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public final static String GetPageAccessTokenUrlForaccessTokenAndOpenid="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public static Map<String, String> getAccessToken(String appid, String appsecret) {
        String requestUrl = GetPageAccessTokenUrl.replace("APPID", appid).replace("SECRET", appsecret);
        HttpClient client = null;
        Map<String, String> result = new HashMap<String, String>();
        String accessToken = null;
        try {
            client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpget, responseHandler);
            JSONObject OpenidJSONO = JSONObject.fromObject(response);
            accessToken =(String)(OpenidJSONO.get("access_token"));
            System.out.println("accessToken=>"+accessToken);
            result.put("accessToken", accessToken);
            result.put("url", requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }
    /**通过code换取网页授权access_token
     * @param appid
     * @param appsecret
     * @param code
     * @return
            "access_token":"ACCESS_TOKEN",
            "expires_in":7200,
            "refresh_token":"REFRESH_TOKEN",
            "openid":"OPENID",
            "scope":"SCOPE" 
            
     */
    public static Map<String, String> getAccessTokenForCode(String appid, String appsecret,String code) {
        String requestUrl = GetPageAccessTokenUrlForCode.replace("CODE", code).replace("APPID", appid).replace("SECRET", appsecret);
        
        HttpClient client = null;
        Map<String, String> result = new HashMap<String, String>();
        String access_token = null;
        String refresh_token = null;
        String openid = null;
        try {
            client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpget, responseHandler);
            JSONObject OpenidJSONO = JSONObject.fromObject(response);
            access_token =(String)(OpenidJSONO.get("access_token"));
            refresh_token =(String)(OpenidJSONO.get("refresh_token"));
            openid =(String)(OpenidJSONO.get("openid"));
         //   System.out.println("ForCode方法中access_token=>"+access_token);
            result.put("access_token", access_token);
            result.put("refresh_token", refresh_token);
            result.put("openid", openid);
            result.put("url", requestUrl);
            System.out.println("url=>"+requestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }
    /**拉取用户信息
     * @param access_token
     * @param openid
     * @return
     * 参数	描述
openid	用户的唯一标识
nickname	用户昵称
sex	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
province	用户个人资料填写的省份
city	普通用户个人资料填写的城市
country	国家，如中国为CN
headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
privilege	用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
unionid	只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
     */
    public static Map<String, Object> getUserInfoForAccessTokenAndOpenid(String access_token, String openid) {
        String requestUrl = GetPageAccessTokenUrlForaccessTokenAndOpenid.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        HttpClient client = null;
        Map<String, Object> result = new HashMap<String, Object>();
        //返回内容;
        String nickname=null;
        String sex=null;
        String province=null;
        String city=null;
        String country=null;
        String headimgurl=null;
        Object privilege=null;
        String unionid=null;
        
        try {
            client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(requestUrl);
            
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            
            String response = client.execute(httpget, responseHandler);
//            Gson gson=new Gson();
//            String object=gson.toJson(response);
            JSONObject OpenidJSONO = JSONObject.fromObject(response);
           
            
            nickname =(String)(OpenidJSONO.get("nickname"));
            sex =String.valueOf(OpenidJSONO.get("sex"));
            province =(String)(OpenidJSONO.get("province"));
            city =(String)(OpenidJSONO.get("city"));
            country =(String)(OpenidJSONO.get("country"));
            headimgurl =(String)(OpenidJSONO.get("headimgurl"));
             privilege =OpenidJSONO.get("privilege");
            unionid =(String)(OpenidJSONO.get("unionid"));
try {
	nickname=new String(nickname.getBytes("ISO-8859-1"),"utf-8");
	province=new String(province.getBytes("ISO-8859-1"),"utf-8");
	city=new String(city.getBytes("ISO-8859-1"),"utf-8");
	country=new String(country.getBytes("ISO-8859-1"),"utf-8");
	headimgurl=new String(headimgurl.getBytes("ISO-8859-1"),"utf-8");
	privilege=new String(((String) privilege).getBytes("ISO-8859-1"),"utf-8");
	unionid=new String(unionid.getBytes("ISO-8859-1"),"utf-8");
			} catch (Exception e) {
				// TODO: handle exception
			}
            result.put("openid", openid);
            result.put("nickname", nickname);
            result.put("sex", sex);
            result.put("province", province);
            result.put("city", city);
            result.put("country", country);
            result.put("headimgurl", headimgurl);
            result.put("privilege", privilege);
            result.put("unionid", unionid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }
}
