package com.baoying.enginex.executor.tactics.model;


import com.baoying.enginex.executor.common.model.ExpressionParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutCondition {
    private String logical;
    private List<ExpressionParam> conditionList;
}
