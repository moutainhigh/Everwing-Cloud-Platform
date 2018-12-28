package com.everwing.server.wy.api.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.platform.entity.generated.Account;
import com.everwing.coreservice.common.utils.WyAppPushUtils;
import com.everwing.coreservice.common.wy.common.enums.TcOrderStatusEnum;
import com.everwing.coreservice.common.wy.entity.order.TcOrder;
import com.everwing.coreservice.common.wy.entity.system.user.TSysUser;
import com.everwing.coreservice.wy.api.order.TcOrderApi;
import com.everwing.coreservice.wy.api.sys.TSysUserApi;
import com.everwing.server.wy.api.vo.BaseApiVo;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 工单接口
 * Created by zhugeruifei on 17/10/13.
 */
@RestController
@RequestMapping("{version}/workOrder")
public class WorkOrderController {

    private static final Logger logger= LogManager.getLogger(WorkOrderController.class);

    @Autowired
    private TcOrderApi tcOrderApi;

    @Autowired
    private TSysUserApi tSysUserApi;

    @Autowired
    private WyAppPushUtils wyAppPushUtils;


    /**
     * 查询我的工单
     * @param request
     * @param description
     * @param date
     * @param searchType
     * @param pageSize
     * @param pageNo
     * @return
     */
    @PostMapping("myWorkOrder")
    @ApiVersion(1.0)
    public BaseApiVo myWorkOrder(HttpServletRequest request,String description,String date, String searchType ,String pageSize,String pageNo) {
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String accountId = account.getAccountId();
        String companyId = account.getCompanyId();
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        if (searchType.equals(SearchType.DATE.getSearchCode())) description = null;
        if (searchType.equals(SearchType.DESCRIPTION.getSearchCode())) date = null;
        logger.debug("查询我的工单，" +
                        "请求参数:accountId={},companyId={},date={},searchType={}，pageSize={}，pageNo={},description={},date={}",
                new Object[]{accountId,companyId,date,searchType,pageSize,pageNo,description,date});

        RemoteModelResult<List> remoteModelResult = tcOrderApi.queryMyWorkOrder(companyId,accountId,description,date,
                Integer.parseInt(pageSize),Integer.parseInt(pageNo));
        logger.debug("查询我的工单,远程调用返回数据：{}",remoteModelResult);

        if (remoteModelResult.isSuccess()){
            returnApiVo.setData(remoteModelResult.getModel());
        }else {
            logger.debug("查询我的工单数据,查询失败!");

        }
        returnApiVo.setCode(remoteModelResult.getCode());
        returnApiVo.setMessage(remoteModelResult.getMsg());
        logger.debug("查询我的工单数据，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    /**
     * 提交处理的工单
     * @param request
     * @param orderCode
     * @param status
     * @param processDate
     * @return
     */
    @PostMapping("submitWorkOrder")
    @ApiVersion(1.0)
    public BaseApiVo submitWorkOrder(HttpServletRequest request,String orderCode,String status,String processDate,String procesInstructions) {
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String accountId = account.getAccountId();
        String companyId = account.getCompanyId();
        BaseApiVo returnApiVo  = new BaseApiVo();

        if(StringUtils.isBlank(companyId)) {
            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            returnApiVo.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
            returnApiVo.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
            return returnApiVo;
        }

        logger.debug("提交已经处理的工单，" +
                        "请求参数:companyId={},orderCode={},status={},processDate={}，",
                new Object[]{accountId,companyId,orderCode,status,processDate});
        RemoteModelResult<Integer> remoteModelResult = tcOrderApi.updateProcessedWorkOrder(companyId,orderCode,status,processDate,procesInstructions,accountId);
        logger.debug("远程调用返回数据：{}",remoteModelResult);
        if (remoteModelResult.isSuccess()){
            returnApiVo.setData(remoteModelResult.getModel());
        }else {
            logger.debug("提交已经处理的工单,更新失败!");

        }
        returnApiVo.setCode(remoteModelResult.getCode());
        returnApiVo.setMessage(remoteModelResult.getMsg());
        logger.debug("提交已经处理的工单接口，返回给前端：{}",returnApiVo);
        return returnApiVo;
    }

    @PostMapping("getAllPerson")
    @ApiVersion(1.0)
    public BaseApiVo getAllPerson(HttpServletRequest request){
        logger.debug("加载公司下的所有员工开始!");
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        String companyId = account.getCompanyId();
        BaseApiVo returnApiVo  = new BaseApiVo();
        RemoteModelResult<List<Map<String,String>>> remoteModelResult=tSysUserApi.listByCompanyId(companyId);
        if (remoteModelResult.isSuccess()){
            returnApiVo.setData(remoteModelResult.getModel());
            logger.debug("加载公司下的所有员工成功!");
        }else {
            logger.debug("加载公司下的所有员工失败!");
        }
        returnApiVo.setCode(remoteModelResult.getCode());
        returnApiVo.setMessage(remoteModelResult.getMsg());
        return returnApiVo;
    }

    @PostMapping("upDatePerson")
    @ApiVersion(1.0)
    public BaseApiVo upDataPerson(HttpServletRequest request,String orderId,String personId){
        logger.debug("修改工单负责人开始!");
        Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
        RemoteModelResult<TcOrder> result=tcOrderApi.queryOrderStatusById(account.getCompanyId(),orderId);
        if(result.isSuccess()){
            TcOrder order=result.getModel();
            if(order==null){
                logger.debug("根据工单id未查询到工单信息！");
                return new BaseApiVo(ReturnCode.API_RESOLVE_FAIL.getCode(),"未查询到工单信息!",order);
            }else {
                if(order.getStatus()==TcOrderStatusEnum.ORDER_STATUS_DONE.getValue()){
                    return new BaseApiVo(ReturnCode.API_RESOLVE_FAIL.getCode(),"工单已完成,不可修改!",null);
                }else {
                    BaseApiVo returnApiVo  = new BaseApiVo();
                    TcOrder tcOrder=new TcOrder();
                    tcOrder.setOrderCode(orderId);
                    tcOrder.setPrincipalSysUserId(personId);
                    tcOrder.setUpdateBy(account.getAccountId());
                    RemoteModelResult remoteModelResult=tcOrderApi.updateOrder(account.getCompanyId(),tcOrder);
                    if (remoteModelResult.isSuccess()){
                        TSysUser sysUser=tSysUserApi.findByPrimaryKey(personId).getModel();
                        logger.info("修改工单负责人成功，调用wyApp推送服务!");
                        //wyAppPushUtils.pushToOrderResponsiblePerson(sysUser.getMobileTelephone());
                        returnApiVo.setData(remoteModelResult.getModel());
                    }else {
                        logger.debug("修改工单失败!");
                    }
                    returnApiVo.setCode(remoteModelResult.getCode());
                    returnApiVo.setMessage(remoteModelResult.getMsg());
                    return returnApiVo;
                }
            }
        }else {
            logger.debug("修改工单失败!{}",result.getMsg());
            return new BaseApiVo(ReturnCode.API_RESOLVE_FAIL.getCode(),ReturnCode.API_RESOLVE_FAIL.getDescription(),null);
        }
    }

    enum SearchType {

        DATE("1"),
        DESCRIPTION("0");

        private String searchCode;

        private SearchType(String searchCode) {
            this.searchCode = searchCode;
        }

        public String getSearchCode() {
            return searchCode;
        }

        public void setSearchCode(String searchCode) {
            this.searchCode = searchCode;
        }
    }

}
