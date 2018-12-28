package com.everwing.autotask.core.service.bill;

/**
 * @author DELL shiny
 * @create 2018/6/4
 */
public interface TBsProjectService {

    void genNextProject(String companyId);

    void autoFlush(String companyId);
}
