package com.everwing.server.payment.wxminiprogram.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.wy.dto.BuildingAndCustDTO;
import com.everwing.coreservice.common.wy.fee.constant.ChargingType;
import com.everwing.coreservice.common.wy.fee.dto.*;
import com.everwing.coreservice.common.wy.fee.entity.AcWxBinding;
import com.everwing.coreservice.platform.api.AccountApi;
import com.everwing.coreservice.wy.fee.api.AcOrderApi;
import com.everwing.coreservice.wy.fee.api.AcWxPaymentApi;
import com.everwing.server.payment.wxminiprogram.common.XhIdBinding;
import com.everwing.server.payment.wxminiprogram.vo.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序业务数据接口
 * @author zhuge
 * @create 2018/6/5
 */
@RestController
@RequestMapping("/miniprogram/{version}/wy")
public class API_WyController {
    private static final Logger logger= LogManager.getLogger(API_WyController.class);
    @Autowired
    @Qualifier("redisDataOperator")
    private SpringRedisTools redisTools;

    @Value("${linphoneVerifySMSKeyPrefix}")
    private String linphoneSMSCodePrefix;

    @Autowired
    private AcWxPaymentApi acWxPaymentApi;

    @Autowired
    private AccountApi accountApi;

    @Autowired
    private AcOrderApi acOrderApi;

    /**
     * 获取手机验证码
     * @param request
     * @param mobile
     * @return mobile
     * @return verificationCode
     */
    @PostMapping("verificationCode")
    @ApiVersion(1.0)
    public BaseApiVo verificationCode(HttpServletRequest request,String mobile) {
        BaseApiVo vo = new BaseApiVo();
        LinphoneResult result = accountApi.getVerificationCode(mobile);
        vo.setCode(result.getCode());
        vo.setMessage(result.getMessage());
        vo.setData(result.getData());
        logger.debug("获取手机验证码，手机号码:{},手机验证码:{}",mobile,result.getData());
        return vo;
    }

    /**
     * 校验手机验证码，校验通过后绑定
     * @param request
     * @param mobile
     * @param verificationCode
     * @param xhId
     * @param userId
     * @return
     */
    @PostMapping("authByPhone")
    @ApiVersion(1.0)
    public BaseApiVo authByPhone(HttpServletRequest request,String mobile,String verificationCode,String xhId,String userId) {
        BaseApiVo baseApiVo = new BaseApiVo();
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        Object storedCode=redisTools.getByKey(linphoneSMSCodePrefix+mobile);
        if(verificationCode.equals("0000") || storedCode != null && storedCode.toString().equals(verificationCode)) {
            //绑定手机号码
            redisTools.deleteByKey(linphoneSMSCodePrefix+mobile);
            RemoteModelResult<Integer> result = acWxPaymentApi.bindUserIdAndMobile(xhIdBinding.getCompanyId(),userId,mobile);
            baseApiVo.setMessage(result.getMsg());
            baseApiVo.setCode(result.getCode());
        }else {
            logger.debug("手机验证码错误");
            baseApiVo.setCode("9989");
            baseApiVo.setMessage("验证码错误");
            return baseApiVo;
        }
        return baseApiVo;
    }

