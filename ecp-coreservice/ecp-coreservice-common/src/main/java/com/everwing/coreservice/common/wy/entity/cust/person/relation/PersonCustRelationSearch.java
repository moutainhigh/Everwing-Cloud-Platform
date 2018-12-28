package com.everwing.coreservice.common.wy.entity.cust.person.relation;

import com.everwing.coreservice.common.Page;

public class PersonCustRelationSearch extends PersonCustRelation {

    private static final long serialVersionUID = 5029780892939746298L;

    Page page = new Page();

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
        return "PersonCustRelationSearch{" +
                "page=" + page +
                '}';
    }
}
