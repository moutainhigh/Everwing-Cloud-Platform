package com.everwing.server.wy.web.controller.cust;

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.dto.TBcCollectionDto;
import com.everwing.coreservice.common.wy.entity.cust.TBcCollection;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.api.cust.CollectionApi;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 银行托收
 *
 * @author DELL shiny
 * @create 2017/9/14
 */
@Controller
@RequestMapping("collection")
public class CollectionController {

    private static final Logger logger= LogManager.getLogger(CollectionController.class);

    @Autowired
    private CollectionApi collectionApi;

    @Autowired
    private FastDFSApi fastDFSApi;

    @RequestMapping("loadListByBuildingCode")
    @ResponseBody
    public BaseDto loadListByBuildingCode(@RequestBody TBcCollectionDto tBcCollectionDto){
        if(tBcCollectionDto==null){
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"未传入参数！"));
        }else {
            if(!StringUtils.isNotEmpty(tBcCollectionDto.getRelateBuildingCode())){
                return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"未传入参数！"));
            }
        }
        RemoteModelResult<BaseDto> remoteModelResult=collectionApi.loadCollectionListByBuildingCode(tBcCollectionDto);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto=remoteModelResult.getModel();
            if(baseDto!=null) {
                List<TBcCollectionDto> collectionDtoList = baseDto.getLstDto();
                for(TBcCollectionDto tBcCollection:collectionDtoList){
                    String attachment=tBcCollection.getAttachment();
                    if(attachment!=null&&StringUtils.isNotEmpty(attachment)){
                        RemoteModelResult<List<UploadFile>> fileResult=fastDFSApi.loadFilesByIds(attachment.split(","));
                        if(fileResult.isSuccess()) {
                            tBcCollection.setFiles(fileResult.getModel());
                        }
                    }
                }
            }
        }
        return BaseDtoUtils.getDto(remoteModelResult);
    }

    @RequestMapping("loadBanksByProjectId")
    @ResponseBody
    public BaseDto loadBanksByProjectId(String projectId){
        if(!StringUtils.isNotEmpty(projectId)){
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"未传入参数！"));
        }
        return BaseDtoUtils.getDto(collectionApi.loadBanksByProjectId(projectId));
    }

    @RequestMapping("loadChargingItemsByProjectId")
    @ResponseBody
    public BaseDto loadChargingItemsByProjectId(String projectId){
        if(!StringUtils.isNotEmpty(projectId)){
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"未传入参数"));
        }
        return BaseDtoUtils.getDto(collectionApi.loadChargingItems(projectId));
    }

    @RequestMapping("uploadFileAttachment")
    @ResponseBody
    public BaseDto uploadAttachment(MultipartFile file){
        logger.info("开始上传附件");
        if(file!=null){
            try {
                RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.uploadFile(file);
                if (remoteModelResult.isSuccess()) {
                    logger.info("上传附件成功!");
                    UploadFile uploadFile = remoteModelResult.getModel();
                    BaseDto baseDto =new BaseDto(uploadFile);
                    baseDto.setMessageMap(new MessageMap(MessageMap.INFOR_SUCCESS,"上传成功"));
                    return baseDto;
                }
            } catch (Exception e) {
                logger.error("上传附件失败!");
            }
        }
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"上传失败!"));
    }

    @RequestMapping("newCollection")
    @ResponseBody
    public BaseDto newCollection(@RequestBody TBcCollection tBcCollection){
        logger.info("开始办理托收:{}",tBcCollection);
        if(tBcCollection!=null){
            if(tBcCollection.getCreateBank()==null){
                logger.error("bank参数未传入,托收办理失败!");
                return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"参数错误,操作失败!"));
            }
            tBcCollection.setCreateBy(WyBusinessContext.getContext().getUserId());
            RemoteModelResult remoteModelResult=collectionApi.add(tBcCollection);
            if(remoteModelResult.isSuccess()){
                logger.info("办理托收成功!");
                return BaseDtoUtils.getDto(remoteModelResult);
            }
        }else {
            logger.info("参数错误，托收办理失败!");
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"参数错误,操作失败!"));
        }
        logger.info("办理托收失败!");
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,"托收办理失败!"));
    }
    @RequestMapping("updateCollection")
    @ResponseBody
    public BaseDto updateCollection(@RequestBody TBcCollection tBcCollection) {
        logger.info("更新托收信息!");
        if (tBcCollection != null) {
            String id = tBcCollection.getId();
            if (!StringUtils.isNotEmpty(id)) {
                logger.info("参数错误,托收信息更新失败!");
                return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "参数错误,操作失败!"));
            }
            RemoteModelResult remoteModelResult = collectionApi.update(tBcCollection);
            if (remoteModelResult.isSuccess()) {
                logger.info(((BaseDto)remoteModelResult.getModel()).getMessageMap().getMessage());
                return BaseDtoUtils.getDto(remoteModelResult);
            }else{
            }
        } else {
            logger.info("参数错误,更新托收失败!");
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "参数错误,操作失败!"));
        }
        logger.info("更新托收信息失败");
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "更新失败!"));
    }

    @RequestMapping("delete")
    @ResponseBody
    public BaseDto delete(String ids){
        logger.info("删除托收信息:{}",ids);
        if(StringUtils.isNotEmpty(ids)){
            RemoteModelResult remoteModelResult=collectionApi.batchDelete(ids);
            if(remoteModelResult.isSuccess()){
                logger.info("删除托收信息成功!");
            }else {
                logger.info("删除托收信息失败!");
            }
            return BaseDtoUtils.getDto(remoteModelResult);
        }
        logger.info("参数错误,托收删除失败!");
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "参数错误,操作失败!"));
    }

    @RequestMapping("effective")
    @ResponseBody
    public BaseDto effective(String ids){
        logger.info("生效托收:{}",ids);
        if(StringUtils.isNotEmpty(ids)){
            RemoteModelResult remoteModelResult = collectionApi.batchEffective(ids);
            if(remoteModelResult.isSuccess()){
                logger.info("生效托收成功!");
            }else {
                logger.info("生效托收失败!");
            }
            return BaseDtoUtils.getDto(remoteModelResult);
        }
        logger.info("参数错误,生效托收失败");
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "参数错误,操作失败!"));
    }

    @RequestMapping("unEffective")
    @ResponseBody
    public BaseDto unEffective(String ids){
        logger.info("失效托收:{}",ids);
        if(StringUtils.isNotEmpty(ids)){
            RemoteModelResult remoteModelResult = collectionApi.batchUnEffective(ids);
            if(remoteModelResult.isSuccess()) {
                logger.info("失效托收成功!");
            }else {
                logger.info("失效托收失败!");
            }
            return BaseDtoUtils.getDto(remoteModelResult);
        }
        logger.info("参数错误,失效托收失败!");
        return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR, "参数错误,操作失败!"));
    }

}