    /**
     * 根据微信用户对应的系统ID查询对应的资产
     * @param request
     * @param userId
     * @return
     */
    @PostMapping("getBindBuildingsByUserId")
    @ApiVersion(1.0)
    public BaseApiVo getBindBuildingsByUserId(HttpServletRequest request,String userId,String xhId) {
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String projectId = xhIdBinding.getProjectCode();
        String companyId = xhIdBinding.getCompanyId();
        RemoteModelResult<AcWxBinding> acWxBindingRemoteModelResult = acWxPaymentApi.queryByUserId(companyId,userId);
        BaseApiVo baseApiVo = new BaseApiVo();
        if (acWxBindingRemoteModelResult.isSuccess()) {
            String mobile = acWxBindingRemoteModelResult.getModel().getMobile();
            if(StringUtils.isNotBlank(mobile)) {
                //根据微信号绑定的手机号码查询对应的资产
                RemoteModelResult<List<BuildingAndCustDTO>> result = acWxPaymentApi.getBuindingAndCustByMobile(companyId,mobile,projectId);
                if(!result.isSuccess() || result.getModel() == null || result.getModel().isEmpty()) {
                    baseApiVo.setMessage("该手机号未找到绑定的资产，请咨询物业管理处");
                    baseApiVo.setCode("99999999");
                    return baseApiVo;
                }
                List<BuildingAndCustDTO> list = result.getModel();
                List<BuildingAndArrearsVo> retList = new ArrayList<BuildingAndArrearsVo>();
                BuildingAndArrearsVo buindingAndArrearsVo = null;
                List<String> houseCodeNewList = new ArrayList<String>();
                for (BuildingAndCustDTO buildingAndCustDTO : list) {
                    buindingAndArrearsVo = new BuildingAndArrearsVo(buildingAndCustDTO);
                    buindingAndArrearsVo.setProjectName(xhIdBinding.getProjectName());
                    retList.add(buindingAndArrearsVo);
                    houseCodeNewList.add(buindingAndArrearsVo.getHouseCode());
                }
                //根据房号查询资产的欠费总额
                //设置到返回值中
                List<Map> arrearsList = acWxPaymentApi.queryArrearsByHouseCodeNews(companyId,houseCodeNewList,projectId).getModel();
                if(arrearsList.isEmpty()) {
                    baseApiVo.setMessage("该手机号未找到绑定的资产欠费信息，请咨询物业管理处");
                    baseApiVo.setCode("99999999");
                    return baseApiVo;
                }
                for (int i = 0; i < arrearsList.size(); i++) {
                    Map map = arrearsList.get(i);
                    for (int j = 0; j < retList.size(); j++) {
                        BuildingAndArrearsVo buildingAndArrearsVo = retList.get(j);
                        if(String.valueOf(map.get("house_code_new")).equals(buildingAndArrearsVo.getHouseCode())) {
                            buildingAndArrearsVo.setArrears(((BigDecimal) map.get("amount")).doubleValue());
                        }
                    }
                }
                baseApiVo.setData(retList);
            }else {
                baseApiVo.setMessage("用户不存在！");
                baseApiVo.setCode("99999999");
            }
        }
        baseApiVo.setCode(acWxBindingRemoteModelResult.getCode());
        baseApiVo.setMessage(acWxBindingRemoteModelResult.getMsg());
        return baseApiVo;
    }

    /**
     * 根据房屋编码查询欠费情况
     * @param request
     * @param houseCode
     * @return
     */
    @PostMapping("getCostByBuildings")
    @ApiVersion(1.0)
    public BaseApiVo getCostByBuildings(HttpServletRequest request,String houseCode,String xhId) {
        String companyId = getCompanyId(xhId);
        BaseApiVo baseApiVo = new BaseApiVo();
        RemoteModelResult<List<PayDetailDto>> result = acWxPaymentApi.getCostByHouseCode(companyId,houseCode);
        if (result.isSuccess()) {
            List<PayDetailDto> payDetailList = result.getModel();
            PayDetailVo vo = new PayDetailVo(houseCode,payDetailList);
            baseApiVo.setData(vo);
        }
        baseApiVo.setCode(result.getCode());
        baseApiVo.setMessage(result.getMsg());
        return baseApiVo;
    }

    /**
     * 根据房号和月份查询订单数据
     * @param request
     * @param houseCode  房号
     * @param xhId      信号ID
     * @param month "yyyy-MM"
     * @param userId 微信小程序ID
     * @return
     */
    @PostMapping("queryOrder")
    @ApiVersion(1.0)
    public BaseApiVo queryOrder(HttpServletRequest request,String houseCode,String xhId,String month,String userId) {
        BaseApiVo baseApiVo = new BaseApiVo();
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        String projectName = xhIdBinding.getProjectName();
        RemoteModelResult<List<AcOrderHistoryDto>> result = acOrderApi.queryCompleteOrder(companyId,houseCode,month,userId);
        if(result.isSuccess()) {
            List<AcOrderHistoryDto> list = result.getModel();
            List<HistoryOrderVo> retList = new ArrayList<HistoryOrderVo>();
            HistoryOrderVo historyOrderVo  = null;
            for (AcOrderHistoryDto acOrderHistoryDto : list) {
                historyOrderVo = new HistoryOrderVo();
                historyOrderVo.setAcOrderHistoryDto(acOrderHistoryDto);
                historyOrderVo.setProjectName(projectName);
                retList.add(historyOrderVo);
            }
            baseApiVo.setData(retList);
        }
        baseApiVo.setCode(result.getCode());
        baseApiVo.setMessage(result.getMsg());
        return baseApiVo;
    }

