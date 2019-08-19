package com.zp.Jpa.entity.sys;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="Log_icecoldmonitor")
public class Log_icecoldmonitor {
	@Id
	@GeneratedValue
  private Integer id;
  private String level;//优先级
  private String category;//类目
  private String thread;//进程
  private Date time;//时间
  private String location;//位置
  private String note;//日志信息
  
}
