package com.zp.Jpa.entity.sys;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Table(name="SysModule")
@Data
public class SysModule {
	@Id
	@GeneratedValue
	@JsonProperty("id")
	private Integer moduleId;
	
	@Column(columnDefinition="int  DEFAULT 0 ",nullable=false)
	private Integer parentId;//父模块Id
	
	@JsonProperty("text")
	@Column(columnDefinition="varchar(20)")
	private String moduleName;//模块名称
	
	@Column(columnDefinition="int  DEFAULT 0")
	private Integer moduleWeight;//模块权重,列表顺序
	
	@Column(columnDefinition="varchar(50) ")
	private String moduleUrl;//模块对应页面url路径
	
	@Transient  //临时参数
	private boolean checked;//是否选中状态
	
	@Transient
	private List<SysModule> children;//父模块下的孩子
	
	 @Column(name="moduleCreateTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=false,  updatable = false )
	
	private Timestamp moduleCreateTime;
	
	 @Column(name="moduleLastUpdateTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=true,  updatable = true )
	private Timestamp moduleLastUpdateTime;
	
	//@ManyToMany(mappedBy="modulelist")
	//private List<Role> rList;
	
	

}
