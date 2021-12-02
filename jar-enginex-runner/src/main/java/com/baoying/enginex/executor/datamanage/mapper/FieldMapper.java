package com.baoying.enginex.executor.datamanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baoying.enginex.executor.datamanage.model.Field;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FieldMapper extends BaseMapper<Field> {

	/**
	 * findFieldByIds:(找出一批字段id对应的字段列表). <br/>
	 * @author caowenyu
	 * @param paramMap 参数集合
	 * @return 字段列表
	 */
	public List<Field> findFieldByIdsbyorganId(Map<String, Object> paramMap);

	/**
	 * findByFieldEn:(根据引擎和字段英文名找出引擎所用字段对象). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 字段对象
	 */
	public Field findByFieldEnbyorganId(Map<String, Object> paramMap);

	/**
	 * findByFieldCn:(根据字段中文名找出字段对象). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 字段对象
	 */
	public Field findByFieldCnbyorganId(Map<String, Object> paramMap);

	/**
	 * findByFieldCn:(按中文名查找通用字段). <br/>
	 * @author yuanlinfeng
	 * @param paramMap 参数集合
	 * @return 字段对象
	 */
	public Field findByFieldCnNoEngineIdbyorganId(Map<String, Object> paramMap);

	/**
	 * findByFieldId:(根据字段Id查找字段对象). <br/>
	 * @author caowenyu
	 * @param paramMap 参数集合
	 * @return 字段对象
	 */
	public Field findByFieldIdbyorganId(Map<String, Object> paramMap);

	 List<Field> selectFieldListByIds(@Param("ids") List<Long> ids);

	 List<Field> selectFieldListByEns(@Param("ens")List<String> ens);
}
