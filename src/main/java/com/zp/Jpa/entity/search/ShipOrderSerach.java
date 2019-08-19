package com.zp.Jpa.entity.search;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ShipOrderSerach {
	private Integer page;
	private Integer size;
	private Integer isDel;
//	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME,pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME,pattern="yyyy-MM-dd")
	private Date begin;
//	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME,pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME,pattern="yyyy-MM-dd")
	private Date end;
   private String linesName;
}
