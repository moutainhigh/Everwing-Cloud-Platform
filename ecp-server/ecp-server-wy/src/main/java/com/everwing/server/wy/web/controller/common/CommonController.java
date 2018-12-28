package com.everwing.server.wy.web.controller.common;/**
 * Created by wust on 2017/9/26.
 */

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.asset.AssetSelectSearch;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.customer.CustomerSelectSearch;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectList;
import com.everwing.coreservice.common.wy.entity.common.select.vehicle.VehicleSelectSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuildingSearch;
import com.everwing.coreservice.common.wy.entity.property.property.CustomerSearch;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.api.common.WyCommonApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 *
 * Function:该控制器存放系统一些公共组件，比如一些公共的弹出框的查询等等
 * Reason:
 * Date:2017/9/26
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/CommonController")
public class CommonController {

    static Logger logger = LogManager.getLogger(CommonController.class);

    @Autowired
    protected FastDFSApi fastDFSApi;

    @Autowired
    private WyCommonApi commonApi;

    @Autowired
    protected SpringRedisTools springRedisTools;

    /**
     * 显示图片
     * @param response
     * @param fileId
     * @throws IOException
     */
    @GetMapping("/showImage/{fileId}")
    public void showImage(HttpServletResponse response, @PathVariable String fileId) throws IOException {
        RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.loadFilePathById(fileId);
        if(remoteModelResult.isSuccess()){
            UploadFile uploadFile = remoteModelResult.getModel();
            if(uploadFile != null){
                response.setHeader("Content-Length", ""+uploadFile.getSize() / 10);
                response.sendRedirect(uploadFile.getPath());
                logger.info("文件重定向：{}",uploadFile.getPath());
            }else {
                logger.info("文件服务器没有该文件：{}",fileId);
            }
        }else{
            logger.info("文件重定向失败：{}",remoteModelResult.getMsg());
        }
    }

    /**
     * 生产指定大小的图片
     * @param response
     * @param fileId
     * @param width 宽
     * @param height 高
     */
    @GetMapping("/showImageBySize/{fileId}/{width}/{height}")
    public void showImageBySize(HttpServletResponse response, @PathVariable String fileId,@PathVariable Integer width,@PathVariable Integer height) {
        boolean flag = true;
        RemoteModelResult<UploadFile> remoteModelResult = fastDFSApi.loadFilePathById(fileId);
        if(remoteModelResult.isSuccess()){
            UploadFile uploadFile = remoteModelResult.getModel();
            if(uploadFile != null){
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
                try {
                    URL url = new URL(uploadFile.getPath());
                    logger.info("图片URL={}",url.getPath());
                    BufferedImage bufferedImage1 = ImageIO.read(url);
                    Image image=  bufferedImage1.getScaledInstance(width, height, BufferedImage.SCALE_DEFAULT);
                    bufferedImage.getGraphics().drawImage(image, 0, 0, null);
                    ImageIO.write(bufferedImage, uploadFile.getSuffix(),response.getOutputStream());//把图片输出
                } catch (FileNotFoundException e) {
                    logger.error("找不到文件：",e);
                    flag = false;
                } catch (IOException e) {
                    logger.error("生成指定大小的图片失败：",e);
                    flag = false;
                }
            }else {
                logger.info("文件服务器没有该文件：{}",fileId);
                flag = false;
            }
        }else{
            logger.info("生产指定大小的图片失败：{}",remoteModelResult.getMsg());
            flag = false;
        }

        if(!flag){
            // TODO 生产本地默认图片

        }
    }

    /**
     * 把文件从文件服务器删除
     * @param fileId
     * @return
     */
    @RequestMapping(value="/deleteDFSFile/{fileId}",method=RequestMethod.DELETE)
    public @ResponseBody MessageMap deleteDFSFile(@PathVariable String fileId){
        MessageMap mm = new MessageMap();
        RemoteModelResult remoteModelResult=fastDFSApi.deleteFile(fileId);
        if (!remoteModelResult.isSuccess()){
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("删除文件失败");
        }
        return mm;
    }


