package com.zp.Jpa.tools;

import java.util.Random;
import java.util.UUID;

public class Suijishu {
	//length用户要求产生字符串的长度
	 public static String getRandomString(int length){
	     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     for(int i=0;i<length;i++){
	       int number=random.nextInt(62);
	       sb.append(str.charAt(number));
	     }
	     return sb.toString();
	 }
	 //产生uuid
	 public static String getUuid() {
		 UUID uuid = UUID.randomUUID();
		 return uuid.toString();
	 }
	 //获得指定范围内的随机数
	 public static Integer getRandom(Integer min,Integer max) {
		 return new Random().nextInt(max-min)+min;
	 }
	 
	 /**生成指定长度的随机整数 长度至少为1
	 * @param length
	 * @return
	 */
	public static Integer getRandom(Integer length) {
		if(length<1) {
			return 0;
		}
		 int max=10;
		 int min=1;
		 Integer a=new Random().nextInt(max-min)+min;
		 int trueleng=length-1;
		 for (int i = 0; i < trueleng; i++) {
			 max=max*10;
			 min=min*10;
		}
		 return new Random().nextInt(max-min)+min;
	 }
	 public static void main(String[] args) {
		System.out.println(getRandomString(4));
	}
}
