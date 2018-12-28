package com.everwing.server.wy.web.controller.configuration.bc.collection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.AnnexEnum;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackHead;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.api.annex.AnnexApi;
import com.everwing.coreservice.wy.api.configuration.bc.collection.TBcCollectionApi;
import com.everwing.coreservice.wy.api.configuration.project.TBsProjectApi;

/**
 * @TODO 托收Controller
 *
 */
@Controller
@RequestMapping("/bc/collection")
public class TBcCollectionController {

	private static final Logger logger = LogManager.getLogger(TBcCollectionController.class);
	
	@Autowired
	private FastDFSApi fastDFSApi;
	
	@Autowired
	private TBsProjectApi tBsProjectApi;
	
	@Autowired
	private AnnexApi annexApi;
	
	/**
	 * 托收管理:
	 * 		银联托收: 1代
	 * 		
	 * 		金融联托收: 2代
	 * 
	 * 		提供功能:
	 * 			1.  项目托收	    新建表?
	 * 				1.1    托收分页显示
	 * 				1.2    列表中托收开关编辑
	 * 
	 * 			2.  项目托收详情页
	 * 				2.1  按年切换, 按月份行列转换, 按银联与金融联托收分离   显示 托收户数 , 总计托收户数的总账单金额 , 托收成功户数 , 托收成功金额 , 托收失败金额, 托收状态
	 * 				
	 *	        3.  对托收状态为待托收的月份, 提供托收按钮
	 *				3.1 指定起止时间 , 查询并生成托收文件     此处只按格式生成托收文件, 托收文件上传给银行由操作员进行, 与本项目无关 				
	 */				
	
	/**                                        '%:                                                     `
	*------------------------------------ bug太多  已经崩溃 ------------------------------------------------`
	*                                          ;|`                                                     `
	*                                          ;|`                                                     `
	*                                          ;|`                                                     `
	*                                          :|`       .;$######&|'                                  `
	*                             `:`    .'!!||$&%!;:`..|#############%`                               `
	*                          '|%$&$|;'.               `;%@############!                              `
	*            `|$&@@#@&$||%|%$;.                          '$#########@;                             `
	*          :|%@##########$:                                '$########%.                            `
	*        :|||&##########!                                    '&######$`                            `
	*      `:;:!@#########@;                    ;&&;.             `%#####%`                            `
	*        .:%#########@;            ;$;.       `|&@|`           `$####!                             `
	*        .`!@########!          .!&$|$|.           `!$$:        :&##%.                             `
	*          `$#######%`       `;$&|:!&##&:   .:$@@##@$|'          !%'                               `
	*         ..:&#####@:       .`:|&####@|:.    ..   ..`''``.       `:.                               `
	*            '$####$`       '|%!%%!:`           .      .`''`.     :'                               `
	*             .;&##!       ``....`..                      .`'`.   ::                               `
	*                .:'     .``                    .           `''.  .:`                              `
	*             .``:;`     `.           ..    ':.  `!|:       .''.   ::                              `
	*            .;||%!     .`.      ``    `;$$|'                `'`.  `:`                             `
	*            `;;;|;     ..     .;`                  ..       .`'`  .:`                             `
	*           `!%|$!.   ``.    `;`   ..         '%&&@####$'    ```. `;`                             `
	*             :!!%|.  .``.    ':`|#@$%%$&@#@$$$@#######@;     .```.:;                              `
	*              .`'|; .```.     .:&#######%`      ...      `.   .  :;.                              `
	*                 '|;``.``.       ;%$$!`   `:``'       `!:      .!;                                `
	*                 ```'.  .`     ..   `;:                      '|;                                  `
	*                   .`|$;``                                :&#&:                                   `
	*                .''.....'|%:.                        .;&#######|.                                 `
	*                  .``':;:!@###@%;'.          .`:!%%|:`.     .;&@;                                 `
	*                  `'`. '$##############@!'`.                    !&%!!:.                           `
	*                  :|%%%&#############@;                          '$####|.                         `
	*                 `!%%%%&##############|.                         .|##&|!!;;::'``:%@##$`           `
	*                  .;%$$&@##############$`                        !###################$'           `
	*                   .:;;;$################&:                   .!@####################@$&&&&&&&@@&%!
	*                      '!%@########|`;@#######&|;:::!!:'':;|&######################@$:             `
	*                    `|&&;!@########$' '$########################%'.                               `
	*                '%&%:      ;@########&:   '|@#################@;                                  `
	*            :$$;.             :$#####@:        `$##############;                                  `
	*        .!&&:                      ..            ;@###########&@##|                               `
	*     :$&;.                                         ;@#############%.                              `
	* '%@%'                                               `|@#########@;                               `
	* $:                                                       `|@####%`                               `
	*/
	