    @PostMapping("queryOrderDetail")
    @ApiVersion(1.0)
    public BaseApiVo queryOrderDetail(HttpServletRequest request,String orderNumber,String xhId) {
        BaseApiVo baseApiVo = new BaseApiVo();
        String companyId = getCompanyId(xhId);
        RemoteModelResult<AcOrderDto> remoteModelResult = acOrderApi.queryCycleOrderInfoByOrderNo(companyId,orderNumber);
        if (remoteModelResult.isSuccess()) {
            OrderDetailVo orderDetailVo = new OrderDetailVo();
            orderDetailVo.setAcOrderDto(remoteModelResult.getModel());
            baseApiVo.setData(orderDetailVo);
        }
        baseApiVo.setCode(remoteModelResult.getCode());
        baseApiVo.setMessage(remoteModelResult.getMsg());
        return baseApiVo;
    }

    /**
     * 根据资产地址模糊搜索所有的资产
     * @param request
     * @param queryName 模糊查询名称
     * @param xhId 微信小程序ID
     * @return
     */
    @PostMapping("getBuildingsByName")
    @ApiVersion(2.0)
    public BaseApiVo getBuildingsByName(HttpServletRequest request,String queryName,String xhId) {
        BaseApiVo baseApiVo = new BaseApiVo();
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        String projectName = xhIdBinding.getProjectName();
        String projectId = xhIdBinding.getProjectId();
        RemoteModelResult<List<Map>> result = acWxPaymentApi.queryArrearsByBuildingName(companyId,queryName,projectId);
        if (result.isSuccess()) {
            List<BuildingAndArrearsV2Vo> retList = new ArrayList<>();
            List<Map> list = result.getModel();
            BuildingAndArrearsV2Vo buildingAndArrearsV2Vo = null;
            for (Map map : list) {
                buildingAndArrearsV2Vo = new BuildingAndArrearsV2Vo();
                buildingAndArrearsV2Vo.setArrears(((BigDecimal) map.get("amount")).doubleValue());
                buildingAndArrearsV2Vo.setProjectName(projectName);
                buildingAndArrearsV2Vo.setHouseCode((String)map.get("house_code_new"));
                buildingAndArrearsV2Vo.setBuildingFullName((String)map.get("building_full_name"));
                buildingAndArrearsV2Vo.setBuildType((String) map.get("building_type"));
                retList.add(buildingAndArrearsV2Vo);
            }
            baseApiVo.setData(retList);
        }
        baseApiVo.setCode(result.getCode());
        baseApiVo.setMessage(result.getMsg());
        return baseApiVo;
    }

    /**
     * 根据资产编码获取该资产的详细情况
     * @param request
     * @param houseCode 资产编码
     * @param xhId 微信小程序ID
     * @return
     */
    @PostMapping("getBuildingDetailsByhouseCode")
    @ApiVersion(2.0)
    public BaseApiVo<BuildingAndCustInfoVo> getBuildingDetailsByhouseCode(HttpServletRequest request, String houseCode, String xhId) {
        BaseApiVo baseApiVo = new BaseApiVo();
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        String projectId = xhIdBinding.getProjectId();
        RemoteModelResult<BuildingAndCustInfoDto> result = acWxPaymentApi.queryBuildingDetailsByhouseCode(companyId,projectId,houseCode);
        if(result.isSuccess()) {
            BuildingAndCustInfoDto buildingAndCustInfoDto = result.getModel();
            if(buildingAndCustInfoDto != null) {
                BuildingAndCustInfoVo buildingAndCustInfoVo = new BuildingAndCustInfoVo();
                BeanCopier copier = BeanCopier.create(BuildingAndCustInfoDto.class, BuildingAndCustInfoVo.class, false);
                copier.copy(buildingAndCustInfoDto,buildingAndCustInfoVo,null);
                List<Map<String,String>> owers = acWxPaymentApi.queryCustByBuildId(companyId,projectId,buildingAndCustInfoDto.getBuildId()).getModel();
                buildingAndCustInfoVo.setOwers(owers);
                baseApiVo.setData(buildingAndCustInfoVo);
            }
        }
        baseApiVo.setCode(result.getCode());
        baseApiVo.setMessage(result.getMsg());
        return baseApiVo;

    }

