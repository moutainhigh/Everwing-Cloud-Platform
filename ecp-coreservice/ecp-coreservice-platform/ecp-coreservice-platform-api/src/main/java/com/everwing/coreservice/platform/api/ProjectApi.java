package com.everwing.coreservice.platform.api;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.extra.Project;
import com.everwing.coreservice.common.platform.service.ProjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * project 同步类
 *
 * @author DELL shiny
 * @create 2018/4/3
 */
@Service
public class ProjectApi {

    private static final Logger logger= LogManager.getLogger(ProjectApi.class);

    @Autowired
    private ProjectService projectService;

    /**
     * 将id一起带过来
     * @param project project
     * @return 通用返回结果
     */
    public RemoteModelResult addProject(Project project){
        if(project.getProjectId()==null){
            RemoteModelResult rs=new RemoteModelResult();
            rs.setCode(ReturnCode.SYSTEM_ERROR.getCode());
            logger.debug("同步项目未传入id,拒绝处理");
            rs.setMsg("未传入项目id，拒绝处理");
            return rs;
        }
        if(project.getCompanyId()==null){
            RemoteModelResult rs=new RemoteModelResult();
            rs.setCode(ReturnCode.SYSTEM_ERROR.getCode());
            logger.debug("同步项目未传入公司id,拒绝处理");
            rs.setMsg("未传入项目公司id，拒绝处理");
            return rs;
        }
        return projectService.add(project);
    }

    /**
     * 通过id修改项目
     * @param project project
     * @return common result
     */
    public RemoteModelResult modifyProject(Project project){
        if(project.getProjectId()==null){
            RemoteModelResult rs=new RemoteModelResult();
            rs.setCode(ReturnCode.SYSTEM_ERROR.getCode());
            logger.debug("修改项目未传入id,拒绝处理");
            rs.setMsg("修改项目未传入id,拒绝处理");
            return rs;
        }
        return projectService.modify(project);
    }

    /**
     * 删除项目通过id
     * @param project project
     * @return common result
     */
    public RemoteModelResult deleteProject(Project project){
        if(project.getProjectId()==null){
            RemoteModelResult rs=new RemoteModelResult();
            rs.setCode(ReturnCode.SYSTEM_ERROR.getCode());
            logger.debug("删除项目未传入id,拒绝处理");
            rs.setMsg("删除项目未传入id,拒绝处理");
            return rs;
        }
        return projectService.delete(project);
    }
}
