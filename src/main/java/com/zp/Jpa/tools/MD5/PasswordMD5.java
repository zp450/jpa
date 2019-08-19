package com.zp.Jpa.tools.MD5;

public class PasswordMD5 {
   /**盐值md5一次加密  
 * @param userName  用户名  
 * @param passWord  密码
 * @return  加密后密码
 */
public static String getNewPassWord(String userName,String passWord) {
	   PasswordEncoder encoder2=new PasswordEncoder(userName, "Md5");
		String newpassWord=encoder2.encode(passWord);
		System.out.println("密码==>"+passWord);
		System.out.println("盐==>"+userName);
		System.out.println("验证盐值加密后的密码=>"+newpassWord);
		return newpassWord;
   }
}
