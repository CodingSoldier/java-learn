package com.poly.tenantcommon.cloudmgt.log;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_log")
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


}
