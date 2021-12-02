package com.baoying.enginex.executor.engine.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_engine_node")
public class EngineNode implements Serializable{
	private static final long serialVersionUID = -1867357850853531748L;

	/**
	 * 节点编号
	 */
    @TableId(type = IdType.AUTO)
    private Long nodeId;
    
    /**
     * 版本编号
     */
    private Long versionId;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点code
     */
    private String nodeCode;

    /**
     * 节点顺序
     */
    private Integer nodeOrder;

    /**
     * 节点类型
     */
    private Integer nodeType;

    /**
     * 节点json
     */
    private String nodeJson;

    /**
     * 节点X轴
     */
    private double nodeX;

    /**
     * 节点Y轴
     */
    private double nodeY;
    
    /**
     * 节点脚本
     */
    private String nodeScript;

    /**
     * 下一节点
     */
    private String nextNodes;

    /**
     * 节点类型,图标等信息
     */
    private String params;

    /**
     * 父节点编号
     */
    private String parentId;

    /**
     * 节点配置快照
     */
    private String snapshot;
}