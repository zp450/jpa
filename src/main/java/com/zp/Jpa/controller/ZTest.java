package com.zp.Jpa.controller;

import java.util.HashMap;
import java.util.Map;

public class ZTest {
 public static void main(String[] args) {
	 Map uidSessionIdmap = new HashMap<>();
	 uidSessionIdmap.put(12, 20);
	 Integer id=12;
	 System.out.println(uidSessionIdmap.get(id));
//	 @Autowired
//	 SysUserServiceImpl SysUserServiceImpl;
//	 SysUserServiceImpl serviceImpl=new SysUserServiceImpl();
//	 Integer aInteger=6032;
//	 Integer reid=60323;
//	 System.out.println(!aInteger.equals(reid));
//	 
//	 Object object=serviceImpl.getTokenAndListUsers(6035);
//	 System.out.println(object);
//	 BigDecimal money=new BigDecimal("1000");
//	BigDecimal directPush =new BigDecimal("0.0");
//	if (money.compareTo(new BigDecimal("150000")) >-1) {
//		directPush =new BigDecimal("0.25");
//	} else if (money.compareTo(new BigDecimal("100000")) > -1 && money.compareTo(new BigDecimal("150000")) == -1) {
//		directPush = new BigDecimal("0.20");
//	} else if (money.compareTo(new BigDecimal("50000")) > -1 && money.compareTo(new BigDecimal("100000"))  == -1) {
//		directPush = new BigDecimal("0.15");
//	} else if (money.compareTo(new BigDecimal("600")) > -1 && money.compareTo(new BigDecimal("50000"))  == -1) {
//		System.out.println("我是600到50000的");
//		directPush = new BigDecimal("0.10");
//	} else {// 如果小于600股就没有收益
//		directPush = new BigDecimal("0.00");
//	}
//	
//		System.out.println(directPush);
//}
	 
	 
 }
}