    /**
     * 根据文件标识去平台获取文件上传信息
     * @return
     */
    @RequestMapping(value="/downloadFastFile",method=RequestMethod.GET)
    public @ResponseBody MessageMap downloadFastFile(HttpServletRequest request, HttpServletResponse response){
        MessageMap mm = new MessageMap();
        String uploadFileId = request.getParameter("uploadFileId");

        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");

            RemoteModelResult<UploadFile> result = this.fastDFSApi.loadFilePathById(uploadFileId);
            if(result.isSuccess()){
                UploadFile uploadFile = result.getModel();
                response.setHeader("Content-Disposition","attachment;filename=" + new String(uploadFile.getFileName().getBytes("UTF-8"), "ISO8859-1"));

                OutputStream outputStream = null;
                InputStream in = null;
                try {
                    outputStream = response.getOutputStream();

                    URL url = new URL(uploadFile.getPath());
                    HttpURLConnection uc = (HttpURLConnection) url.openConnection();
                    uc.setDoInput(true);//设置是否要从 URL 连接读取数据,默认为true
                    uc.connect();
                    in = uc.getInputStream();
                    int len = 0;
                    byte[] buf = new byte[1024];
                    while ((len = in.read(buf, 0, 1024)) != -1) {
                        outputStream.write(buf, 0, len);
                    }
                } catch (IOException e) {
                    logger.error(e);
                }finally {
                    try {
                        if(in != null){
                            in.close();
                        }
                        if(outputStream != null){
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        logger.error(e);
                    }
                }
            }else{
                mm.setFlag(MessageMap.INFOR_ERROR);
                mm.setMessage(result.getMsg());
            }
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("系统异常!");
        }
        return mm;
    }


    /**
     * 注意，这种下载只适合当前用户即存即取（因为只有这样才能保证你下载的是你刚刚存储的文件）
     * @return
     * @throws IOException
     */
    @RequestMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile() throws IOException {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        try {
            Object obj = springRedisTools.getByKey(String.format(WyEnum.download_redis_file_by_loginname_key.getStringValue(),ctx.getLoginName()));
            if (obj != null) {
                byte[] bt = (byte[])obj;
                HttpHeaders headers = new HttpHeaders();
                String fileName = UUID.randomUUID().toString().replaceAll("-","").toUpperCase() + ".xls";
                headers.setContentDispositionFormData("attachment", fileName);
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return new ResponseEntity<byte[]>(bt, headers, HttpStatus.CREATED);
            }
        }finally {
            springRedisTools.deleteByKey(String.format(WyEnum.download_redis_file_by_loginname_key.getStringValue(),ctx.getLoginName()));
        }
        return null;
    }


    /**
     * 选择客户组件：分页查询客户信息
     * @param customerSearch
     * @return
     */
    @RequestMapping(value = "/listPageCustomer",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageCustomer(@RequestBody CustomerSearch customerSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = commonApi.listPageCustomer(ctx.getCompanyId(),customerSearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 选择资产组件：分页查询资产信息
     * @param tcBuildingSearch
     * @return
     */
    @RequestMapping(value = "/listPageBuilding",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPageBuilding( @RequestBody TcBuildingSearch tcBuildingSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = commonApi.listPageBuilding(ctx,tcBuildingSearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 公共组件：客户下拉选择框
     * @param customerSearch
     * @return
     */
    @RequestMapping(value = "/findCustomerSelect",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto findCustomerSelect(@RequestBody CustomerSelectSearch customerSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<java.util.List<CustomerSelectList>> result = commonApi.findCustomerSelect(ctx,customerSearch);
        if(result.isSuccess()){
            List<CustomerSelectList> customerSelectListList = result.getModel();
            baseDto.setLstDto(customerSelectListList);
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 公共组件：资产下拉选择框
     * @param assetSelectSearch
     * @return
     */
    @RequestMapping(value = "/findAssetSelect",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto findAssetSelect(@RequestBody AssetSelectSearch assetSelectSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<java.util.List<AssetSelectList>> result = commonApi.findAssetSelect(ctx,assetSelectSearch);
        if(result.isSuccess()){
            List<AssetSelectList> assetSelectLists = result.getModel();
            baseDto.setLstDto(assetSelectLists);
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    /**
     * 公共组件：车辆下拉选择框
     * @param vehicleSelectSearch
     * @return
     */
    @RequestMapping(value = "/findVehicleSelect",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto findVehicleSelect(@RequestBody VehicleSelectSearch vehicleSelectSearch){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<java.util.List<VehicleSelectList>> result = commonApi.findVehicleSelect(ctx,vehicleSelectSearch);
        if(result.isSuccess()){
            List<VehicleSelectList> lists = result.getModel();
            baseDto.setLstDto(lists);
        }else{
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }
}
