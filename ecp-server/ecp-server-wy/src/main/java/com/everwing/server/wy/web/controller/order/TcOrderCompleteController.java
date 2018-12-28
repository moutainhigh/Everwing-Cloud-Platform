package com.everwing.server.wy.web.controller.order;

import com.esotericsoftware.minlog.Log;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.utils.PropertiesHelper;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.order.TcOrderComplete;
import com.everwing.coreservice.wy.api.annex.AnnexApi;
import com.everwing.coreservice.wy.api.order.TcOrderCompleteApi;
import com.everwing.server.wy.util.ImportExportHelper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * 完成工单
 *
 */
@Controller
@RequestMapping("/OrderComplete")
public class TcOrderCompleteController {
	
	private static final Logger LOG = Logger.getLogger(TcOrderComplaintController.class);

	@Autowired
	private TcOrderCompleteApi tcOrderCompleteApi;
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public @ResponseBody BaseDto insert(HttpServletRequest req, @RequestBody TcOrderComplete entity){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcOrderCompleteApi.insert(ctx.getCompanyId(),entity));
	}
	
	/**
	 * @TODO 根据id获取完成工单
	 * @param req
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/findById/{id}",method=RequestMethod.POST)
	public @ResponseBody BaseDto findById(HttpServletRequest req, @PathVariable String id){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcOrderCompleteApi.findById(ctx.getCompanyId(),id));
	}
	
	/**
	 * @TODO 显示投诉,完成,附件以及抄表数据详情
	 * @param req
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/findDetailById/{id}",method=RequestMethod.GET)
	public @ResponseBody BaseDto findDetailById(HttpServletRequest req, @PathVariable String id){
		BaseDto baseDto = new BaseDto();
		MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tcOrderCompleteApi.findDetailById(ctx.getCompanyId(),id));
	}
	
	@RequestMapping(value="/uploadFiles",method=RequestMethod.POST)
	public @ResponseBody MessageMap uploadFiles(HttpServletRequest request, HttpServletResponse response){
    	MessageMap mm = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
    //  模块名称，必填，只能用英文名
        String moduleDescription = CommonUtils.null2String(request.getParameter("moduleDescription"));
        if(StringUtils.isBlank(moduleDescription)){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("上传文件失败，模块描述必须填。");
            return mm;
        }else if(!moduleDescription.matches("[A-Za-z0-9_]+")){
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("上传文件失败，模块描述只能是字母、数字、下划线或三者的组合。");
            return mm;
        }
        //编号，由各个模块的ID组成
        String moduleCode = CommonUtils.null2String(request.getParameter("moduleCode"));
        if(StringUtils.isBlank(moduleCode)){
        	 mm.setFlag(MessageMap.INFOR_WARNING);
             mm.setMessage("上传文件失败，模块ID必须填。");
             return mm;
        }
    	//公司编码
    	String companyId = CommonUtils.null2String(request.getParameter("companyId"));
        String dirConfig = PropertiesHelper.getInstance("config/commonConf.properties").getDefaultSystemValue("importExcelBaseDir") + File.separator + moduleDescription + File.separator;
        String uploadDir = dirConfig + moduleCode+File.separator+new DateTime().toString("yyyyMMdd");
         // 不存在目录则创建
        File dirTempFile = new File(uploadDir);
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }
        OutputStream os = null;
        InputStream is = null;

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        StringBuffer error = new StringBuffer();

        try {
        	List<FileItem> listfileItem = (List<FileItem>)upload.parseRequest(request);
        	if(listfileItem.size()>3){
        		mm.setFlag(MessageMap.INFOR_ERROR);
        		mm.setMessage("一次最多只能上传三张图片");
        		return mm;
        	}
        	Integer failCount=0;
        	Integer successCount=0;
        	
        		FileItem fileItem = listfileItem.get(0);  //前端上传插件是分多次提交的
        		String fileName =fileItem.getName().substring(0,fileItem.getName().lastIndexOf("."));
        		String fileType = fileItem.getName().substring(fileItem.getName().lastIndexOf(".") + 1);
        		mm= ImportExportHelper.existAnnex(companyId, moduleCode, fileName,fileType);
        		if(mm.getFlag().equals(MessageMap.INFOR_ERROR)){
        			error.append(mm.getMessage()).append("\n");
        			failCount++;
        		}else{//可以上传
        			try {
        				File uploadedFile = new File(uploadDir,fileName+"."+fileType);
            			os = new FileOutputStream(uploadedFile);

                        is = fileItem.getInputStream();

                        byte buf[] = new byte[10240];//可以修改 1024 以提高读取速度

                        int length = 0;

                        while( (length = is.read(buf)) > 0 ){
                            os.write(buf, 0, length);
                        }
                        os.flush();
                        
                        Annex annex = new Annex();
                    	annex.setAnnexName(fileName);
                    	annex.setAnnexType(1); //图片  目前暂时只能上传图片
                    	annex.setFileType(fileType);
                    	annex.setRelationId(moduleCode);
                    	annex.setAnnexAddress(uploadDir);
                    	annex.setAnnexTime(CommonUtils.getDateStr());
                    	annex.setIsMain("1"); //辅图
                    	
                        RemoteModelResult<BaseDto> remoteModelResult = ((AnnexApi) SpringContextHolder.getBean("annexApi")).addAnner(companyId, annex);
                        if(remoteModelResult.getModel().getMessageMap().getFlag().equals(MessageMap.INFOR_SUCCESS)){
                        	successCount++;
                        }else{
                        	failCount++;
                        	error.append(remoteModelResult.getModel().getMessageMap().getMessage()).append("\n");
                        }
                        
					} catch (FileNotFoundException e) {
						 LOG.error(e);
				         error.append("上传失败，没有发现目录[" + uploadDir + "]：" + e.getMessage());
				         failCount++;
					}catch(IOException e){
			            LOG.error(e);
			            error.append("上传失败:"+e.getMessage());
			            failCount++;
        		    }catch(Exception e){
        		    	LOG.error(e);
 			            error.append("上传失败:"+e.getMessage());
 			            failCount++;
        		    }finally{
        	            if(os != null){
        	                try {
        	                    os.close();
        	                } catch (IOException e) {
        	                    LOG.error(e);
        	                }
        	            }
        	            if(is != null){
        	                try {
        	                    is.close();
        	                } catch (IOException e) {
        	                    LOG.error(e);
        	                }
        	            }
        	        }
        	}
          
          //处理返回
          if(failCount>0){
        	  mm.setFlag(MessageMap.INFOR_WARNING);
        	  mm.setMessage("部分上传成功;成功了["+successCount+"]个;失败了["+failCount+"]个"+"\n"+error.toString());
          }
          if(successCount.equals(listfileItem.size())){
        	  mm.setFlag(MessageMap.INFOR_SUCCESS);
        	  mm.setMessage("全部上传成功;成功了["+successCount+"]个");
          }
          if(failCount.equals(listfileItem.size())){
        	  mm.setFlag(MessageMap.INFOR_ERROR);
        	  mm.setMessage("上传全部失败;失败了["+failCount+"]个");
          }
		} catch (Exception e) {
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage("上传失败!");
			LOG.error(e);
		}
    	return mm;
    }
	
	/**
	 * 新增产权变更完成工单
	 */
	@RequestMapping(value="/saveChangeCompleteOrder/{projectId}/{projectName}/{type}",method=RequestMethod.POST)
	public @ResponseBody BaseDto saveChangeCompleteOrder(HttpServletRequest req,
			                    @RequestBody TcOrderComplete entity,@PathVariable String projectId,
			                    @PathVariable String projectName,@PathVariable String type){
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		WyBusinessContext ctx = WyBusinessContext.getContext();
		try {
			ctx.setProjectId(projectId);
			ctx.setProjectName(projectName);
			RemoteModelResult<BaseDto> result = this.tcOrderCompleteApi.saveChangeCompleteOrder(ctx,entity,type);
			if(result.isSuccess()){
				baseDto = result.getModel();
				msgMap = baseDto.getMessageMap();
			}else{
				msgMap.setFlag(MessageMap.INFOR_ERROR);
				msgMap.setMessage(result.getMsg());
			}
		} catch (Exception e) {
			Log.info(CommonUtils.log(e.getMessage()));
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("系统错误!");
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
}
