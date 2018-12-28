package com.everwing.coreservice.platform.core.service.impl;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.extra.Project;
import com.everwing.coreservice.common.platform.service.ProjectService;
import com.everwing.coreservice.platform.dao.mapper.extra.ProjectDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 项目相关
 *
 * @author DELL shiny
 * @create 2018/4/3
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger= LogManager.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectDao projectDao;

    @Override
    public RemoteModelResult add(Project project) {
        int ex=projectDao.checkExists(project);
        if(ex>0){
            return modify(project);
        }else {
            int count = projectDao.insert(project);
            RemoteModelResult result = new RemoteModelResult();
            if (count == 0) {
                logger.info("项目添加失败!:{}", project);
                result.setCode(ReturnCode.SYSTEM_ERROR.getCode());
                result.setMsg("添加项目失败!");
            }
            return result;
        }
    }

    @Override
    public RemoteModelResult modify(Project project) {
        int count=projectDao.update(project);
        RemoteModelResult result=new RemoteModelResult();
        if(count==0){
            logger.info("项目修改失败!:{}",project);
            result.setCode(ReturnCode.SYSTEM_ERROR.getCode());
            result.setMsg("添加修改失败!");
        }
        return result;
    }

    @Override
    public RemoteModelResult delete(Project project) {
        int count=projectDao.delete(project);
        RemoteModelResult result=new RemoteModelResult();
        if(count==0){
            logger.info("项目删除失败!:{}",project);
            result.setCode(ReturnCode.SYSTEM_ERROR.getCode());
            result.setMsg("添加删除失败!");
        }
        return result;
    }
}
