package com.everwing.server.linphone.controller;


import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.wy.api.business.meterdata.TcMeterDataApi;
import com.everwing.coreservice.wy.api.business.watermeter.TcWaterMeterApi;
import com.everwing.server.linphone.vo.MeterYearDataVo;
import com.everwing.server.linphone.vo.WaterDataVo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("{version}/waterMeter")
public class WaterMterController {
	 private static final Logger logger= LogManager.getLogger(WaterMterController.class);
	  private static final String pattern_month = "MM";
	  private static final String pattern_year = "yyyy";
	   private static final String pattern = "yyyy-MM-dd HH:mm:ss";
	    @Autowired
	    private TcWaterMeterApi waterMeterApi;
	    @Autowired
	    private TcMeterDataApi tcMeterDataApi;
	    @PostMapping("getWaterMeter")
	    @ApiVersion(1.0)
	    public LinphoneResult findsByBuildingCode(HttpServletRequest request,String companyId,String buildingCode){
	    	LinphoneResult linphoneResult = new LinphoneResult();
//	    	 Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
//	          String accountId = account.getAccountId();
//	          String companyId = account.getCompanyId();
	          if(StringUtils.isBlank(companyId)) {
		            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
		            linphoneResult.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
		            linphoneResult.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
		            return linphoneResult;
		        }
	          RemoteModelResult<List<TcWaterMeter>> remoteModelResult = waterMeterApi.findsByBuildingCode(companyId, buildingCode);
	          List<WaterDataVo> listVo = new ArrayList<WaterDataVo>();
	          WaterDataVo waterDataVo = null;
	          if(remoteModelResult.isSuccess()){
	        	  List<TcWaterMeter> list = remoteModelResult.getModel();
	        	   for(TcWaterMeter tcWaterMeter : list){
	        		   waterDataVo = new WaterDataVo();
	        		   waterDataVo.setWaterMeterCode(tcWaterMeter.getCode());
	        		   waterDataVo.setWaterMeterName(tcWaterMeter.getWaterMeterName());
	        		   waterDataVo.setCompanyId(companyId);
	        		   listVo.add(waterDataVo);
	        	   }
	        	  linphoneResult.setCode(ReturnCode.API_RESOLVE_SUCCESS.getCode());
	        	  linphoneResult.setData(listVo);
	        	  linphoneResult.setMessage(ReturnCode.API_RESOLVE_SUCCESS.getDescription());
	        	  
	          } else {
	        	  linphoneResult.setCode(ReturnCode.API_RESOLVE_FAIL.getCode());
	        	  linphoneResult.setMessage(ReturnCode.API_RESOLVE_FAIL.getDescription());  
	          }
	    	return linphoneResult;
	    }
	    
	    
	    @PostMapping("getWaterMeterByYear")
	    @ApiVersion(1.0)
	    public LinphoneResult getMeterByYear(HttpServletRequest request,String companyId,String meterCode,String year){
			
//	    	Account account= JSON.parseObject(request.getAttribute("account").toString(),Account.class);
//	        String accountId = account.getAccountId();
//	        String companyId = account.getCompanyId();
	        int meterType = 0;
	        LinphoneResult linphoneResult = new LinphoneResult();
	        final FastDateFormat df = FastDateFormat.getInstance(pattern_month);
	        final FastDateFormat df1 = FastDateFormat.getInstance(pattern_year);
	        if(StringUtils.isBlank(companyId)) {
	            logger.debug(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
	            linphoneResult.setCode(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getCode());
	            linphoneResult.setMessage(ReturnCode.API_WY_COMPANYID_NOT_FOUND.getDescription());
	            return linphoneResult;
	        }
	        logger.debug("根据年份查询对应的水表元数据，" +
                    "请求参数:accountId={},companyId={},meterType={},biildingCode={},year={}",
            new Object[]{companyId,meterType,meterCode,year});

	        RemoteModelResult<List<TcMeterData>> remoteModelResult = tcMeterDataApi.queryMeterByYear(companyId,meterCode,meterType,year);
	        logger.debug("根据年份查询对应的水表元数据,远程调用返回数据：{}",remoteModelResult);
	        if(remoteModelResult.isSuccess()){
	        	  List<TcMeterData> list = remoteModelResult.getModel();
	        	  List<MeterYearDataVo> meterYearDataVoList = new ArrayList<MeterYearDataVo>();
	              MeterYearDataVo meterYearDataVo = null;
	        	  for (TcMeterData meterData : list) {
	        		  meterYearDataVo = new MeterYearDataVo();
	        		  meterYearDataVo.setMonth(df.format(meterData.getReadingTime()));
	        		  meterYearDataVo.setMeterCode(meterData.getMeterCode());
	        		  meterYearDataVo.setCorrection(String.valueOf((meterData.getCorrection()==null)?"":meterData.getCorrection()));
	        		  meterYearDataVo.setDosage(String.valueOf(meterData.getUseCount()));
	        		  meterYearDataVo.setTotalReading(String.valueOf(meterData.getTotalReading()));
	        		  meterYearDataVo.setYear(df1.format((meterData.getReadingTime())));
	        		  meterYearDataVoList.add(meterYearDataVo);
	        	  }
	        	  linphoneResult.setCode(ReturnCode.API_RESOLVE_SUCCESS.getCode());
	        	  linphoneResult.setData(meterYearDataVoList);
	        	  linphoneResult.setMessage(ReturnCode.API_RESOLVE_SUCCESS.getDescription());
	        	  
	          } else {
	        	  linphoneResult.setCode(ReturnCode.API_RESOLVE_FAIL.getCode());
	        	  linphoneResult.setMessage(ReturnCode.API_RESOLVE_FAIL.getDescription());  
	          }
	    	
	    	return linphoneResult;
	    	
	    	
	    	
	    }
	    
}