	@Autowired
	private TBcCollectionApi tBcCollectionApi;
	
	
	/**
	 * @TODO 分页加载    银联托收   母表
	 * @param req
	 * @param TBcCollectionHead
	 * @return
	 */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="银联托收 : 分页查询母表",operationType= OperationEnum.Search)
	@PostMapping("/listPageUnionCollHeads")
	public @ResponseBody BaseDto listPageUnionCollHeads(HttpServletRequest req , @RequestBody TBcUnionCollectionHead head){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBcCollectionApi.listPageUnionCollHeads(ctx.getCompanyId(),head));
	}
    
    /**
     * @TODO 分页加载    银联托收   子表
     * @param req
     * @param TBcCollectionHead
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="银联托收 : 分页查询子表",operationType= OperationEnum.Search)
    @PostMapping("/listPageCollBodyInfos")
    public @ResponseBody BaseDto listPageCollBodyInfos(HttpServletRequest req , @RequestBody TBcUnionCollectionBody body){
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	return BaseDtoUtils.getDto(this.tBcCollectionApi.listPageCollBodyInfos(ctx.getCompanyId(),body));
    }
    
    /**
	 * @TODO 分页加载    银联回盘   母表
	 * @param req
	 * @param TBcCollectionHead
	 * @return
	 */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="银联回盘 : 分页查询母表",operationType= OperationEnum.Search)
	@PostMapping("/listPageUnionBackHeads")
	public @ResponseBody BaseDto listPageUnionBackHeads(HttpServletRequest req , @RequestBody TBcUnionBackHead head){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBcCollectionApi.listPageUnionBackHeads(ctx.getCompanyId(),head));
	}
    
    /**
     * @TODO 分页加载    银联回盘 子表
     * @param req
     * @param TBcCollectionHead
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="银联回盘 : 分页查询子表",operationType= OperationEnum.Search)
    @PostMapping("/listPageUnionBackbodies")
    public @ResponseBody BaseDto listPageUnionBackbodies(HttpServletRequest req , @RequestBody TBcUnionBackBody body){
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	return BaseDtoUtils.getDto(this.tBcCollectionApi.listPageUnionBackbodies(ctx.getCompanyId(),body));
    } 
    
    /**
	 * @TODO 分页加载    金融联托收/回盘   母表
	 * @param req
	 * @param TBcCollectionHead
	 * @return
	 */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="金融联托收/回盘 : 分页查询母表",operationType= OperationEnum.Search)
	@PostMapping("/listPageJrlHeads")
	public @ResponseBody BaseDto listPageJrlHeads(HttpServletRequest req , @RequestBody TBcJrlHead head){
		WyBusinessContext ctx = WyBusinessContext.getContext();
		return BaseDtoUtils.getDto(this.tBcCollectionApi.listPageJrlHeads(ctx.getCompanyId(),head));
	}
    
    /**
     * @TODO 分页加载    金融联托收/回盘   子表
     * @param req
     * @param TBcCollectionHead
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="金融联托收/回盘 : 分页查询子表",operationType= OperationEnum.Search)
    @PostMapping("/listPageJrlBodies")
    public @ResponseBody BaseDto listPageJrlBodies(HttpServletRequest req , @RequestBody TBcJrlBody body){
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	return BaseDtoUtils.getDto(this.tBcCollectionApi.listPageJrlBodies(ctx.getCompanyId(),body));
    }
    
    /**
     * @TODO 生成银联托收文件txt并上传
     * @param req
     * @param TBcCollectionHead
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="银联托收 : 生成托收文件并上传到服务器",operationType= OperationEnum.Search)
    @PostMapping("/genCollTxtFile")
    public @ResponseBody BaseDto genCollTxtFile(HttpServletRequest req , @RequestBody TBcUnionCollectionHead head){
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	return BaseDtoUtils.getDto(this.tBcCollectionApi.genCollTxtFile(ctx.getCompanyId(),head)); 
    }

    /**
     * @TODO 提出银联托收文件txt
     * @param req
     * @param TBcCollectionHead
     * @return
     * @throws UnsupportedEncodingException 
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="银联/金融联托收 : 提出托收文件",operationType= OperationEnum.Search)
    @PostMapping("/exportCollTxtFile/{type}")
    public @ResponseBody BaseDto exportCollTxtFile(HttpServletRequest req , @RequestBody TBcUnionCollectionHead head , @PathVariable Integer type,
    											   HttpServletResponse resp) throws UnsupportedEncodingException{
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	return BaseDtoUtils.getDto(this.tBcCollectionApi.exportCollTxtFile(ctx.getCompanyId(),head,type));
    }
    
    /**
     * @TODO 提出银联托收文件txt
     * @param req
     * @param TBcCollectionHead
     * @return
     * @throws UnsupportedEncodingException 
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="银联/金融联托收 : 下载托收文件",operationType= OperationEnum.Search)
    @GetMapping("/download")
    public @ResponseBody MessageMap download(HttpServletRequest req , HttpServletResponse resp) throws UnsupportedEncodingException{
    	String fileName = CommonUtils.null2String(req.getParameter("fileName"));
    	String path = CommonUtils.null2String(req.getParameter("path"));
    	Integer type = CommonUtils.null2Int(req.getParameter("type"));
    	
    	RemoteModelResult<byte[]> rslt = this.fastDFSApi.downloadFile(path);
    	if(rslt.isSuccess() && CommonUtils.isNotEmpty(rslt.getModel())){
    		fileName = URLEncoder.encode(fileName, "GBK");  
    		resp.reset();  
    		resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
    		resp.addHeader("Content-Length", "" + rslt.getModel().length);  
    		resp.setContentType("text/plain;charset=" + "GBK");  
    		OutputStream os = null;
    		try {
    			os = resp.getOutputStream();
    			os.write(rslt.getModel());
    			os.flush();
    			os.close();
    		} catch (IOException e) {
    			logger.error("托收文件提出  : 写出数据失败. ");
    			e.printStackTrace();
    		} finally{
    			if(null != os){
    				try {
    					os.close();
    				} catch (IOException e) {
    					e.printStackTrace();
    				} finally{
    					os = null;
    				}
    			}
    		}
    		return new MessageMap(MessageMap.INFOR_WARNING, "托收文件下载: 下载完成. ");
    	}else{
    		return new MessageMap(MessageMap.INFOR_WARNING, "托收文件下载: 未找到对应的托收文件. ");
    	}
    }
    
    
    /**
     * @TODO 生成金融联托收文件 两个 打成zip包 并上传
     * @param req
     * @param TBcCollectionHead
     * @return
     */
    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="金融联托收 : 生成托收文件并上传到服务器",operationType= OperationEnum.Search)
    @PostMapping("/genJrlCollZipFile")
    public @ResponseBody BaseDto genJrlCollZipFile(HttpServletRequest req , @RequestBody TBcJrlHead head){
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	return BaseDtoUtils.getDto(this.tBcCollectionApi.genJrlCollZipFile(ctx.getCompanyId(),head)); 
    }
    
    /**
     * @TODO 导入回盘文件
     * @param req
     * @param resp
     * @return
     */
	@WyOperationLogAnnotation(moduleName= OperationEnum.Module_Collection,businessName="文件回盘 : 上传文件到fastDFS,并取出数据进行处理",operationType= OperationEnum.Search)
    @PostMapping("/import/{flag}/{projectId}/{totalId}")
    public @ResponseBody MessageMap importFile(MultipartFile file,
    										   @PathVariable("flag") Integer flag , 
    										   @PathVariable("totalId") String totalId,
    										   @PathVariable("projectId") String projectId){
    	
    	WyBusinessContext ctx = WyBusinessContext.getContext();
    	
    	MessageMap msgMap = new MessageMap();
    	
    	if(null == file){
    		msgMap.setFlag(MessageMap.INFOR_WARNING);
    		msgMap.setMessage("传入文件为空,回盘文件导入失败");
    		return msgMap;
    	}
    	
        String msg = "";
    	RemoteModelResult<UploadFile> rslt = null;
		try {
			rslt = this.fastDFSApi.uploadFile(file);
			
			if(rslt.isSuccess() && CommonUtils.isNotEmpty(rslt.getModel())){
				
				this.annexApi.addAnner(ctx.getCompanyId(),
										new Annex(rslt.getModel(), AnnexEnum.annex_type_txt.getIntV(), totalId, projectId));
			}else{
				msg += "文件: " + file.getName() + "上传失败, ";
			}
			
		} catch (Exception e) {
			logger.error("回盘文件导入: 文件上传时出现异常. 文件名:{},异常数据:{}.",file.getName(),e.getMessage());
			msgMap.setFlag(MessageMap.INFOR_WARNING);
    		msgMap.setMessage("文件: " + file.getName() + "上传失败.  ");
    		return msgMap;
		} 
    	
		if(CommonUtils.isEmpty(file)){
			msgMap = new MessageMap(MessageMap.INFOR_WARNING,"上传文件为空,导入完成.");
		}else{
			String str = null;
			BufferedReader reader = null;
			try {
				if(flag == 1){
					//金融联
					str = new String(file.getBytes(), "GBK");
				}else{
					//银联
					reader = new BufferedReader(new InputStreamReader(new BOMInputStream(file.getInputStream()),"UTF-8"));
					if(null != reader){
						String data = null;
						str = "";
						while( (data = reader.readLine()) != null){
							str += data + "\n";
						}
					}
				}
			} catch (Exception e) {
				logger.error("回盘文件导入: 数据转换成字符串出现异常. ");
				msgMap = new MessageMap(MessageMap.INFOR_ERROR,"数据处理出现异常.");
				e.printStackTrace();
				return msgMap;
			}finally{
				if(null != reader){
					try {
						reader.close();
						reader = null;
					} catch (IOException e) {
						e.printStackTrace();
					} finally{
						reader = null;
					}
				}
			}
			CommonsMultipartFile cmFile = (CommonsMultipartFile)file;
			FileItem item = cmFile.getFileItem();
			String fileName = item.getName();
			RemoteModelResult<MessageMap> mmRslt = this.tBcCollectionApi.importFileByQueue(ctx.getCompanyId(), str, fileName, flag, totalId, projectId,ctx.getUserId());  
			
			if(mmRslt.isSuccess()){
				msgMap.setFlag(MessageMap.INFOR_SUCCESS);
				msgMap = mmRslt.getModel();
				if(!msg.isEmpty()){
					msgMap.setMessage("文件上传: " + msg + ", \n" + rslt.getMsg());
				}
			}else{
				msgMap.setFlag(MessageMap.INFOR_WARNING);
				if(!msg.isEmpty()){
					msgMap.setMessage("文件上传: " + msg + ", \n" + rslt.getMsg());
				}else{
					msgMap.setMessage(rslt.getMsg());
				}
			}
		}

    	return msgMap;
    }
	
}
 