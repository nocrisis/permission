package com.rbac.common.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

public class PageQuery {
    @Getter
    @Setter
    @Min(value = 1, message = "当前页码不合法")
    private int pageNo;
    @Getter
    @Setter
    @Min(value = 1, message = "每页展示数量不合法")
    private int pageSize;
    @Setter
    private int rowStart;

    public int getRowStart() {
        return (pageNo - 1) * pageSize;
    }
}
