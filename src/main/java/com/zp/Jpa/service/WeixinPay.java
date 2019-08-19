package com.zp.Jpa.service;

import java.util.Map;

import com.zp.Jpa.entity.PayApply;

public interface WeixinPay {
	Map<String, Object> addPayApply(PayApply payApply) ;
}
