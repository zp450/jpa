package com.zp.Jpa.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name="LinkMan")
public class LinkMan {
	@Id
	@GeneratedValue
    private Integer lmId;
	//主要联系人id
    private Integer parentId;
    private String name;
    private String mobile;
    @Transient
    private List<String> IDcardPic;
    private String IDcardPic1;//身份证
    private String IDcardPic2;//身份证
    
//    private String IDcardPic;
    private String passportPic;//护照
    @Column(name="addTime" , columnDefinition = "datetime DEFAULT getdate()   " , nullable=true,insertable=false,  updatable = false )
    private Date addTime;
}
