package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
@Table(name="Action")
public class Action {
	
	@Id
	@GeneratedValue
    private Integer  aid;
	private String actionDes;
    private String actionType;
    private String actionIp;
    private Integer userId;
    private String actionTime;
}
