package com.milkwang.framework.domain;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApiModel("分页参数")
public class Page<T> {

    public Page() {
    }

    public Page(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public Page(Integer page, Integer pageSize, Integer total, List<T> list) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    public Page(BaseDomain page, Integer total, List<T> list) {
        this.page = page.getPage();
        this.pageSize = page.getPageSize();
        this.total = total;
        this.list = list;
    }

    @ApiModelProperty(value = "页码，从1开始", example = "1")
    private Integer page = 1;

    // 每页条数
    @ApiModelProperty(value = "每页条数", example = "10")
    private Integer pageSize = 10;

    /**
     * 总记录数, -1: 未知
     */
    private Integer total = -1;

    private List<T> list;

    public Integer getPage() {
        return page;
    }

    /**
     * 当前页码, 1-based
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * 每页记录数
     */
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPrevPage() {
        return this.isFirstPage() ? this.getPage() : this.getPage() - 1;
    }

    public Integer getNextPage() {
        return this.isLastPage() ? this.getPage() : this.getPage() + 1;
    }

    public boolean isFirstPage() {
        return (1 == this.getPage());
    }

    public boolean isLastPage() {
        if (-1 == this.getPageCount()) {
            return false;
        }

        return (this.getPageCount() < 1 || this.getPageCount() <= this.getPage());
    }

    /**
     * 页数, 根据total和pageSize计算
     * -1: 未知
     *
     * @return
     */
    public Integer getPageCount() {
        return getTotalPage();
    }

    public Integer getTotalPage() {
        if (-1 == total) {
            return -1;
        }

        if (total < 1) {
            return 0;
        }

        if (pageSize < 1) {
            return 1;
        }

        return (0 == total % pageSize) ? total / pageSize : total / pageSize
                + 1;
    }

    /**
     * mysql offset, 0-based, 根据page和pageSize计算
     *
     * @return
     */
    public Integer getOffset() {
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
    public Integer getLimit() {
        return pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + page;
        result = prime * result + pageSize;
        result = prime * result + total;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Page other = (Page) obj;
        if (page != other.page) {
            return false;
        }
        if (pageSize != other.pageSize) {
            return false;
        }
        if (total != other.total) {
            return false;
        }

        return true;
    }

    /**
     * 转成一个新的Page对象
     *
     * @param clazz 新的Item类型
     */
    public <F> Page<F> convert(Class<F> clazz) {
        return this.convert((input) -> JSON.parseObject(JSON.toJSONString(input), clazz));
    }

    /**
     * 转成一个新的Page对象
     *
     * @param mapFunction 对每一个对象应用map函数
     */
    public <F> Page<F> convert(Function<T, F> mapFunction) {
        Page<F> result = new Page<>();
        result.setPage(this.getPage());
        result.setPageSize(this.getPageSize());
        result.setTotal(this.getTotal());
        if (CollectionUtils.isEmpty(this.getList())) {
            this.setList(Lists.newArrayList());
            return result;
        }
        List<F> newList = this.getList().stream()
                .map(mapFunction)
                .collect(Collectors.toList());
        result.setList(newList);
        return result;
    }

    @Override
    public String toString() {
        return "Page{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", total=" + total +
                '}';
    }
}
