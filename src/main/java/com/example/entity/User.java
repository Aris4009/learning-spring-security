package com.example.entity;
/*
* 
* gen by beetlsql3 2021-03-17
*/

import org.beetl.sql.annotation.entity.AssignID;
import org.beetl.sql.annotation.entity.Table;

@Table(name = "user")
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 7868936920951566972L;

	@AssignID("snow")
	private Long id;
	private String username;
	private String password;

	public User() {
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
