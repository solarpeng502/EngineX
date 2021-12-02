package com.baoying.enginex.executor;

import com.baoying.enginex.executor.util.JevalUtil;
import com.baoying.enginex.executor.util.jeval.EvaluationException;
import com.baoying.enginex.executor.util.jeval.Evaluator;

import java.util.HashMap;

public class JevalTest {
    public static void main(String[] args) throws EvaluationException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ceshiyong","'a'");
        String expression = "(contains(#{ceshiyong},'a'))";
        System.out.println(JevalUtil.evaluateBoolean(expression.toString(), map));
    }
}
