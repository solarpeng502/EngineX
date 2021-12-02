
package com.baoying.enginex.executor.system.mapper;


import com.baoying.enginex.executor.common.mapper.BaseMapper;
import com.baoying.enginex.executor.system.model.Department;


public interface DepartmentMapper extends BaseMapper<Department> {
    
	/**
	 * isExist:(根据相应的条件判断是否存在重复值). <br/>
	 * @author wz
	 * @param departmentVo 部门实体类
	 * @return 返回行数
	 */
	Integer isExist(Department department);
	
	/**
	 * deleteDept:(根据部门ids删除部门信息). <br/>
	 * @author wz
	 * @param deletIds 部门ids
	 */
	void deleteDept(Long[] deletIds);
}

