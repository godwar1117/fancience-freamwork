package com.fancience.api.utils;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.plugins.Page;
import java.io.Serializable;

/**
 * 分页信息接收对象
 * Created by Leonid on 17/11/9.
 */
public class PageForm<T> implements Serializable {

    private Integer pageSize;

    private Integer currentPage;

    private Page<T> page = null;

    public PageForm() {
    }

    private static final Integer DEFAULT_PAGE_SIZE = 10;
    private static final Integer DEFAULT_CURRENT_PAGE = 1;

    /**
     * 供 mybatis-plus 使用
     * @return
     */
    public Page<T> createPage() {
        page = new Page<T>(ObjectUtil.isNull(currentPage) ? DEFAULT_CURRENT_PAGE : currentPage,
                ObjectUtil.isNull(pageSize) ? DEFAULT_PAGE_SIZE : pageSize );
        return page;
    }

    public PageForm(Integer pageSize, Integer currentPage) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

}
