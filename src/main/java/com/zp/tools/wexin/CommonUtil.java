package com.zp.tools.wexin;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.sf.json.JSONObject;

public class CommonUtil {
	private static Logger log = Logger.getLogger(CommonUtil.class);
	
	/**
     * 
     * @param requestUrl     接口地址
     * @param requestMethod  请求方法：POST、GET...
     * @param output         接口入参
     * @param needCert       是否需要数字证书
     * @return
     */
    private static StringBuffer httpsRequest(String requestUrl, String requestMethod, String output,boolean needCert)
            throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException, MalformedURLException,
            IOException, ProtocolException, UnsupportedEncodingException {
        
        //新创建一个url对象,接过来,转化为url对象不再是一串字符串;
        URL url = new URL(requestUrl);
        
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        
        //是否需要数字证书
        if(needCert){
            //设置数字证书
            setCert(connection);
        }
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
//        connection.setRequestProperty("Pragma", value);
  //      connection.setRequestProperty("Content-Type", "text/xml"); 
        connection.setRequestMethod(requestMethod);
        if (null != output) {
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(output.getBytes("UTF-8"));
            outputStream.close();
        }

        // 从输入流读取返回内容
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str = null;
        StringBuffer buffer = new StringBuffer();
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        inputStream = null;
        connection.disconnect();
        return buffer;
    }
    /**   http请求,,不能用来访问微信支付api,安全级别不够!  取消使用
     *  参数 xml  返回string
     * @param requestUrl     接口地址
     * @param requestMethod  请求方法：POST、GET...
     * @param output         接口入参
     * @param needCert       是否需要数字证书
     * @return
     */
    public static String httpsRequestXML(String requestUrl, String requestMethod, String output,boolean needCert)
            throws NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException, MalformedURLException,
            IOException, ProtocolException, UnsupportedEncodingException {
    	String result="调用http无返回状态";
    	HttpPost post = null;
    	HttpClient httpClient = new DefaultHttpClient();
    	System.out.println("__________________________________");
    	try {
    		post = new HttpPost(requestUrl);
    		// 构造消息头
            post.setHeader("Content-type", "text/xml; charset=utf-8");
            post.setHeader("Connection", "Close");
            // 构建消息实体
            StringEntity entity = new StringEntity(output, "UTF-8");
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("text/xml");
            post.setEntity(entity);
            HttpResponse response = httpClient.execute(post);
            System.out.println("一个http请求,"+requestMethod+",返回的数据===>"+response.toString());
         // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
            	result=EntityUtils.toString(response.getEntity());
                
 
            } else {
            	result="结果返回错误头 ,查看请求头和请求参数";
                //404...
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

            
        return result;
    	
    }
    /**
     * 给HttpsURLConnection设置数字证书
     * @param connection
     * @throws IOException
     */
    private static void setCert(HttpsURLConnection connection) throws IOException{
        FileInputStream instream = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            //读取本机存放的PKCS12证书文件
            instream = new FileInputStream(new File("certPath")); //certPath:数字证书路径
            
            //指定PKCS12的密码(商户ID)
            keyStore.load(instream, "商户ID".toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, "商户ID".toCharArray()).build();
            //指定TLS版本
            SSLSocketFactory ssf = sslcontext.getSocketFactory(); 
             connection.setSSLSocketFactory(ssf);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            instream.close();
        }
    }
    
    
    /**
     * 如果返回JSON数据包,转换为 JSONObject
     * @param requestUrl 
     * @param requestMethod
     * @param outputStr
     * @param needCert
     * @return
     */
    public static JSONObject httpsRequestToJsonObject(String requestUrl, String requestMethod, String outputStr,boolean needCert) {
        JSONObject jsonObject = null;
        try {
             StringBuffer buffer = httpsRequest(requestUrl, requestMethod, outputStr,needCert);
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("连接超时："+ce.getMessage());
        } catch (Exception e) {
            log.error("https请求异常："+e.getMessage());
        }
        
        return jsonObject;
    }
    
