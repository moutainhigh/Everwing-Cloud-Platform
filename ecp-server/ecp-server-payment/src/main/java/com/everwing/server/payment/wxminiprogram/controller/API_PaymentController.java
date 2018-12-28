package com.everwing.server.payment.wxminiprogram.controller;

import com.alibaba.fastjson.JSON;
import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.utils.HttpUtils;
import com.everwing.coreservice.common.utils.MD5Utils;
import com.everwing.coreservice.common.wy.entity.account.pay.TBsPayInfo;
import com.everwing.coreservice.common.wy.fee.constant.*;
import com.everwing.coreservice.common.wy.fee.dto.AcBusinessOperaDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderCycleDetailDto;
import com.everwing.coreservice.common.wy.fee.dto.AcOrderDto;
import com.everwing.coreservice.common.wy.fee.dto.PayDetailDto;
import com.everwing.coreservice.common.wy.fee.entity.AcWxBinding;
import com.everwing.coreservice.common.wy.fee.entity.PayDetailInfoForWeiXin;
import com.everwing.coreservice.wy.api.business.pay.TBsPayInfoApi;
import com.everwing.coreservice.wy.fee.api.AcAccountApi;
import com.everwing.coreservice.wy.fee.api.AcBusinessOperaDetailApi;
import com.everwing.coreservice.wy.fee.api.AcOrderApi;
import com.everwing.coreservice.wy.fee.api.AcWxPaymentApi;
import com.everwing.server.payment.wxminiprogram.common.*;
import com.everwing.server.payment.wxminiprogram.vo.BaseApiVo;
import com.everwing.server.payment.wxminiprogram.vo.WxLoginVo;
import com.everwing.utils.TokenGenUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 微信小程序支付相关接口
 * @author zhuge
 * @create 2018/6/5
 */
@RestController
@RequestMapping("/miniprogram/{version}")
public class API_PaymentController {

    private static final Logger logger= LogManager.getLogger(API_PaymentController.class);

    @Autowired
    @Qualifier("redisDataOperator")
    private SpringRedisTools redisTools;

    @Autowired
    private AcWxPaymentApi acWxPaymentApi;

    @Autowired
    private AcBusinessOperaDetailApi acBusinessOperaDetailApi;

    @Autowired
    private AcOrderApi acOrderApi;

    @Autowired
    private AcAccountApi acAccountApi;

    @Autowired
    private TBsPayInfoApi tBsPayInfoApi;

    private static String wyPaymentTokenKeyPrefix = "WyPaymentToken";


    private static int wyPaymentTokenExpireTime = 10;

    //微信小程序登录接口URL
    private static String WX_AUTH_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code";

    //统一下单接口URL
    private static String WX_UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";


