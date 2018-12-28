package com.everwing.coreservice.common.platform.service;

/**
 * Created by shiny on 2017/5/31.
 */
public interface IdGenService {
    /**
     * 通过类型获取最大id值
     * @param type 类型
     * @return id值
     */
    int queryMaxIdByType(int type);

	int getIncreasedId(int type);
}
