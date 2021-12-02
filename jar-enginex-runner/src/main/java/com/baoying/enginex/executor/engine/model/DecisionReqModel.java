package com.baoying.enginex.executor.engine.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class DecisionReqModel implements Serializable {
    private static final long serialVersionUID = 1743177499998353115L;

    private String pid;
    private String uid;
    private JSONObject fields;

}