    /**
     * 如果返回xml数据包，转换为Map<String, String>
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @param needCert
     * @return
     */
    public static Map<String, String> httpsRequestToXML(String requestUrl, String requestMethod, String outputStr,boolean needCert) {
        Map<String, String> result = new HashMap<>();
        try {
             StringBuffer buffer = httpsRequest(requestUrl, requestMethod, outputStr,needCert);
             result = parseXml(buffer.toString());
        } catch (ConnectException ce) {
            log.error("连接超时："+ce.getMessage());
        } catch (Exception e) {
            log.error("https请求异常："+e.getMessage());
        }
        return result;
    }
    /**
     * 参数为xml格式 ,，转换为Map<String, String>
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @param needCert
     * @return
     */
    public static Map<String, String> httpsRequestXMLToXML(String requestUrl, String requestMethod, String outputStr,boolean needCert) {
        Map<String, String> result = new HashMap<>();
        System.out.println("xmlTOxml");
        try {
             String buffer = httpsRequestXML(requestUrl, requestMethod, outputStr,needCert);
//             result = parseXml(buffer);
             result.put("buffer", buffer);
        } catch (ConnectException ce) {
            log.error("连接超时："+ce.getMessage());
        } catch (Exception e) {
            log.error("https请求异常："+e.getMessage());
        }
        return result;
    }
    /**
     * xml转为map
     * @param xml
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> parseXml(String xml)  {
        Map<String, String> map = new HashMap<String, String>();
        try {
            Document document = DocumentHelper.parseText(xml);
            
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();
    
            for (Element e : elementList){
                map.put(e.getName(), e.getText());
            }
        } catch (DocumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return map;
    }
    public static Map<Object, Object> parseXml(HttpServletRequest request) 
    {
       // 解析结果存储在HashMap
       Map<Object, Object> map = new HashMap<Object, Object>();
       try {
           InputStream inputStream;
           
               inputStream = request.getInputStream();
           
           // 读取输入流
           SAXReader reader = new SAXReader();
           Document document = reader.read(inputStream);
           System.out.println(document);
           // 得到xml根元素
           Element root = document.getRootElement();
           // 得到根元素的所有子节点
           List<Element> elementList = root.elements();
        
           // 遍历所有子节点
           for (Element e : elementList)
               map.put(e.getName(), e.getText());
        
           // 释放资源
           inputStream.close();
           inputStream = null;
       } catch (IOException e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
       } catch (DocumentException e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
       }
       return map;
   }
    /** 
     * 将一个 JavaBean 对象转化为一个  Map 
     * @param bean 要转化的JavaBean 对象 
     * @return 转化出来的  Map 对象 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */   
    @SuppressWarnings({ "rawtypes"})   
    public static SortedMap<Object,Object> convertBean(Object bean) { 
        SortedMap<Object,Object> returnMap = new TreeMap<Object,Object>();  
        try {
            Class type = bean.getClass();   
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();   
            for (int i = 0; i< propertyDescriptors.length; i++) {   
                PropertyDescriptor descriptor = propertyDescriptors[i];   
                String propertyName = descriptor.getName();   
                if (!propertyName.equals("class")) {   
                    Method readMethod = descriptor.getReadMethod();   
                    Object result = readMethod.invoke(bean, new Object[0]);   
                    if (result != null) {   
                        returnMap.put(propertyName, result);   
                    } else {   
                        returnMap.put(propertyName, "");   
                    }   
                }   
            }   
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }  
        return returnMap;   
    } 

    /**获取时间戳
     * @return String
     */
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
//    public static void main(String[] args) {
//		System.out.println(create_timestamp().length());
//	}
    /**获取随机字符串
     * @return
     */
    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
//    public static String createSign() {
//    	
//    	// 注意这里参数名必须全部小写，且必须有序
//    	String string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str
//    	        + "&timestamp=" + timestamp + "&url=" + url;
//
//    	logger.debug("[string1] = " + string1);
//
//    	try {
//    	    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
//    	    crypt.reset();
//    	    crypt.update(string1.getBytes("UTF-8"));
//    	    signature = byteToHex(crypt.digest());
//
//    	    logger.debug("[signature] = " + signature);
//
//    	} catch (NoSuchAlgorithmException e) {
//    	    e.printStackTrace();
//    	} catch (UnsupportedEncodingException e) {
//    	    e.printStackTrace();
//    	}
//    	
//    }
/**
      * 生成签名
      * @param parameters
      * @return
      */
//     public static String createSgin(SortedMap<Object,Object> parameters)
//     {
//         StringBuffer sb = new StringBuffer();  
//            Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）  
//            Iterator it = es.iterator();  
//            while(it.hasNext()) {  
//                Map.Entry entry = (Map.Entry)it.next();  
//                String k = (String)entry.getKey();  
//                Object v = entry.getValue();  
//                if(null != v && !"".equals(v)   
//                        && !"sign".equals(k) && !"key".equals(k)) {  
//                    sb.append(k + "=" + v + "&");  
//                }  
//            }  
//            sb.append("key=" + wxConfig.getWxKey());  
//            String sgin=MD5.sign(sb.toString());
//            return sgin;
//     }
     /**
      * 获取ip地址
      * @param request
      * @return
      */
     public static String getIpAddr(HttpServletRequest request) {  
         InetAddress addr = null;  
         try {  
             addr = InetAddress.getLocalHost();  
         } catch (UnknownHostException e) {  
             return request.getRemoteAddr();  
         }  
         byte[] ipAddr = addr.getAddress();  
         String ipAddrStr = "";  
         for (int i = 0; i < ipAddr.length; i++) {  
             if (i > 0) {  
                 ipAddrStr += ".";  
             }  
             ipAddrStr += ipAddr[i] & 0xFF;  
         }  
         return ipAddrStr;  
     }
     /**
      * 获得指定长度的随机字符串
      * @author Administrator
      *
      */
     public class StringWidthWeightRandom {
         private int length = 32;  
         private char[] chars = new char[]{
             '0','1','2','3','4','5','6','7','8','9',
             'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
             'A','B','V','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
         };  
         private Random random = new Random();  
         
