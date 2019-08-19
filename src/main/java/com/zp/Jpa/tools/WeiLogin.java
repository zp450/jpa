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
 * @author Administrator
 *
 */
@SuppressWarnings("deprecation")
public class WeiLogin {
 // 
	
    public final static String GetPageAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx42661ad619b7676b&secret=55e6dad131ff56deb8da522587789c59&code=CODE&grant_type=authorization_code";
    /**通过code换取网页授权access_token
     * @param code
     * @return
     */
    public static Map<String, String> getAccessToken(String code) {
        String requestUrl = GetPageAccessTokenUrl.replace("CODE", code);
        HttpClient client = null;
        Map<String, String> result = new HashMap<String, String>();
        String accessToken = null;
        try {
            client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpget, responseHandler);
            JSONObject OpenidJSONO = JSONObject.fromObject(response);           
            System.out.println("OpenidJoono=>"+OpenidJSONO.toString());
            accessToken =(String)(OpenidJSONO.get("access_token"));
            System.out.println("accessToken=>"+accessToken);
            result.put("accessToken", accessToken);
//            result.put("url", requestUrl);
     
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }
	
}
