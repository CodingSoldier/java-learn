package com.vanrui.dto;

import java.io.Serializable;

/**
 * 修改业主的扩展信息
 * @author yangliu
 * @date 2019/1/23 14:16
 */
public class EmpExternalDTO implements Serializable {

    private String spatialCodes;

    private int action;

    public String getSpatialCode() {
        return spatialCodes;
    }

    public void setSpatialCode(String spatialCodes) {
        this.spatialCodes = spatialCodes;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "EmpExternalDTO{" +
                "spatialCode=" + spatialCodes +
                ", action=" + action +
                '}';
    }
}
