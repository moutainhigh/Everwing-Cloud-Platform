package com.everwing.coreservice.common.solr.entity.demo;/**
 * Created by wust on 2018/6/5.
 */

import com.everwing.coreservice.common.Page;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/5
 * @author wusongti@lii.com.cn
 */
public class SpringDataSolrDemoSearch extends SpringDataSolrDemo {
    private static final long serialVersionUID = -3260143897568987390L;
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
