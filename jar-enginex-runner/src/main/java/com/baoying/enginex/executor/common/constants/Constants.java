package com.baoying.enginex.executor.common.constants;

/**
 * 公共变量约定
 */
public class Constants {

    // 规则集节点相关常量
    public interface ruleNode {
        // 互斥组（串行）
        int MUTEXGROUP = 1;
        // 执行组（并行）
        int EXECUTEGROUP = 2;
    }

    public interface switchFlag {
        // 开关打开
        String ON = "on";
        // 开关关闭
        String OFF = "off";
    }

    public interface fieldName {
        // 字段英文名
        String fieldEn = "field_en";
        //字段中文名
        String fieldCn = "field_cn";
    }

}