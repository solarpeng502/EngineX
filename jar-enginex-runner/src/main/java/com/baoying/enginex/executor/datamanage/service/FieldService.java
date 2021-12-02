package com.baoying.enginex.executor.datamanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baoying.enginex.executor.datamanage.model.Field;

import java.util.List;

public interface FieldService extends IService<Field> {

    Field queryById(Long id);

    List<Field> findFieldByIdsbyorganId(Long organId, List<Long> ids);

    List<Field> selectFieldListByEns(List<String> fieldEnList);

    Field findByFieldEnbyorganId(Long organId, String fieldEn);

    Field findByFieldCnbyorganId(Long organId, String fieldCn);
}
