package com.everwing.server.wy.web.controller.common;/**
 * Created by wust on 2018/1/16.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.system.importExport.Excel;
import com.everwing.coreservice.wy.api.common.ComplexReportExportApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * Function:
 * Reason:
 * Date:2018/1/16
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/ComplexReportExportController")
public class ComplexReportExportController {
    @Autowired
    private ComplexReportExportApi complexReportExportApi;

    /**
     * 导出格式固定的复杂excel报表
     * 你可以复制这个方法
     * @param excel
     * @return
     */
    @RequestMapping(value = "/exportDemo", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap exportDemo(@RequestBody Excel excel) {
        MessageMap mm = new MessageMap();

        WyBusinessContext ctx = WyBusinessContext.getContext();

        if (excel == null) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("导出参数[XML Name, Excel Version, Module Description]不能为空。");
            return mm;
        } else if (StringUtils.isBlank(CommonUtils.null2String(excel.getXmlName()))) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("XML Name参数不能少噢。");
            return mm;
        } else if (StringUtils.isBlank(CommonUtils.null2String(excel.getExcelVersion()))) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("Excel Version参数不能少噢。");
            return mm;
        } else if (!excel.getModuleDescription().matches("[A-Za-z0-9_]+")) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage("上传文件失败，模块描述只能是字母、数字、下划线或三者的组合。");
            return mm;
        }

        RemoteModelResult<MessageMap> remoteModelResult = complexReportExportApi.exportDemo(ctx,excel);
        if(remoteModelResult.isSuccess()){

        }else {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(remoteModelResult.getMsg());
        }
        return mm;
    }
}
