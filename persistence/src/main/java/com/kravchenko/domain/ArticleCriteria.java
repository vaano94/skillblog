package com.kravchenko.domain;

import java.io.Serializable;

/**
 * Created by john on 6/5/17.
 */
public class ArticleCriteria implements Serializable{

    private String tag;
    private int offset;
    private int pageSize;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
