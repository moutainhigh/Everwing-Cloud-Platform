package com.everwing.coreservice.common.platform.service;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.extra.Project;

/**
 * @author shiny
 * Created by DELL on 2018/4/3.
 */
public interface ProjectService {

    /**
     * 向平台同步项目
     * @param project 项目
     * @return 通用返回结果
     */
    RemoteModelResult add(Project project);

    /**
     * 修改项目
     * @param project 项目
     * @return 通用返回结果
     */
    RemoteModelResult modify(Project project);

    /**
     * 删除项目
     * @param project
     * @return
     */
    RemoteModelResult delete(Project project);
}
