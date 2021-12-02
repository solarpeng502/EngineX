package com.baoying.enginex.executor.engine.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("t_engine_version")
public class EngineVersion implements Serializable {
    private static final long serialVersionUID = 2923432053414979455L;

    /**
	 * 版本编号
	 */
    @TableId(type = IdType.AUTO)
    private Long versionId;

    /**
     * 引擎编号
     */
    private Long engineId;

    /**
     * 版本号
     */
    private Integer version;
    
    /**
     * 子版本
     */
    private Integer subVersion;

    /**
     * 部署状态
     */
    private Integer bootState;

    /**
     * 版本状态
     */
    private Integer status;

    /**
     * 布局方式
     */
    private Integer layout;

    /**
     * 创建者
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人
     */
    private Long latestUser;

    /**
     * 最后修改时间
     */
    private String latestTime;
    
    /**
     * 节点集合
     * */
    @TableField(exist = false)
    private List<EngineNode> engineNodeList;

}