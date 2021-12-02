package com.baoying.enginex.executor.common.mapper;

import com.baoying.enginex.executor.common.model.EmailTemplate;
import com.baoying.enginex.executor.common.model.EmailTemplateExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmailTemplateMapper {
    long countByExample(EmailTemplateExample example);

    int deleteByExample(EmailTemplateExample example);

    int deleteByPrimaryKey(Integer templateId);

    int insert(EmailTemplate record);

    int insertSelective(EmailTemplate record);

    List<EmailTemplate> selectByExampleWithBLOBs(EmailTemplateExample example);

    List<EmailTemplate> selectByExample(EmailTemplateExample example);

    EmailTemplate selectByPrimaryKey(Integer templateId);

    int updateByExampleSelective(@Param("record") EmailTemplate record, @Param("example") EmailTemplateExample example);

    int updateByExampleWithBLOBs(@Param("record") EmailTemplate record, @Param("example") EmailTemplateExample example);

    int updateByExample(@Param("record") EmailTemplate record, @Param("example") EmailTemplateExample example);

    int updateByPrimaryKeySelective(EmailTemplate record);

    int updateByPrimaryKeyWithBLOBs(EmailTemplate record);

    int updateByPrimaryKey(EmailTemplate record);

    EmailTemplate selectTemplateByNid(String nid);
}