package com.baoying.enginex.executor.rule.model.vo;


import com.baoying.enginex.executor.rule.model.RuleConditionInfo;
import com.baoying.enginex.executor.rule.model.RuleLoopGroupAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors
public class RuleConditionVo extends RuleConditionInfo {

//    private Long id;//主键ID
//
//    private String logical;//关系节点的逻辑符号：&&（并关系），||（或关系）
//
//    private Long fieldId;//表达式节点对应的字段id
//
//    private String fieldEn;//字段en
//    private String fieldType;//字段的类型：1中间变量 2 入参
//    private String operator;//表达式节点的操作符
//    private Integer variableType;//变量类型，1常量 2变量
//    private String fieldValue;//表达式节点对应字段的限定值
//    private String executionLogic;//执行逻辑
//    private Long ruleId;//规则表的id
//
//    private Long parentId;//父节点的id
//
//    private Integer conditionType;//规则节点的类型：1-关系节点，2-表达式节点
//
//    private Date createTime;//创建时间
//
//    private Date updateTime;//修改时间
//
//    private String insertTempId;//插入时临时id
//
//    private String TempParentId;//插入时临时父id

    private List<RuleConditionVo> children;//规则子节点

//    private Integer valueType;//字段类型
//
//    private List<RuleLoopGroupAction> loopGroupActions = new ArrayList<>();//循环组对应的条件，循环内的组条件下的操作列表
//
//    private RuleConditionVo loopResultCondition;//for对应的结果条件的计算条件树
//
//    private RuleConditionVo condGroupResultCondition;//条件组对应的结果计算条件树
}