    /**
     * 微信小程序登录
     * 保存小程序登录后的返回数据
     * 绑定系统唯一标识跟返回的OPENID
     * @param request
     * @param wxCode
     * @param xhId
     * @return 系统token
     * @return 系统UserId
     */
    @PostMapping("wxauthLogin")
    @ApiVersion(1.0)
    public BaseApiVo wxauthLogin(HttpServletRequest request, String wxCode, String xhId) {
//      https://api.weixin.qq.com/sns/jscode2session?appid=wx7663c8900a553155&secret=fc8452c892de5d65415dd9926eba9fb8&js_code=033ujRHS0WQu3Y1qBXGS0nSZHS0ujRH8&grant_type=authorization_code
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String loginUrl = WX_AUTH_LOGIN_URL +
                "&appid=" + xhIdBinding.getAppId() +
                "&secret=" + xhIdBinding.getAppSecret() +
                "&js_code=" + wxCode;
        String companyId = xhIdBinding.getCompanyId();
        logger.debug("WX_AUTH_LOGIN_URL: " + loginUrl);
        String wxloginRetJson = HttpUtils.doGet(loginUrl,null);
        logger.debug("wxlogin return: " + wxloginRetJson);
//      {"session_key":"vWtjTXbq+XwMFfzR4efiaw==","openid":"oqVXi5JdlV8ijZhHiqqVJhXEQQYc"}
        String openId = JSON.parseObject(wxloginRetJson).getString("openid");
        BaseApiVo BaseApiVo = new BaseApiVo();
        if(openId == null) {
            BaseApiVo.setCode("9990");
            BaseApiVo.setMessage("获取openId失败");
            return BaseApiVo;
        }
        //创建绑定信息
        RemoteModelResult<String> remoteModelResult = acWxPaymentApi.bindUseridAndOpenId(companyId,openId);
        if (remoteModelResult.isSuccess()) {
            String userId = remoteModelResult.getModel();
            //创建token,放入redis
            String token = TokenGenUtils.generateToken(userId);
            redisTools.addData(wyPaymentTokenKeyPrefix+token,userId,Integer.valueOf(wyPaymentTokenExpireTime), TimeUnit.MINUTES);
            WxLoginVo wxLoginVo = new WxLoginVo();
            wxLoginVo.setOpenId(openId);
            wxLoginVo.setUserId(userId);
            wxLoginVo.setToken(token);
            BaseApiVo.setData(wxLoginVo);
            BaseApiVo.setCode(remoteModelResult.getCode());
            BaseApiVo.setMessage(remoteModelResult.getMsg());
            logger.debug("绑定用户成功，userId:{},openId:{}",userId,openId);
        }else {
            BaseApiVo.setCode("9990");
            BaseApiVo.setMessage("绑定用户失败");
            logger.error("绑定用户失败!");
        }
        return BaseApiVo;

    }

