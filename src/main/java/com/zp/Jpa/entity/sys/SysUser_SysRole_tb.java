package com.zp.Jpa.entity.sys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="SysUser_SysRole_tb")
@Data
public class SysUser_SysRole_tb {
	@Id
	private Integer user_id;
	private Integer role_id;
}
