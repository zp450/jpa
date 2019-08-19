package com.zp.Jpa.Adapter;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigTools {

	/**
	* 格式化当前日期
	* @return
	*/
	@Bean("dateFormat")
	public SimpleDateFormat dateFormat(){
	return new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	}
}
