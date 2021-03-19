package com.example.entity;
import org.beetl.sql.annotation.entity.AssignID;
import org.beetl.sql.annotation.entity.Table;
/*
* 
* gen by beetlsql3 2021-03-19
*/

@Table(name="role_permission")
public class RolePermission implements java.io.Serializable {
	@AssignID("snow")
	private Long id ;
	private Long roleId ;
	private Long permissionId ;

	public RolePermission() {
	}

	public Long getId(){
		return  id;
	}
	public void setId(Long id ){
		this.id = id;
	}
	public Long getRoleId(){
		return  roleId;
	}
	public void setRoleId(Long roleId ){
		this.roleId = roleId;
	}
	public Long getPermissionId(){
		return  permissionId;
	}
	public void setPermissionId(Long permissionId ){
		this.permissionId = permissionId;
	}

}
