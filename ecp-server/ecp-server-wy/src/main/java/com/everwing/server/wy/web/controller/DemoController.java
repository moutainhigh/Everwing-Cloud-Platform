package com.everwing.server.wy.web.controller;/**
 * Created by wust on 2018/6/4.
 */

import com.alibaba.fastjson.JSONArray;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.solr.entity.demo.SpringDataSolrDemo;
import com.everwing.coreservice.common.solr.entity.demo.SpringDataSolrDemoSearch;
import com.everwing.coreservice.solr.api.demo.SpringDataSolrDemoApi;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 *
 * Function:
 * Reason:
 * Date:2018/6/4
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/DemoController")
public class DemoController {
    @Autowired
    private SpringDataSolrDemoApi springDataSolrDemoApi;




    @RequestMapping(value = "/addBean",method = RequestMethod.POST)
    public void addBean() {
        SpringDataSolrDemo springDataSolrDemo = new SpringDataSolrDemo();
        springDataSolrDemo.setId(UUID.randomUUID().toString());
        springDataSolrDemo.setBatchNo("012454545");
        springDataSolrDemo.setCreaterName("吴宋体");
        WyBusinessContext ctx = WyBusinessContext.getContext();
        ctx.setCompanyId("09841dc0-204a-41f2-a175-20a6dcee0187");
        RemoteModelResult<UpdateResponse> responseRemoteModelResult = springDataSolrDemoApi.addBean(ctx.getCompanyId(),springDataSolrDemo);
        if(responseRemoteModelResult.isSuccess()){
            System.out.println("testAddBean成功==="+responseRemoteModelResult.getModel());
        }else{
            System.out.println("testAddBean失败==="+responseRemoteModelResult.getMsg());
        }
    }


    /**
     * solr分页查询demo
     * @param springDataSolrDemoSearch
     * @return
     */
    @RequestMapping(value = "/listPageDemo",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageDemo(@RequestBody SpringDataSolrDemoSearch springDataSolrDemoSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<BaseDto> remoteModelResult = springDataSolrDemoApi.listPageDemo(ctx.getCompanyId(),springDataSolrDemoSearch);
        if(remoteModelResult.isSuccess()){
            baseDto = remoteModelResult.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(remoteModelResult.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 如何获取组织架构，如当前登录用户的岗位、项目、部门等
     */
    @RequestMapping(value = "/getOrganization/{type}",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto getOrganization(@PathVariable String type){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        JSONArray jsonArray = new JSONArray();

        if("department".equals(type)){
            jsonArray = ctx.getOrganizationByType(WyBusinessContext.BusinessContextEnum.DEPARTMENT);
        }else if("project".equals(type)){
            jsonArray = ctx.getOrganizationByType(WyBusinessContext.BusinessContextEnum.PROJECT);
        }else if("role".equals(type)){
            jsonArray = ctx.getOrganizationByType(WyBusinessContext.BusinessContextEnum.ROLE);
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("无效的类型");
        }

        baseDto.setMessageMap(mm);
        baseDto.setT(jsonArray);
        return baseDto;
    }
}
