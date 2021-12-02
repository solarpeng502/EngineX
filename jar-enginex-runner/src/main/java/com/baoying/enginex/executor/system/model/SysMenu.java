
package com.baoying.enginex.executor.system.model;

import java.io.Serializable;
import java.util.Date;



public class SysMenu implements Serializable {

    private static final long serialVersionUID = -1L;

    private long id; 
    private long userId;//分配者
    private String name; //资源名称
    private String code;//资源代号
    private String url;//路径
    private long parentId;//父节点
    private String des;
    private Date birth;//创建时间
    private String icon;//图标
    private int status;//状态
    private long roleId;//角色id
    private boolean checked;//菜单默认选中
    private boolean chkDisabled;//节点是否禁用
    private boolean isHidden;//节点是否隐藏
    
    
    
    
	public boolean isisHidden() {
		return isHidden;
	}
	public void setisHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	public boolean isChkDisabled() {
		return chkDisabled;
	}
	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	@Override
	public String toString() {
		return "SysMenu [id=" + id + ", userId=" + userId + ", name=" + name
				+ ", versionCode=" + code + ", url=" + url + ", parentId=" + parentId
				+ ", des=" + des + ", birth=" + birth + ", icon=" + icon
				+ ", status=" + status + ", roleId=" + roleId + ", checked="
				+ checked + ", chkDisabled=" + chkDisabled + ", isHidden="
				+ isHidden + "]";
	}
	
	
    
}
