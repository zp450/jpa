package com.zp.Jpa.entity.search;

import java.util.Date;

import lombok.Data;

@Data
public class SysUserSearch {
	private Integer page;
	private Integer size;
	private Integer isDel;
	private String userName;
	private Integer userIsLockout;
	// createTime
	private Date begin;
	private Date end;

	private String userLastLoginIp;
	private String userEmail;
	private String userTelephone;

	
}
