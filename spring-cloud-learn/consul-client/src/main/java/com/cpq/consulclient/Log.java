package com.cpq.consulclient;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author chenpiqian
 * @since 2019-04-01
 */
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 账号
     */
    private String loginName;

    /**
     * 操作功能项
     */
    private String operateItem;

    /**
     * 增删改查
     */
    private String operateType;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作结果true成功，false失败
     */
    private Boolean result;

    /**
     * 请求url
     */
    private String url;

    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 详情
     */
    private String detail;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getOperateItem() {
        return operateItem;
    }

    public void setOperateItem(String operateItem) {
        this.operateItem = operateItem;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
