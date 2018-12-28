package com.everwing.server.wy.web.controller.common;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.api.annex.AnnexApi;


@Controller
@RequestMapping("/TsAnnexController")
public class TsAnnexController {
	
	private static final Logger log = Logger.getLogger(TsAnnexController.class);
	@Autowired
	private AnnexApi annexApi;

	@Autowired
	FastDFSApi fastDFSApi;
	
	@RequestMapping(value="/queryBillEnclosure",method=RequestMethod.POST)
	public @ResponseBody BaseDto queryBillEnclosure(HttpServletRequest req,@RequestBody Annex annex){
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		try {
			RemoteModelResult<BaseDto> result = this.annexApi.queryBillEnclosure(ctx, annex);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
			
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统错误!");
			log.info(CommonUtils.log(e.getMessage()));
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	/**
	 * 下载附件
	 */
	@RequestMapping(value="/getEnclosureById/{annexId}",method=RequestMethod.POST)
	public @ResponseBody BaseDto getEnclosureById(HttpServletRequest req,@PathVariable String annexId){
		BaseDto baseDto  = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = null;
		try {
			 ctx = WyBusinessContext.getContext();
		  RemoteModelResult<UploadFile>	result = this.fastDFSApi.loadFilePathById(annexId);
		 if(result.isSuccess()){
			 msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			 baseDto.setObj(result.getModel());
		 }else{
			 msgMap.setFlag(MessageMap.INFOR_ERROR);
			 msgMap.setMessage(result.getMsg());
		 }
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统异常!");
			log.info(CommonUtils.log(e.getMessage()));
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	
	/**
	 * 批量下载
	 */
	@RequestMapping(value="/batchloadEnclosure",method=RequestMethod.POST)
	public @ResponseBody BaseDto batchloadEnclosure(HttpServletRequest req,@RequestBody Annex annex){
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx =null;
		try {
			ctx = WyBusinessContext.getContext();
			if(CommonUtils.isNotEmpty(annex)){
				List<String> annIds = annex.getAnnexIds();
				if(CollectionUtils.isEmpty(annIds)){
					msgMap.setFlag(MessageMap.INFOR_ERROR);
					msgMap.setMessage("传入的附件编号为空!");
				}else{
					String[] Ids =new String[]{};
					Ids = annIds.toArray(Ids);
					RemoteModelResult<List<UploadFile>> result = this.fastDFSApi.loadFilesByIds(Ids);
					if(result.isSuccess()){
						msgMap.setFlag(MessageMap.INFOR_SUCCESS);
						baseDto.setLstDto(result.getModel());
					}else{
						msgMap.setFlag(MessageMap.INFOR_ERROR);
						msgMap.setMessage(result.getMsg());
					}
				}
				
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage("传入的参数为空!");
			}
			
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统错误!");
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
}
