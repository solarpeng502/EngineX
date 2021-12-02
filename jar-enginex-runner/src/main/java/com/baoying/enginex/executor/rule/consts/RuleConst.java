package com.baoying.enginex.executor.rule.consts;

public class RuleConst {

    public static final int CONST_TYPE = 1;//常量
    public static final int VARIABLE_TYPE = 2;//变量

    public static final int RELATION_CONDITION = 1;//关系节点表示&&或者||
    public static final int EXPRESSION_CONDITION = 2;//表达式条件
    public static final int LOOP_CONDITION = 3;//循环条件
    public static final int LOOP_RESULT_CONDITION = 4;//循环规则条件
    public static final int CONDITION_GROUP_CONDITION = 5;//条件组节点
    public static final int CONDITION_RESULT_CONDITION = 6;//条件组节点


    public static final int LOOP_GROUP_ACTION_TYPE_SUM = 1;//循环中求和
    public static final int LOOP_GROUP_ACTION_TYPE_ASSIGNMENT = 2;//赋值

    public static final int LOOP_GROUP_ACTION_TYPE_OUT_VARIABLE = 3;//输出变量
    public static final int LOOP_GROUP_ACTION_TYPE_OUT_CONST = 4;//输出常量

    public static class ScriptType {
        public static final String GROOVY = "groovy";
        public static final String PYTHON = "python";
        public static final String JAVASCRIPT = "js";

    }
}
