package com.zp.Jpa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
/**
 * 邮轮定制提交
 * 
 * */
@Data
@Entity
@Table(name="GroupCustomSumission")
public class GroupCustomSumission {
	@Id
	@GeneratedValue
	private Integer csId;
	private Integer uid;//用户id
	private Integer gid;//定制id
	private String name;//定制类型名字
	private String termini;//目的地
	@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME,pattern="yyyy-MM-dd")
	private Date departure;//出发预算日期
	private Integer days;//预算形成天数
	private Integer people;//成人数量 
	private Integer children;//儿童数量
	private Long budgetPrice;//预算费用
	private String placeOfIssue;//护照签发地
	private Integer groupLeader;//是否需要领队  1需要 0不需要
	private Integer tourGuide;//是否需要导游     1需要 0不需要
	private String roomType;//房间类型
	private Integer traffic;//是否需要往返交通1需要 0不需要
	private String appellation;//称谓
	private String trueName;//联系人
	private String mobile;//联系电话
	private String email;//邮箱
	
	private String userRemark;//用户备注
	
	private Integer auditStatue;//审核状态  0未审核  1已审核 2审核未通过
	private Date addTime;
	private Date editTime;
	private Integer payStatus;
	private Integer orderStatus;
	private Integer isDel;
}
