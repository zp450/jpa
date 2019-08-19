package com.zp.Jpa.entity;

import lombok.Data;

@Data
public class UserUser {

	private Integer userId;
	private String userName;
	private String userPassWord;
	public UserUser(String userName, String userPassWord
			) {
			super();
			this.userName = userName;
			this.userPassWord = userPassWord;
			
		}
		public UserUser() {
			super();
		}
}
