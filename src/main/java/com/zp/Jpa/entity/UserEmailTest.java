package com.zp.Jpa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="UserEmailTest")
public class UserEmailTest {
	@Id
	@GeneratedValue
  private Integer uid;
  private String userName;
  private String userEmail;
  private String userPassWord;
  private Integer state;
  private String code;
}