    /**
     * 微信预下单接口
     * 生成订单以及订单明细
     * @param request
     * @return
     */
    @PostMapping("wxPerPay")
    @ApiVersion(1.0)
    public BaseApiVo wxPerPay(HttpServletRequest request,String xhId,String userId,String houseCode) {
        XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
        String companyId = xhIdBinding.getCompanyId();
        String projectId = xhIdBinding.getProjectCode();
        String projectName = xhIdBinding.getProjectName();

        //通过houseCode 查询 欠费总额(包含滞纳金)
        //复用之前查询多个的接口
        List<String> houseCodeNewList = new ArrayList<String>();
        houseCodeNewList.add(houseCode);
        List<Map> arrearsList = acWxPaymentApi.queryArrearsByHouseCodeNews(xhIdBinding.getCompanyId(),houseCodeNewList,projectId).getModel();
        BigDecimal arrears = (BigDecimal) arrearsList.get(0).get("amount");
        logger.debug("微信预下单，房号：{},微信用户userId：{},欠费总金额：{}",houseCode,userId,arrears);
        //插入业务资金明细表
        AcBusinessOperaDetailDto operaDetailDto = new AcBusinessOperaDetailDto();
        operaDetailDto.setAmount(arrears.doubleValue());
        operaDetailDto.setBusinessType(BusinessType.PAYMENT.getCode());
        operaDetailDto.setClientType(ClientType.MOBILE.getCode());
        operaDetailDto.setRemark("微信小程序缴费");
        operaDetailDto.setPersonType(PersonType.YZ.getCode());
        operaDetailDto.setProjectId(projectId);
        operaDetailDto.setProjectName(projectName);
        operaDetailDto.setOperationTime(new Date());
        operaDetailDto.setOperationId(userId);
        RemoteModelResult<String> result = acBusinessOperaDetailApi.createOperaDetail(companyId,operaDetailDto);
        BaseApiVo baseApiVo = new BaseApiVo();
        if (result.isSuccess()) {
            logger.debug("资金操作明细写入成功，创建订单，订单明细");
            //生成订单
            AcWxBinding acWxBinding = acWxPaymentApi.queryByUserId(companyId,userId).getModel();
            String payMobile = acWxBinding.getMobile();
            String openId = acWxBinding.getOpenId();
            String operaId = result.getModel();
            AcOrderDto orderDto = new AcOrderDto();
            orderDto.setAmount(arrears.doubleValue());
            orderDto.setOperaId(operaId);
            orderDto.setOrderState(AcOrderStateEnum.GENERATED.getState());
            orderDto.setOrderType(AcOrderTypeEnum.CYCLE.getType());
            orderDto.setPayerMobile(payMobile);
            orderDto.setPaymentCahnnel(PayChannel.WE_CHAT.getCode());
            orderDto.setPaymentTime(new Date());
            orderDto.setPayer(userId);
            orderDto.setPayState(AcPayStateEnum.UN_PAY.getState());
            orderDto.setHouseCodeNew(houseCode);
            orderDto.setIsRcorded(RcordedType.NOT_RCORDEDTYPE.getCode());
            orderDto.setSignature("a");
            //根据房屋编号查询欠费信息，生成订单明细
            RemoteModelResult<List<PayDetailDto>> payDetailResult = acWxPaymentApi.getCostByHouseCode(companyId,houseCode);
            List<PayDetailDto> payDetailDtoList = payDetailResult.getModel();
            List<AcOrderCycleDetailDto> orderCycleDetailList = new ArrayList<AcOrderCycleDetailDto>();
            AcOrderCycleDetailDto acOrderCycleDetailDto  = null;
            for (PayDetailDto payDetailDto : payDetailDtoList) {
                if(payDetailDto.getLateFee() != 0D || payDetailDto.getMoney() != 0D) {
                    acOrderCycleDetailDto = new AcOrderCycleDetailDto();
                    acOrderCycleDetailDto.setAccountType(payDetailDto.getAccountType());
                    acOrderCycleDetailDto.setDetailAmount(payDetailDto.getMoney());
                    acOrderCycleDetailDto.setBusinessType(BusinessType.PAYMENT.getCode());
                    acOrderCycleDetailDto.setHouseCodeNew(houseCode);
                    acOrderCycleDetailDto.setLateAmount(payDetailDto.getLateFee());
                    orderCycleDetailList.add(acOrderCycleDetailDto);
                }
            }
            orderDto.setOrderCycleDetailList(orderCycleDetailList);

            //事物,插入订单和订单明细
            RemoteModelResult<String> orderNoResult = acOrderApi.createCycleOrderInfo(companyId,orderDto,operaDetailDto);

            if (orderNoResult.isSuccess()) {
                String orderNo = orderNoResult.getModel();
                baseApiVo.setCode(orderNoResult.getCode());
                baseApiVo.setMessage(orderNoResult.getMsg());
                logger.debug("订单，订单明细创建成功，orderNo：{}",orderNo);
                //调用微信预下单接口
                RequestUnifiedorder requestUnifiedorder = buildRequestUnifiedorder(orderNo,houseCode,xhIdBinding,arrears,openId);
//                RequestUnifiedorder requestUnifiedorder = buildRequestUnifiedorder("sfsdf222gg",houseCode,xhIdBinding,arrears,openId);
                logger.debug("调用微信预下单接口，参数：{}",requestUnifiedorder.toString());
                String returnXML =  HttpUtils.doPost(WX_UNIFIEDORDER_URL,requestUnifiedorder.toXML());
                logger.debug("微信预下单接口返回：{}",returnXML);
                //返回值封装参数类
                ResultUnifiedorder resultUnifiedorder = ResultUnifiedorder.fromXML(returnXML);

                //校验参数
                if(checkSign(resultUnifiedorder)) {
                    //当ReturnCode和ResultCode同时为SUCCESS时返回perpayId
                    if(resultUnifiedorder.getReturnCode().equals("SUCCESS") &&
                            resultUnifiedorder.getResultCode().equals("SUCCESS")){
                        String perpayId = resultUnifiedorder.getPrepayId();
                        Map<String,String> data = new HashMap<String, String>();
                        data.put("timeStamp",String.valueOf(System.currentTimeMillis()/1000));
                        data.put("appId",xhIdBinding.getAppId());
                        data.put("package","prepay_id="+perpayId);
                        data.put("nonceStr",requestUnifiedorder.getNonceStr());
                        data.put("signType","MD5");
                        data.put("paySign",SignUtils.createSign(data,"MD5",xhIdBinding.getAppKey(),false));
                        data.put("orderNumber",orderNo);
                        baseApiVo.setData(data);
                        baseApiVo.setCode(orderNoResult.getCode());
                        baseApiVo.setMessage(orderNoResult.getMsg());
                    }else {
                        baseApiVo.setCode("9995");
                        baseApiVo.setMessage("获取perpayId失败");
                    }
                }else {
                    baseApiVo.setCode("9996");
                    baseApiVo.setMessage("参数签名错误");
                }

            }else {
                baseApiVo.setCode("9998");
                baseApiVo.setMessage("订单创建失败");
            }
        }else {
            baseApiVo.setCode("9997");
            baseApiVo.setMessage("操作明细写入失败");
        }
        return baseApiVo;
    }

