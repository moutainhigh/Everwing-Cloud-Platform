package com.everwing.coreservice.common.wy.entity.property.PublicAsset;

import com.everwing.coreservice.common.Page;

public class TcPublicAssetSearch extends TcPublicAsset {

    private static final long serialVersionUID = -4806137444458021372L;

    private Page page;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "TcPublicAssetSearch{" +
                "page=" + page +
                '}';
    }
}
