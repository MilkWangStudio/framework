package com.milkwang.framework.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 用于继承的基类
 */
public abstract class BaseDomain implements Serializable {

    /**
     * 状态
     */
    private Status status;
    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Date createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Date updateTime;
    /**
     * 创建者
     */
    @ApiModelProperty(hidden = true)
    private String createdBy;
    /**
     * 更新者
     */
    @ApiModelProperty(hidden = true)
    private String modifiedBy;

    /**
     * 页码,从1开始
     */
    @ApiModelProperty(value = "页码从1开始", example = "1")
    private Integer page = 1;

    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * mysql offset, 0-based, 根据page和pageSize计算
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    public int getOffset() {
        if (page < 1) {
            return 0;
        }

        return (page - 1) * pageSize;
    }

    /**
     * mysql limit, 0-based, 根据page和pageSize计算
     *
     * @return
     */
    @ApiModelProperty(hidden = true)
    public int getLimit() {
        return pageSize;
    }


    @Override
    public String toString() {
        return "BaseDomain{" +
                "status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