    /**
     * 接收微信支付结果
     * 更新订单状态
     * 调用资产接口
     * @param request
     * @return
     */
    @PostMapping("wxPaymentCallBack")
    @ApiVersion(1.0)
    public void wxReceivePayResult(HttpServletRequest request, HttpServletResponse response) {
        BaseApiVo baseApiVo = new BaseApiVo();
        try {
            synchronized (this){
                Map<String, String> kvm = XMLUtil.parseRequestXmlToMap(request);
                logger.debug("微信支付回调返回数据：",kvm);
                String xhId = kvm.get("attach");
                XhIdBinding xhIdBinding = (XhIdBinding) redisTools.getByKey(xhId);
                if (xhIdBinding == null) {
                    baseApiVo.setMessage("系统配置丢失");
                    baseApiVo.setCode("9991");
                }
                //校验签名
                if (SignUtils.checkSign(kvm, "MD5",xhIdBinding.getAppKey())) {
                    if (kvm.get("result_code").equals("SUCCESS")) {
                        String orderNo = kvm.get("out_trade_no");
                        int totalFee = Integer.valueOf(kvm.get("total_fee"));
                        //微信支付交易流水，回写到订单
                        String transactionId = kvm.get("transaction_id");
                        //查询周期性订单。。。
                        AcOrderDto acOrderDto = acOrderApi.queryCycleOrderInfoByOrderNo(xhIdBinding.getCompanyId(),orderNo).getModel();
                        if (acOrderDto == null ) {
                            logger.error("微信支付回调订单不存在");
                            //回写微信支付服务器订单数据异常
                            responseFail(response,"Order not find");
                        }
                        logger.info("微信回调订单数据：{}",acOrderDto);
                        //订单金额,支付状态，订单状态，入账状态校验
                        if(acOrderDto.getPayState() == AcPayStateEnum.UN_PAY.getState() &&
                                totalFee == BigDecimal.valueOf(acOrderDto.getAmount()).multiply(new BigDecimal(100)).intValue() &&
                                acOrderDto.getIsRcorded() == RcordedType.NOT_RCORDEDTYPE.getCode()) {
                            //更新订单状态
                            acOrderApi.updateOrderPayState(xhIdBinding.getCompanyId(),orderNo,AcPayStateEnum.PAYED.getState(),AcOrderStateEnum.WAITING_RCORDED.getState(),transactionId);
                            //资产账户入账
                            PayDetailInfoForWeiXin payDetailInfoForWeiXin= bulidingPayDetailInfoForWeiXin(xhIdBinding,acOrderDto);
                            logger.debug("资产账户入账数据：payDetailInfoForWeiXin={}",payDetailInfoForWeiXin);
                            RemoteModelResult<Boolean> result = acAccountApi.payToNewAccountForWeiXin(xhIdBinding.getCompanyId(),payDetailInfoForWeiXin);
                            if (result.isSuccess()) {
                                if (result.getModel()) {
                                    //入账成功更新订单入账状态
                                    acOrderApi.updateOrderRcorded(xhIdBinding.getCompanyId(),orderNo,RcordedType.IS_RCORDEDTYPE.getCode());
                                    logger.info("资产账户入账成功。");
                                    //更改老账户数据
                                    TBsPayInfo tBsPayInfo = bulidingTBsPayInfo(xhIdBinding,acOrderDto);
                                    logger.debug("老账户入账数据：tBsPayInfo={}",tBsPayInfo);
                                    tBsPayInfoApi.pay2AccountForWeiXin(xhIdBinding.getCompanyId(),tBsPayInfo,"Y");
                                }else {
                                    logger.error("资产账户入账失败,入账数据：{}",payDetailInfoForWeiXin.toString());
                                }
                            }
                            //只要订单状态成功便回写微信成功，跟入账状态无关
                            responseSuccess(response);
                        }else {
                            logger.error("微信支付回调订单状态异常,订单数据：{}",acOrderDto.toString());
                            //回写微信支付服务器订单数据异常
                            responseFail(response,"Order Exception");
                        }
                    }else{
                        logger.error("微信支付回调签名校验失败");
                        //回写微信支付服务器签名校验失败
                        responseFail(response,"check signature FAIL ");
                    }
                }else {
                    logger.error("微信支付回调返回码错误");
                    //回写微信支付服务器支付返回码错误
                    responseFail(response,"result_code is FAIL");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建微信预下单数据
     * @param orderNo
     * @param houseCode
     * @param xhIdBinding
     * @param totalFee
     * @param openId
     * @return
     */
    private RequestUnifiedorder buildRequestUnifiedorder(String orderNo,String houseCode, XhIdBinding xhIdBinding, BigDecimal totalFee,String openId){
        RequestUnifiedorder requestUnifiedorder = new RequestUnifiedorder();
        requestUnifiedorder.setAppid(xhIdBinding.getAppId());
        requestUnifiedorder.setMchId(xhIdBinding.getMerchantNo());
        Random random = new Random();
        long val = random.nextLong();
        requestUnifiedorder.setNonceStr(MD5Utils.MD5((val + "zgrf")).toUpperCase());
        requestUnifiedorder.setBody("资产编号: "+houseCode +" -物业缴费");
        requestUnifiedorder.setOutTradeNo(orderNo);
        requestUnifiedorder.setSign_type("MD5");
//        requestUnifiedorder.setTotalFee(1);
        requestUnifiedorder.setTotalFee(totalFee.multiply(new BigDecimal(100)).intValue());
        requestUnifiedorder.setTradeType("JSAPI");
        requestUnifiedorder.setSpbillCreateIp("10.0.0.22");
        requestUnifiedorder.setNotifyUrl(xhIdBinding.getNotifyUrl());
        requestUnifiedorder.setAttach(xhIdBinding.getXhId());
        requestUnifiedorder.setOpenId(openId);
        requestUnifiedorder.setSign(SignUtils.createSign(requestUnifiedorder,"MD5",xhIdBinding.getAppKey(),false));
        return requestUnifiedorder;
    }

    /**
     * 构建新资产账户写入数据
     * @return
     */
    private PayDetailInfoForWeiXin bulidingPayDetailInfoForWeiXin(XhIdBinding xhIdBinding,AcOrderDto acOrderDto) {
        List<AcOrderCycleDetailDto> acOrderCycleDetailDtoList = acOrderDto.getOrderCycleDetailList();
        PayDetailInfoForWeiXin payDetailInfoForWeiXin = new PayDetailInfoForWeiXin();
        payDetailInfoForWeiXin.setProjectId(xhIdBinding.getProjectCode());
        payDetailInfoForWeiXin.setCompanyId(xhIdBinding.getCompanyId());
        payDetailInfoForWeiXin.setHouseCodeNew(acOrderDto.getHouseCodeNew());
        payDetailInfoForWeiXin.setProjectName(xhIdBinding.getProjectName());
        payDetailInfoForWeiXin.setOperaId(acOrderDto.getOperaId());
        for (AcOrderCycleDetailDto acOrderCycleDetailDto : acOrderCycleDetailDtoList) {
            int chargingType = acOrderCycleDetailDto.getAccountType();
            if (chargingType == ChargingType.WY.getCode()) {
                payDetailInfoForWeiXin.setWyAmount(BigDecimal.valueOf(acOrderCycleDetailDto.getDetailAmount()));
                payDetailInfoForWeiXin.setWyLateFeeAmount(BigDecimal.valueOf(acOrderCycleDetailDto.getLateAmount()));
                continue;
            }
            if (chargingType == ChargingType.BT.getCode()) {
                payDetailInfoForWeiXin.setBtAmount(BigDecimal.valueOf(acOrderCycleDetailDto.getDetailAmount()));
                payDetailInfoForWeiXin.setBtLateFeeAmount(BigDecimal.valueOf(acOrderCycleDetailDto.getLateAmount()));
                continue;
            }
            if (chargingType == ChargingType.WATER.getCode()) {
                payDetailInfoForWeiXin.setWaterAmount(BigDecimal.valueOf(acOrderCycleDetailDto.getDetailAmount()));
                payDetailInfoForWeiXin.setWaterLateFeeAmount(BigDecimal.valueOf(acOrderCycleDetailDto.getLateAmount()));
                continue;
            }
            if (chargingType == ChargingType.ELECT.getCode()) {
                payDetailInfoForWeiXin.setElectAmount(BigDecimal.valueOf(acOrderCycleDetailDto.getDetailAmount()));
                payDetailInfoForWeiXin.setElectLateFeeAmount(BigDecimal.valueOf(acOrderCycleDetailDto.getLateAmount()));
                continue;
            }
        }
        return payDetailInfoForWeiXin;
    }

    /**
     * 构建老账户缴费数据
     * @param xhIdBinding
     * @param acOrderDto
     * @return
     */
    private TBsPayInfo bulidingTBsPayInfo(XhIdBinding xhIdBinding,AcOrderDto acOrderDto) {
        TBsPayInfo tBsPayInfo = new TBsPayInfo();
        List<String> buildingCodes = new ArrayList<>();
        List<AcOrderCycleDetailDto> orderCycleDetailList = acOrderDto.getOrderCycleDetailList();
        for (AcOrderCycleDetailDto acOrderCycleDetailDto : orderCycleDetailList) {
            int chargingType = acOrderCycleDetailDto.getAccountType();
            double allAmount = acOrderCycleDetailDto.getDetailAmount() + acOrderCycleDetailDto.getLateAmount();
            if (chargingType == ChargingType.WY.getCode()) {
                tBsPayInfo.setWyAmount(allAmount);
                continue;
            }
            if (chargingType == ChargingType.BT.getCode()) {
                tBsPayInfo.setBtAmount(allAmount);
                continue;
            }
            if (chargingType == ChargingType.WATER.getCode()) {
                tBsPayInfo.setWaterAmount(allAmount);
                continue;
            }
            if (chargingType == ChargingType.ELECT.getCode()) {
                tBsPayInfo.setElectAmount(allAmount);
                continue;
            }
        }
        buildingCodes.add(acOrderDto.getHouseCodeNew());
        tBsPayInfo.setBuildingCodes(buildingCodes);
        tBsPayInfo.setCommonAmount(0D);
        tBsPayInfo.setPayCash(0D);
        tBsPayInfo.setPayUnion(0D);
        tBsPayInfo.setPayWx(acOrderDto.getAmount());
        tBsPayInfo.setCreateId(acOrderDto.getPayer());
        tBsPayInfo.setCustId(acOrderDto.getPayer());
        tBsPayInfo.setCustId(acOrderDto.getPayer());
        tBsPayInfo.setProjectId(xhIdBinding.getProjectCode());
        tBsPayInfo.setModifyId(acOrderDto.getPayer());
        tBsPayInfo.setJmRemark(null);
        tBsPayInfo.setPayType(2);
        tBsPayInfo.setStatus(0);
        tBsPayInfo.setPayerName(acOrderDto.getPayer());
        return tBsPayInfo;
    }

    private boolean checkSign (ResultUnifiedorder resultUnifiedorder){
        // TODO: 18/8/16 返回参数要校验签名
        return true;
    }


    private void responseSuccess(HttpServletResponse response) throws IOException {
        response.getWriter().write("<xml>" +
                "<return_code><![CDATA[SUCCESS]]></return_code>" +
                "<return_msg><![CDATA[OK]]></return_msg>" +
                "</xml>");
    }

    private void responseFail(HttpServletResponse response,String msg) throws IOException {
        response.getWriter().write(
                "<xml>" +
                        "<return_code><![CDATA[FAIL]]></return_code>" +
                        "<return_msg><![CDATA["+msg+"]]>" +
                        "</return_msg>" +
                        "</xml>");
    }

}
