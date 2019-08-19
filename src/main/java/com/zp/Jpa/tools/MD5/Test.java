package com.zp.Jpa.tools.MD5;

public class Test {
	
	public static void main(String[] args) {
		PasswordEncoder encoder = null;
		
		encoder = new PasswordEncoder("马帅", "Md5");		
		System.out.println(encoder.encode("666"));
		
		encoder = new PasswordEncoder("马云", "Md5");
		System.out.println(encoder.encode("ysd123"));
		
		encoder = new PasswordEncoder("小张三", "Md5");
		String p1 = encoder.encode("ysd123");
		String p2 = encoder.encode(p1);
		String p3 = encoder.encode(p2);
		System.out.println("p3=>"+p3);
		
		PasswordEncoder encoder2=new PasswordEncoder("祝您生活愉快", "Md5");
		String newpassWord=encoder2.encode("123456");
		System.out.println("yanzhi=>"+newpassWord);
		
	}

}
