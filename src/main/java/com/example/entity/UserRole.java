package com.example.entity;
/*
* 
* gen by beetlsql3 2021-03-17
*/

import org.beetl.sql.annotation.entity.AssignID;
import org.beetl.sql.annotation.entity.Table;

@Table(name = "user_role")
public class UserRole implements java.io.Serializable {

	private static final long serialVersionUID = 2382762284262765344L;

	@AssignID("snow")
	private Long id;
	private Long userId;
	private Long roleId;

	public UserRole() {
	}

	public UserRole(Long userId) {
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
