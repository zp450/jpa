package com.zp.Jpa.entity.sys;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name="User_SysRole_tb")
@Data
public class User_SysRole_tb {
	@Id
   private Integer uid;
   private Integer role_id;
}
