package com.baoying.enginex.executor.engine.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("t_engine")
public class Engine implements Serializable {
	private static final long serialVersionUID = -6611916471057697499L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 引擎编号
	 */
	private String code;

	/**
	 * 引擎名称
	 */
	private String name;

	/**
	 * 引擎描述
	 */
	private String description;

	/**
	 * 引擎状态
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private Date createDatetime;

	/**
	 * 修改时间
	 */
	private Date updateDatetime;

	/**
	 * 创建人
	 */
	private Long creator;

	/**
	 * 修改人
	 */
	private Long userId;

	/**
	 * 公司编号
	 */
	private Long organId;
	
	/**
	 * 调用方式 1：同步，2：异步
	 */
	private Integer callbackType;

	/**
	 * 回调地址
	 */
	private String callbackUrl;

	/**
	 * 异常回调地址
	 */
	private String exceptionCallbackUrl;

	/**
	 * 引擎版本集合
	 */
	@TableField(exist = false)
	private List<EngineVersion> engineVersionList;

}