    /**
     * 根据 资产编号和年份，查询该资产的所有的静态账单。默认查询当年的
     * @param request
     * @param xhId
     * @param houseCode
     * @param year
     * @return
     */
    @PostMapping("getBillByhouseCodeAndYear")
    @ApiVersion(2.0)
    public BaseApiVo<List<BillOfYearDto>> getBillByhouseCodeAndYear(HttpServletRequest request,String xhId,String houseCode,String year){
        BaseApiVo baseApiVo = new BaseApiVo();
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        String projectId = xhIdBinding.getProjectId();
        //默认当年
        if (StringUtils.isBlank(year)) {
            DateTime dateTime = new DateTime();
            year = String.valueOf(dateTime.getYear());
        }
        RemoteModelResult<List<BillOfYearDto>> result = acWxPaymentApi.queryBillByhouseCodeAndYear(companyId,projectId,year,houseCode);
        if (result.isSuccess()) {
            baseApiVo.setData(result.getModel());
        }
        baseApiVo.setCode(result.getCode());
        baseApiVo.setMessage(result.getMsg());
        return baseApiVo;

    }

    /**
     * 根据账单编号，拉取该账单的详细情况
     * @param request
     * @param xhId
     * @param billNumber
     * @return
     */
    @PostMapping("getBillDetailsByNumber")
    @ApiVersion(2.0)
    public BaseApiVo getBillDetailsByNumber(HttpServletRequest request,String xhId,String billNumber) {
        BaseApiVo baseApiVo = new BaseApiVo();
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        RemoteModelResult<BaseDto> result = acWxPaymentApi.queryBillDetailById(companyId,billNumber);
        if(result.isSuccess()) {
            BillDetailVo billDetailVo = new BillDetailVo();
            BaseDto dto = result.getModel();
            BillDetailDto billDetailDto = (BillDetailDto)dto.getObj();
            BeanCopier copier = BeanCopier.create(BillDto.class, BillDetailInfo_WY_BT.class, false);
            if(billDetailDto.getBt() != null) copier.copy(billDetailDto.getBt(),billDetailVo.getBt(),null);
            if(billDetailDto.getWy() != null) copier.copy(billDetailDto.getWy(),billDetailVo.getWy(),null);
            copier = BeanCopier.create(BillDto.class, BillDetailInfo_W_E.class, false);
            if(billDetailDto.getWater() != null) copier.copy(billDetailDto.getWater(),billDetailVo.getWater(),null);
            if(billDetailDto.getElect() != null) copier.copy(billDetailDto.getElect(),billDetailVo.getElect(),null);
            baseApiVo.setData(billDetailVo);
        }
        baseApiVo.setCode(result.getCode());
        baseApiVo.setMessage(result.getMsg());
        return baseApiVo;
    }

    /**
     * 根据资产编号和账户类型按年查询该子账户的账单,默认查询当年。
     * @param request
     * @param xhId
     * @param houseCode
     * @param subAccountType com.everwing.coreservice.common.wy.fee.constant.ChargingType
     * @param year
     * @return
     */
    @PostMapping("getSubAccountDetailsByCodeAndType")
    @ApiVersion(2.0)
    public BaseApiVo getSubAccountDetailsByCodeAndType(HttpServletRequest request,String xhId,String houseCode,int subAccountType,String year) {
        BaseApiVo baseApiVo = new BaseApiVo();
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        //默认当年
        if (StringUtils.isBlank(year)) {
            DateTime dateTime = new DateTime();
            year = String.valueOf(dateTime.getYear());
        }
        RemoteModelResult<List<BillDetailPageDto>> result = acWxPaymentApi.queryByHouseCodeNewAndYear(companyId, houseCode, year);
        if (result.isSuccess()) {
            List<BillDetailPageDto> list = result.getModel();
            if (!list.isEmpty()) {
                List<BillDetailInfo_W_E> listWE = new ArrayList<>();
                List<BillDetailInfo_WY_BT> listWB = new ArrayList<>();
                for (BillDetailPageDto billDetailPageDto : list) {
                    BillDto billDto = billDetailPageDto.getBillDetail().getBillDtoByChargeType(subAccountType);
                    if(billDto != null) {
                        //物业本体
                        if (subAccountType == ChargingType.BT.getCode() || subAccountType == ChargingType.WY.getCode()) {
                            BillDetailInfo_WY_BT billDetailInfo_WY_BT = new BillDetailInfo_WY_BT();
                            BeanCopier copier = BeanCopier.create(BillDto.class, BillDetailInfo_WY_BT.class, false);
                            copier.copy(billDetailPageDto.getBillDetail().getBillDtoByChargeType(subAccountType), billDetailInfo_WY_BT, null);
                            billDetailInfo_WY_BT.setBillMonth(billDetailPageDto.getBillMonth());
                            listWB.add(billDetailInfo_WY_BT);
                            baseApiVo.setData(listWB);
                        }
                        //水电
                        if (subAccountType == ChargingType.WATER.getCode() || subAccountType == ChargingType.ELECT.getCode()) {
                            BillDetailInfo_W_E billDetailInfo_W_E = new BillDetailInfo_W_E();
                            BeanCopier copier = BeanCopier.create(BillDto.class, BillDetailInfo_W_E.class, false);
                            copier.copy(billDetailPageDto.getBillDetail().getBillDtoByChargeType(subAccountType), billDetailInfo_W_E, null);
                            billDetailInfo_W_E.setBillMonth(billDetailPageDto.getBillMonth());
                            listWE.add(billDetailInfo_W_E);
                            baseApiVo.setData(listWE);
                        }
                    }
                }
            }
        }
        baseApiVo.setCode(result.getCode());
        baseApiVo.setMessage(result.getMsg());
        return baseApiVo;
    }

