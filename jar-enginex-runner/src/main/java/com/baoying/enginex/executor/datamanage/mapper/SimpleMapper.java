package com.baoying.enginex.executor.datamanage.mapper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface SimpleMapper {

    List<LinkedHashMap<String, Object>> customSelect(Map<String, Object> paramsMap);
    List<LinkedHashMap<String, Object>> test(Map<String, Object> paramsMap);
}
