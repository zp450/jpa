package com.zp.Jpa.entity;




import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

   /**
    * @文件名称：PayApply.java
    * @创建时间：2019-05-27 15:03:59
    * @创  建  人：zp 
    * @文件描述：PayApply 实体类
    * @文件版本：V0.01 
    */ 

@Data
@Entity
@Table(name="PayApply")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class PayApply{
	@Id
//	@GeneratedValue(generator="system-uuid")
//	@GenericGenerator(name="system-uuid", strategy="uuid")
	
//	@Column(name="PAid" , columnDefinition = "uniqueidentifier" , nullable=false )
	
//	@Id
//    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
//    @GeneratedValue(strategy = GenerationType.AUTO)
	
//	@Id
//    @GeneratedValue(generator = "jpa-uuid")
	@Column(name="PAid" , columnDefinition = "nvarchar(32)" , nullable=false )
	private String pAid;
	@Column(name="ApplyUser" , columnDefinition = "int" , nullable=false )
	private Integer applyUser;
	@Column(name="ApplyName" , columnDefinition = "nvarchar(50)" , nullable=false )
	private String applyName;
	@Column(name="ApplyDate" , columnDefinition = "datetime" , nullable=false )
	private Date applyDate;
	@Column(name="ApplyMoney" , columnDefinition = "money" , nullable=false )
	private Long applyMoney;
	@Column(name="Mobile" , columnDefinition = "nvarchar(50)" , nullable=false )
	private String mobile;
	@Column(name="CardId" , columnDefinition = "nvarchar(50)" , nullable=false )
	private String cardId;
	@Column(name="BankCardId" , columnDefinition = "nvarchar(50)" , nullable=false )
	private String bankCardId;
	@Column(name="Bank" , columnDefinition = "nvarchar(50)" , nullable=false )
	private String bank;
	@Column(name="BankName" , columnDefinition = "nvarchar(50)" , nullable=false )
	private String bankName;
	@Column(name="Remark" , columnDefinition = "ntext" , nullable=false )
	//ntext
	private String remark;
	@Column(name="OperationName" , columnDefinition = "int" , nullable=true )
	private Integer operationName;
	@Column(name="OperationTime" , columnDefinition = "datetime" , nullable=true )
	private Date OperationTime;
	@Column(name="PayType" , columnDefinition = "int" , nullable=false )
	private Integer payType;
	@Column(name="Sates" , columnDefinition = "int" , nullable=false )
	private Integer sates;
	@Column(name="IsDel" , columnDefinition = "int" , nullable=false )
	private Integer isDel;
	@Column(name="ONumber" , columnDefinition = "int" , nullable=true )
	private Integer oNumber;
	
}

