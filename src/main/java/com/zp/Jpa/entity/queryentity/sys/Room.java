package com.zp.Jpa.entity.queryentity.sys;

import javax.persistence.Transient;

import lombok.Data;

@Data
public class Room{
	@Transient
	private Object roomChoice;

	  private String roomNuber;
	  
	  
 }