

package com.baoying.enginex.executor.datamanage.mapper;


import com.baoying.enginex.executor.common.mapper.BaseMapper;
import com.baoying.enginex.executor.datamanage.model.FieldTypeUser;

import java.util.Map;

public interface FieldTypeUserMapper extends BaseMapper<FieldTypeUser> {

	/**
	 * createFieldTypeUserRel:(新增字段类型). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 插入成功
	 */
	public boolean createFieldTypeUserRel(Map<String, Object> paramMap);

	/**
	 * batchBindEngineFieldTypeUserRel:(把一批通用字段类型id中不存在的类型id批量绑定到引擎). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 插入成功
	 */
	public boolean batchBindEngineFieldTypeUserRel(Map<String, Object> paramMap);

	/**
	 * deleteFieldTypeUserRel:(取消字段类型). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 删除成功
	 */
	public boolean deleteFieldTypeUserRel(Map<String, Object> paramMap);
	
	/**
	 * updateFieldTypeUserRel:(更新字段类型名). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 更新成功
	 */
	public boolean updateFieldTypeUserRel(Map<String, Object> paramMap);
	
	/**
	 * findNodeIds:(查找引擎在用的节点集合). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 
	 */
	public String findNodeIds(Map<String, Object> paramMap);
	
	
}
