
package com.baoying.enginex.executor.system.model;


import com.baoying.enginex.executor.common.model.BasePage;

import java.io.Serializable;


public class  UserRole extends BasePage implements Serializable{
   
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 用户id
	 */
	private Long  userId;
	/**
	 * 角色编码
	 */
	private String roleCode;
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
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	@Override
	public String toString() {
		return "UserRole [id=" + id + ", userId=" + userId + ", roleCode="
				+ roleCode + "]";
	}
	
	
}

