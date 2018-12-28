package com.everwing.coreservice.common.wy.entity.property.Engineering;

import com.everwing.coreservice.common.Page;

public class

TcConstructionSearch  extends   TcConstruction {
    private static final long serialVersionUID = -6189726404849845242L;

    private Page page;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "TcConstructionSearch{" +
                "page=" + page +
                '}';
    }
}
