package io.milkwang.framework.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

/**
 * 用于继承的基类
 */
public abstract class BaseDomain implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 是否删除
     */
    @Schema(hidden = true, title = "是否删除")
    private Boolean del;
    /**
     * 创建时间
     */
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY, title = "创建时间")
    private Date createTime;
    /**
     * 更新时间
     */
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY, title = "更新时间")
    private Date updateTime;
    /**
     * 创建者
     */
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY, title = "创建者")
    private String createdBy;
    /**
     * 更新者
     */
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY, title = "更新者")
    private String modifiedBy;

    /**
     * 页码,从1开始
     */
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, title = "页码", example = "1")
    private Integer page = 1;

    /**
     * 每页条数
     */
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, title = "每页条数", example = "10")
    private Integer pageSize = 10;

    public Boolean getDel() {
        return del;
    }

    public void setDel(Boolean del) {
        this.del = del;
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
     * @return 页码
     */
    @Schema(hidden = true)
    public int getOffset() {
        if (page < 1) {
            return 0;
        }

        return (page - 1) * pageSize;
    }

    /**
     * mysql limit, 0-based, 根据page和pageSize计算
     *
     * @return 页码
     */
    @Schema(hidden = true)
    public int getLimit() {
        return pageSize;
    }


    @Override
    public String toString() {
        return "BaseDomain{" +
                "del=" + del +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
