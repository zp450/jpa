package com.zp.Jpa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name="UserCheck")
public class UserCheck {
	@Id
	@GeneratedValue
   private Integer checkId;
   private Integer uid;
   private String userName;
   private Date checkTime;
   private Integer ContinuousCheckDay;
   private String remark;
   private Integer isDel;
   
//   @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
//   @JoinColumn(name="uid")//设置在article表中的关联字段(外键)
//   private Users users;//所属用户
   
//   @JsonIgnore 
//   @ManyToOne(targetEntity = Users.class) 
//   @JoinColumn(name="check_user_id") //副表中的外键字段名称
//   private Users users;
   
   //@JsonIgnore 
//   @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},optional=false)//可选属性optional=false,表示author不能为空。删除文章，不影响用户
//   @JoinColumn(name="uid")//设置在article表中的关联字段(外键)
//   private Users users;//所属作者
}

