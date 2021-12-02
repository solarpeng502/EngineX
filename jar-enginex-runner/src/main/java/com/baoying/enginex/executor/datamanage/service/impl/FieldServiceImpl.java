package com.baoying.enginex.executor.datamanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.common.session.SessionManager;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.datamanage.mapper.FieldMapper;
import com.baoying.enginex.executor.datamanage.model.Field;
import com.baoying.enginex.executor.datamanage.service.FieldService;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl extends ServiceImpl<FieldMapper, Field> implements FieldService {

    @Autowired
    public FieldMapper fieldMapper;
    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;

    @Override
    public Field queryById(Long id) {
        Field field = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, id);
            field = redisManager.getByPrimaryKey(key, Field.class);
        } else {
            field = fieldMapper.selectById(id);
        }

        return field;
    }

    @Override
    public List<Field> findFieldByIdsbyorganId(Long organId, List<Long> ids) {
        List<Field> fieldList = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            List<String> keys = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, ids);
            fieldList = redisManager.hgetAllBatchByPrimaryKeys(keys, Field.class);
        } else {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("organId", organId);
            paramMap.put("Ids", ids);
            fieldList = fieldMapper.findFieldByIdsbyorganId(paramMap);
        }
        return fieldList;
    }

    @Override
    public List<Field> selectFieldListByEns(List<String> fieldEnList) {
        List<Field> fieldList = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            Long organId = SessionManager.getSession().getOrganId();
            List<String> keys = fieldEnList.stream().map(item -> {
                String fieldEnStr = Constants.fieldName.fieldEn + ":" + organId + ":" + item;
                String fieldEnKey = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, fieldEnStr);
                return fieldEnKey;
            }).collect(Collectors.toList());

            fieldList = redisManager.hgetAllBatchByPrimaryKeys(keys, Field.class);

        } else {
            fieldList = fieldMapper.selectFieldListByEns(fieldEnList);
        }
        return fieldList;
    }

    @Override
    public Field findByFieldEnbyorganId(Long organId, String fieldEn) {
        Field field = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String fieldEnStr = Constants.fieldName.fieldEn + ":" + organId + ":" + fieldEn;
            String fieldEnKey = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, fieldEnStr);
            field = redisManager.getByPrimaryKey(fieldEnKey, Field.class);
            // todo 是否需要status = 1判断
        } else {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("organId", organId);
            paramMap.put("fieldEn", fieldEn);
            field = fieldMapper.findByFieldEnbyorganId(paramMap);
        }
        return field;
    }

    @Override
    public Field findByFieldCnbyorganId(Long organId, String fieldCn) {
        Field field = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String fieldCnStr = Constants.fieldName.fieldCn + ":" + organId + ":" + fieldCn;
            String fieldCnKey = RedisUtils.getPrimaryKey(TableEnum.T_FIELD, fieldCnStr);
            field = redisManager.getByPrimaryKey(fieldCnKey, Field.class);
            // todo 是否需要status = 1判断
        } else {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("organId", organId);
            paramMap.put("fieldCn", fieldCn);
            field = fieldMapper.findByFieldCnbyorganId(paramMap);
        }
        return field;
    }

}
