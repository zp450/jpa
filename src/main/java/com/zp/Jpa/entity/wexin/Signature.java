package com.zp.Jpa.entity.wexin;

import java.io.Serializable;

import lombok.Data;

/**
 * 签名实体类
 * 
 * @author rory.wu
 *
 */
@Data
public class Signature implements Serializable {
	private static final long serialVersionUID = -7799030247222127708L;

	private String url;
	private String jsapi_ticket;
	private String nonceStr;
	private String timestamp;
	private String signature;

}
