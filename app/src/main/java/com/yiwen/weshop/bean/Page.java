package com.yiwen.weshop.bean;

import java.io.Serializable;
import java.util.List;



public class Page<T> implements Serializable {

    /**
     * totalCount : 28
     * currentPage : 1
     * totalPage : 28
     * pageSize : 1
     * list : []
     */

    private int totalCount;
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private List<T> list;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