         //参数为生成的字符串的长度，根据给定的char集合生成字符串  
         public String getNextString(int length){      
               
             char[] data = new char[length];  
               
             for(int i = 0;i < length;i++){  
                 int index = random.nextInt(chars.length);  
                 data[i] = chars[index];  
             }  
             String s = new String(data);  
             return s;  
         }

         public int getLength() {
             return length;
         }

         public void setLength(int length) {
             this.length = length;
         }  
     }
     
     public static class UtilDate {
    	    
    	    /** 年月日时分秒(无下划线) yyyyMMddHHmmss */
    	    public static final String dtLong                  = "yyyyMMddHHmmss";
    	    
    	    /** 完整时间 yyyy-MM-dd HH:mm:ss */
    	    public static final String simple                  = "yyyy-MM-dd HH:mm:ss";
    	    
    	    /** 年月日(无下划线) yyyyMMdd */
    	    public static final String dtShort                 = "yyyyMMdd";
    	    
    	    
    	    /**
    	     * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
    	     * @return
    	     *      以yyyyMMddHHmmss为格式的当前系统时间
    	     */
    	    public  static String getDateLong(){
    	        Date date=new Date();
    	        DateFormat df=new SimpleDateFormat(dtLong);
    	        return df.format(date);
    	    }
    	    
    	    /**
    	     * 获取系统当前日期(精确到毫秒)，格式：yyyy-MM-dd HH:mm:ss
    	     * @return
    	     */
    	    public  static String getDateFormatter(){
    	        Date date=new Date();
    	        DateFormat df=new SimpleDateFormat(simple);
    	        return df.format(date);
    	    }
    	    
    	    /**
    	     * 获取系统当期年月日(精确到天)，格式：yyyyMMdd
    	     * @return
    	     */
    	    public static String getDate(){
    	        Date date=new Date();
    	        DateFormat df=new SimpleDateFormat(dtShort);
    	        return df.format(date);
    	    }
    	    
    	    
    	    
    	}
     public static class MD5 {
    	    public static String sign(String str){
    	        MessageDigest md5;
    	        String sgin = "";
    	        try {
    	            md5 = MessageDigest.getInstance("MD5");
    	            md5.reset();
    	            md5.update(str.getBytes("UTF-8"));
    	            sgin = byteToStr(md5.digest()).toUpperCase();
    	        } catch (NoSuchAlgorithmException e) {
    	            e.printStackTrace();
    	        } catch (UnsupportedEncodingException e) {
    	            e.printStackTrace();
    	        }
    	        return sgin;
    	    }
    	    
    	    /**
    	     * 将字节数组转换为十六进制字符串
    	     * 
    	     * @param byteArray
    	     * @return
    	     */
    	    public static String byteToStr(byte[] byteArray) {
    	        String strDigest = "";
    	        for (int i = 0; i < byteArray.length; i++) {
    	            strDigest += byteToHexStr(byteArray[i]);
    	        }
    	        return strDigest;
    	    }

    	    /**
    	     * 将字节转换为十六进制字符串
    	     * 
    	     * @param btyes
    	     * @return
    	     */
    	    public static String byteToHexStr(byte bytes) {
    	        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    	        char[] tempArr = new char[2];
    	        tempArr[0] = Digit[(bytes >>> 4) & 0X0F];
    	        tempArr[1] = Digit[bytes & 0X0F];

    	        String s = new String(tempArr);
    	        return s;
    	    }
    	    
    	}
     /**将string 转成xml格式
     * @param xmlStr
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
//    public Document toXML(String xmlStr) throws ParserConfigurationException, SAXException, IOException {
//    	 StringReader sr = new StringReader(xmlStr); 
//    	 InputSource is = new InputSource(sr); 
//    	 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
//    	 DocumentBuilder builder=factory.newDocumentBuilder(); 
//    	 Document doc = (Document) builder.parse(is); 
//    	 return doc;
//     }
//	
    }
