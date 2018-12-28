package com.everwing.coreservice.common.solr.entity.customer;

import com.everwing.coreservice.common.Page;

/**
 * @author shiny
 **/
public class BuildingSearch extends Building {

    private Page page;

    private String keyWord;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
