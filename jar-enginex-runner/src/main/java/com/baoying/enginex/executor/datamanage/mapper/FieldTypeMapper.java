

package com.baoying.enginex.executor.datamanage.mapper;


import com.baoying.enginex.executor.common.mapper.BaseMapper;
import com.baoying.enginex.executor.datamanage.model.FieldType;

import java.util.List;
import java.util.Map;

public interface FieldTypeMapper extends BaseMapper<FieldType> {

	/**
	 * getFieldTypeList:(查找用户的字段类型列表). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 字段类型列表
	 */
	public List<FieldType> getFieldTypeList(Map<String, Object> paramMap);

	/**
	 * getSubFieldTypeList:(根据传入的字段父类型查找子类型列表). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 字段类型列表
	 */
	public List<FieldType> getSubFieldTypeList(Map<String, Object> paramMap);

	/**
	 * findFieldTypeById:(根据传入的字段类型ID查找字段类型名). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 字段类型列表
	 */
	public FieldType findFieldTypeById(Map<String, Object> paramMap);

	/**
	 * findTypeIdByParentId:(根据传入的字段类型父ID查找子类型ID). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 子字段类型ID
	 */
	public String findTypeIdByParentId(Map<String, Object> paramMap);

	/**
	 * findTypeIdByParentId:(根据传入的字段类型类型ID查找父ID). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 子字段类型ID
	 */
	public String findParentIdByTypeId(Map<String, Object> paramMap);

	/**
	 * findFieldType:(查找用户可用的字段类型列表，通用组织所有，引擎只有自定义). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 字段类型列表
	 */
	public List<FieldType> findFieldType(Map<String, Object> paramMap);

	/**
	 * createFieldType:(新增字段类型). <br/>
	 * @author yuanlinfeng
	 * @param fieldTypeVo 字段类型实体类
	 * @return 插入成功
	 */
	public boolean createFieldType(FieldType fieldTypeVo);

	/**
	 * findIdByFieldType:(新增字段类型). <br/>
	 * @author yuanlinfeng
	 * @param paramMap paramMap
	 * @return 字段类型编号
	 */
	public long findIdByFieldType(Map<String, Object> paramMap);

	/**
	 * updateFieldType:(更新字段类型名). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 更新成功
	 */
	public boolean updateFieldType(Map<String, Object> paramMap);

	/**
	 * updateFieldTypeByTypeIds:(更新字段类型为删除状态(-1)). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 更新成功
	 */
	public boolean updateFieldTypeByTypeIds(Map<String, Object> paramMap);

	/**
	 * deleteFieldTypeByTypeIds:(删除字段类型下没有字段的空节点)). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 更新成功
	 */
	public boolean deleteFieldTypeByTypeIds(Map<String, Object> paramMap);

	/**
	 * backFieldTypeByTypeIds:(更新字段类型状态为启用状态(1)). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 更新成功
	 */
	public boolean backFieldTypeByTypeIds(Map<String, Object> paramMap);

	/**
	 * isExists:(查找字段名是否存在). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 存在的记录条数
	 */
	public int isExists(Map<String, Object> paramMap);


	/**
	 * isExistsDefaultTreeName:(查找默认节点名是否存在). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 存在的记录条数
	 */
	public int isExistsDefaultTreeName(Map<String, Object> paramMap);
	
	
}
