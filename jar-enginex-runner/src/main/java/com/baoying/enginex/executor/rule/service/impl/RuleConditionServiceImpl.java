package com.baoying.enginex.executor.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baoying.enginex.executor.canal.TableEnum;
import com.baoying.enginex.executor.common.constants.Constants;
import com.baoying.enginex.executor.config.ConfigHolder;
import com.baoying.enginex.executor.redis.RedisManager;
import com.baoying.enginex.executor.redis.RedisUtils;
import com.baoying.enginex.executor.rule.consts.RuleConditionConst;
import com.baoying.enginex.executor.rule.mapper.RuleConditionInfoMapper;
import com.baoying.enginex.executor.rule.model.RuleConditionInfo;
import com.baoying.enginex.executor.rule.model.RuleLoopGroupAction;
import com.baoying.enginex.executor.rule.model.vo.RuleConditionVo;
import com.baoying.enginex.executor.rule.service.RuleConditionService;
import com.baoying.enginex.executor.rule.service.RuleLoopGroupActionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service("ruleConditionService2")
public class RuleConditionServiceImpl extends ServiceImpl<RuleConditionInfoMapper, RuleConditionInfo> implements RuleConditionService {

    @Resource
    private RuleConditionInfoMapper ruleConditionInfoMapper;
    @Resource
    private RuleLoopGroupActionService loopGroupActionService;
    @Autowired
    private ConfigHolder configHolder;
    @Autowired
    private RedisManager redisManager;

    @Override
    public RuleConditionVo queryByVersionId(Long versionId) {
        if (versionId == null) {
            return null;
        }
        //构造查询条件，查询条件列表
        RuleConditionVo result = null;

        List<RuleConditionInfo> ruleConditionInfoList = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            String key = RedisUtils.getForeignKey(TableEnum.T_RULE_CONDITION, versionId);
            ruleConditionInfoList = redisManager.getByForeignKey(key, RuleConditionInfo.class);
        } else {
            LambdaQueryWrapper<RuleConditionInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RuleConditionInfo::getVersionId, versionId);
            ruleConditionInfoList = ruleConditionInfoMapper.selectList(queryWrapper);
        }

        //组装为需要的树形结构
        if (ruleConditionInfoList != null) {
            result = this.assemble(ruleConditionInfoList);
        }
        return result;
    }

    @Override
    public List<String> queryFieldEnByVersionIds(List<Long> versionIds) {
        List<RuleConditionInfo> ruleConditions = null;
        if(Constants.switchFlag.ON.equals(configHolder.getCacheSwitch())){
            List<String> keys = RedisUtils.getForeignKey(TableEnum.T_RULE_CONDITION, versionIds);
            ruleConditions = redisManager.hgetAllBatchByForeignKeys(keys, RuleConditionInfo.class);

            ruleConditions = ruleConditions.stream()
                    .filter(item -> StringUtils.isNotBlank(item.getFieldEn()) && !"1".equals(item.getFieldType()))
                    .collect(Collectors.toList());

        } else {
            LambdaQueryWrapper<RuleConditionInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(RuleConditionInfo::getVersionId,versionIds);
            queryWrapper.isNotNull(RuleConditionInfo::getFieldEn);
            queryWrapper.ne(RuleConditionInfo::getFieldType,1);
            queryWrapper.select(RuleConditionInfo::getFieldEn);
            ruleConditions = ruleConditionInfoMapper.selectList(queryWrapper);
        }

        List<String> result = new ArrayList<>();
        if (ruleConditions != null){
            for (RuleConditionInfo condition : ruleConditions) {
                result.add(condition.getFieldEn());
            }
        }
        return result;
    }

    //装配方法，将规则条件List装配成一个规则树并返回
    public RuleConditionVo assemble(List<RuleConditionInfo> list) {
        RuleConditionVo root = null;
        //转换为Vo
        List<RuleConditionVo> rcVoList = transferToVoList(list);
        //获取根节点，根节点只有一个的时候进行操作，并且返回拼装好的规则树，否则返回null
        List<RuleConditionVo> collect = rcVoList.stream().filter(rc -> {
            return rc.getParentId() == RuleConditionConst.DEFAULT_CONDITION_PARENT_ID;
        }).collect(Collectors.toList());
        if (collect.size() == 1) {
            root = collect.get(0);
            RuleConditionVo ruleTree = coupling(rcVoList, root);
            return ruleTree;
        }
        return null;
    }

    //耦合方法：将规则节点列表耦合规则树(),循环规则的子节点需要去查循环表获取
    private RuleConditionVo coupling(List<RuleConditionVo> list, RuleConditionVo root) {
        List<RuleConditionVo> children = new ArrayList<>();
        for (RuleConditionVo rc : list) {
            //处理root的子节点
            if (root.getId().equals(rc.getParentId())) {
                RuleConditionVo rcVo = coupling(list, rc);
                String logical = root.getLogical();

                if (logical!=null&&!"".equals(logical)){
                    switch (logical){
                        //当root为for节点，则此子节点需要拼上循环动作
                        case RuleConditionConst.LOOP_RULE_LOGICAL:
                            List<RuleLoopGroupAction> loopList = loopGroupActionService.getRuleLoopList(root.getId(),rc.getId());
                            rcVo.setLoopGroupActions(loopList);
                            if (rc.getConditionType()==RuleConditionConst.LOOP_RULE_RESULT_CONDITION){
                                root.setLoopResultCondition(rcVo);
                                continue;
                            }
                            break;
                        //当root为条件组节点，则此子节点需要拼上条件组结果
                        case RuleConditionConst.CONDITION_GROUP_LOGICAL:
                            if (rc.getConditionType()==RuleConditionConst.CONDITION_GROUP_RESULT_CONDITION){
                                root.setCondGroupResultCondition(rcVo);
                                continue;
                            }
                            break;
                    }

                }
                children.add(rcVo);
            }
        }
        root.setChildren(children);
        return root;
    }

    //List<RuleConditionInfo>转换为List<RuleConditionVo>
    private List<RuleConditionVo> transferToVoList(List<RuleConditionInfo> list) {
        List<RuleConditionVo> rcVoList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RuleConditionVo rcVo = new RuleConditionVo();
            BeanUtils.copyProperties(list.get(i), rcVo);
            rcVoList.add(rcVo);
        }
        return rcVoList;
    }
}