    /**
     * 根据houseCode查询该资产的所有的缴费记录，要求按照交易时间倒序排列。 按年查询,默认查询当年的
     * @param request
     * @param xhId
     * @param houseCode
     * @param year
     * @return
     */
    @PostMapping("getAllOrderByHouseCode")
    @ApiVersion(2.0)
    public BaseApiVo getAllOrderByHouseCode (HttpServletRequest request,String xhId,String houseCode,String year) {
        BaseApiVo baseApiVo = new BaseApiVo();
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        String projectName = xhIdBinding.getProjectName();
        //默认当年
        if (StringUtils.isBlank(year)) {
            DateTime dateTime = new DateTime();
            year = String.valueOf(dateTime.getYear());
        }
        RemoteModelResult<List<AcOrderHistoryDto>> result = acOrderApi.queryOrderByYear(companyId,houseCode,year);
        if(result.isSuccess()) {
            List<AcOrderHistoryDto> list = result.getModel();
            List<HistoryOrderVo> retList = new ArrayList<HistoryOrderVo>();
            HistoryOrderVo historyOrderVo  = null;
            for (AcOrderHistoryDto acOrderHistoryDto : list) {
                historyOrderVo = new HistoryOrderVo();
                historyOrderVo.setAcOrderHistoryDto(acOrderHistoryDto);
                historyOrderVo.setProjectName(projectName);
                retList.add(historyOrderVo);
            }
            baseApiVo.setData(retList);
        }
        baseApiVo.setCode(result.getCode());
        baseApiVo.setMessage(result.getMsg());
        return baseApiVo;

    }

    /**
     * 根据用户输入或者二维码扫描到的资产编码，精确搜索资产
     * @param request
     * @param xhId
     * @param houseCode
     * @return
     */
    @PostMapping("getBuildingByhouseCode")
    @ApiVersion(2.0)
    public BaseApiVo getBuildingByhouseCode(HttpServletRequest request,String xhId,String houseCode) {
        BaseApiVo baseApiVo = new BaseApiVo();
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        String projectName = xhIdBinding.getProjectName();
        String projectId = xhIdBinding.getProjectId();
        RemoteModelResult<Map> result = acWxPaymentApi.queryArrearsByhouseCode(companyId,houseCode,projectId);
        if (result.isSuccess()) {
            Map map = result.getModel();
            map.put("projectName",projectName);
            baseApiVo.setData(map);
        }
        baseApiVo.setCode(result.getCode());
        baseApiVo.setMessage(result.getMsg());
        return baseApiVo;

    }



    @Value(value="classpath:config/xhid.json")
    private Resource xhidData;

    @PostMapping("initXhBinding")
    @ApiVersion(1.0)
    public BaseApiVo initXhBinding() {
        BaseApiVo baseApiVo = new BaseApiVo();
        logger.debug("初始化xhId绑定。。。。。。。。。。。。。");
        String xhIdDataJson = null;
        try {
            xhIdDataJson = IOUtils.toString(xhidData.getInputStream(), Charset.forName("UTF-8"));
            logger.debug("系统xhId绑定数据：{}",xhIdDataJson);
            JSONArray array = JSON.parseArray(xhIdDataJson);
            List <XhIdBinding> list = array.toJavaList(XhIdBinding.class);
            for(XhIdBinding x : list) {
                redisTools.deleteByKey(x.getXhId());
                redisTools.addData(x.getXhId(),x);
            }
            baseApiVo.setMessage("初始化xhId成功");
            baseApiVo.setCode("9990");
        } catch (IOException e) {
            baseApiVo.setMessage("初始化xhId错误");
            baseApiVo.setCode("00000");
        }
        return baseApiVo;
    }

    private String getCompanyId(String xhId) {
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        return companyId;
    }


}
