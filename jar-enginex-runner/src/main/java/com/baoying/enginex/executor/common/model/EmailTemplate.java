package com.baoying.enginex.executor.common.model;

import java.util.Date;

public class EmailTemplate {
    private Integer templateId;

    private String subject;

    private String nid;

    private Byte status;

    private String address;

    private Byte useType;

    private Date createTime;

    private Date updateTime;

    private String content;

    public EmailTemplate(Integer templateId, String subject, String nid, Byte status, String address, Byte useType, Date createTime, Date updateTime) {
        this.templateId = templateId;
        this.subject = subject;
        this.nid = nid;
        this.status = status;
        this.address = address;
        this.useType = useType;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public EmailTemplate(Integer templateId, String subject, String nid, Byte status, String address, Byte useType, Date createTime, Date updateTime, String content) {
        this.templateId = templateId;
        this.subject = subject;
        this.nid = nid;
        this.status = status;
        this.address = address;
        this.useType = useType;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.content = content;
    }

    public EmailTemplate() {
        super();
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid == null ? null : nid.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Byte getUseType() {
        return useType;
    }

    public void setUseType(Byte useType) {
        this.useType = useType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}