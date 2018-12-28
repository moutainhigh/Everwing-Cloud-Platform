package com.everwing.coreservice.common.wy.entity.product;/**
 * Created by wust on 2017/9/27.
 */

import com.everwing.coreservice.common.Page;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/9/27
 * @author wusongti@lii.com.cn
 */
public class TProductOrderSearch extends TProductOrder {
    private static final long serialVersionUID = 4313125559306242914L;

    private Page page;

    private String beginTime;

    private String endTime;

    private List<String> statusList;

    private String searchKeyWord;


    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }


    public String getSearchKeyWord() {
        return searchKeyWord;
    }

    public void setSearchKeyWord(String searchKeyWord) {
        this.searchKeyWord = searchKeyWord;
    }
}
