package com.baoying.enginex.executor.system.mapper;


import com.baoying.enginex.executor.common.mapper.BaseMapper;
import com.baoying.enginex.executor.system.model.User;
import com.baoying.enginex.executor.system.model.UserRole;

import java.util.List;
import java.util.Set;


public interface UserMapper extends BaseMapper<User> {
	/**
	 * isExist:(根据相应的条件判断是否存在重复值). <br/>
	 * @author wz
	 * @param user 用户实体类
	 * @return 返回行数
	 */
	Integer isExist(User user);
	
	/**
	 * selectLoginInfo:(用户登录判断). <br/>
	 * @author wz
	 * @param user 用户实体类
	 * @return  User 
	 */
	User selectLoginInfo(User user);
	
	/**
	 * deleteDept:(根据用户ids删除用户信息). <br/>
	 * @author wz
	 * @param user 用户id
	 */
	void deleteUser(User user);
	
	/**
	 * insertUserRole:(增加记录到用户角色关系表). <br/>
	 * @author wz
	 * @param userRolelist 用户角色关系list
	 */
	void insertUserRole(List<UserRole> userRolelist);
	
	/**
	 * deleteUserRole:(根据用户ids删除用户角色关联表信息). <br/>
	 * @author wz
	 * @param deletIds 用户ids
	 */
	void deleteUserRole(Long[] deletIds);
	
	/**
	 * findUserMenuSet:(根据用户名获取所授权的菜单). <br/>
	 * @author wz
	 * @param loginName 
	 * @return 根据用户名获取所授权的菜单
	 */
	Set<String> findUserMenuSet(String loginName);
}
