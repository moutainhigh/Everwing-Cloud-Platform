package com.everwing.server.wy.web.controller.product;/**
 * Created by wust on 2018/2/2.
 */

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.product.TProductOrderSearch;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.wy.api.building.TcBuildingApi;
import com.everwing.coreservice.wy.api.product.TProductOrderApi;
import com.everwing.coreservice.wy.api.product.TProductPaymentApi;
import com.everwing.coreservice.wy.api.sys.TSysLookupApi;
import com.everwing.coreservice.wy.api.sys.TSysProjectApi;
import com.everwing.utils.CreateFileUtil;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 *
 * Function:产品相关报表
 * Reason:
 * Date:2018/2/2
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/TProductReportsController")
public class TProductReportsController {
    @Autowired
    protected TProductOrderApi tProductOrderApi;

    @Autowired
    protected SpringRedisTools springRedisTools;

    @Autowired
    private TSysProjectApi tSysProjectApi;

    @Autowired
    private TcBuildingApi tcBuildingApi;

    @Autowired
    private TProductPaymentApi tProductPaymentApi;

    @Autowired
    private TSysLookupApi tSysLookupApi;

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Product,businessName="导出产品收款报表",operationType= OperationEnum.Export)
    @RequestMapping(value = "/exportProductCollectingReports",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap exportProductCollectingReports(@RequestBody TProductOrderSearch tProductOrderSearch) {
        MessageMap mm = new MessageMap();
        try {
            exportProductCollectingReportsToXls(tProductOrderSearch);
        } catch (FileNotFoundException e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("导出失败："+e.getMessage());
        } catch (DRException e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("导出失败："+e.getMessage());
        } catch (IOException e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("导出失败："+e.getMessage());
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("导出失败："+e.getMessage());
        }
        return mm;
    }

    private void exportProductCollectingReportsToXls(TProductOrderSearch tProductOrderSearch) throws Exception {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        TSysProjectSearch tSysProjectSearch = new TSysProjectSearch();
        tSysProjectSearch.setLoginName(ctx.getLoginName());
        if(StringUtils.isNotBlank(tProductOrderSearch.getProjectId())){
            tSysProjectSearch.setProjectId(tProductOrderSearch.getProjectId());
        }

        TSysProjectList tSysProjectList = null;
        RemoteModelResult<BaseDto> remoteModelResult = tSysProjectApi.findByCondition(ctx,tSysProjectSearch);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto = remoteModelResult.getModel();
            List<TSysProjectList> tSysProjectLists = baseDto.getLstDto();
            if(CollectionUtils.isNotEmpty(tSysProjectLists)){
                tSysProjectList = tSysProjectLists.get(0);
            }
        }


        StyleBuilder boldCenteredStyle = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle).setBorder(stl.penThin()).setBackgroundColor(Color.LIGHT_GRAY);
        StyleBuilder columnStyle  = stl.style(boldCenteredStyle).setBorder(stl.penThin()).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.MIDDLE);

        File file = null;
        String prefix = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
        String suffix = ".xls";
        String path = CreateFileUtil.createTempFile(prefix,suffix,null);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(path));
            tProductOrderSearch.setProjectId(tSysProjectList.getProjectId());
            report()
                    .ignorePageWidth()
                    .setColumnTitleStyle(columnTitleStyle)
                    .setColumnStyle(columnStyle)
                    .highlightDetailEvenRows()
                    .columns(
                            col.column("收款项目",       "product_name",      type.stringType()).setWidth(300),
                            col.column("合计", "total_price", type.bigDecimalType())
                    )
                    .title(
                            cmp.text("产品收款报表").setStyle(boldCenteredStyle),
                            cmp.horizontalFlowList().add(cmp.text("项目：" + tSysProjectList.getName()).setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                                    .newRow()
                                    .add(cmp.text("日期从：" + tProductOrderSearch.getBeginTime()).setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                                    .newRow()
                                    .add(cmp.text("日期至：" + tProductOrderSearch.getEndTime()).setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                    )
                    .ignorePagination()
                    .setDataSource(createProductCollectingReportsDataSource(tProductOrderSearch))
                    .toXls(outputStream);

            file = new File(path);
            springRedisTools.addData(String.format(WyEnum.download_redis_file_by_loginname_key.getStringValue(),ctx.getLoginName()), FileUtils.readFileToByteArray(file));
        } finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {

                }
            }

            if(file != null && file.exists() && file.isFile()){
                file.delete();
            }
        }
    }

    /**
     * 创建产品收款报表数据源
     * @return
     */
    private JRDataSource createProductCollectingReportsDataSource(TProductOrderSearch tProductOrderSearch) {
        DRDataSource dataSource = new DRDataSource("product_name","total_price");
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<List<Map>> remoteModelResult =  tProductOrderApi.productCollectingReports(ctx,tProductOrderSearch);
        if(remoteModelResult.isSuccess()){
            List<Map> list = remoteModelResult.getModel();
            if(CollectionUtils.isNotEmpty(list)){
                for (Map map : list) {
                    BigDecimal totalPrice = new BigDecimal(0.00);
                    if(map.containsKey("total_price")){
                        totalPrice = new BigDecimal(Double.parseDouble(map.get("total_price").toString())).setScale(2,BigDecimal.ROUND_HALF_UP);
                    }
                    dataSource.add(map.get("product_name"),totalPrice);
                }
            }
        }
        return dataSource;
    }





    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Product,businessName="导出产品收款报表（流水）",operationType= OperationEnum.Export)
    @RequestMapping(value = "/exportProductCollectingSerialNumberReports",method = RequestMethod.POST)
    public @ResponseBody
    MessageMap exportProductCollectingSerialNumberReports(@RequestBody TProductOrderSearch tProductOrderSearch) {
        MessageMap mm = new MessageMap();
        try {
            exportProductCollectingSerialNumberReportsToXls(tProductOrderSearch);
        } catch (FileNotFoundException e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("导出失败："+e.getMessage());
        } catch (DRException e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("导出失败："+e.getMessage());
        } catch (IOException e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("导出失败："+e.getMessage());
        } catch (Exception e) {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage("导出失败："+e.getMessage());
        }
        return mm;
    }

    private void exportProductCollectingSerialNumberReportsToXls(TProductOrderSearch tProductOrderSearch) throws Exception {
        WyBusinessContext ctx = WyBusinessContext.getContext();
        TSysProjectSearch tSysProjectSearch = new TSysProjectSearch();
        tSysProjectSearch.setLoginName(ctx.getLoginName());
        if(StringUtils.isNotBlank(tProductOrderSearch.getProjectId())){
            tSysProjectSearch.setProjectId(tProductOrderSearch.getProjectId());
        }

        TSysProjectList tSysProjectList = null;
        RemoteModelResult<BaseDto> remoteModelResult = tSysProjectApi.findByCondition(ctx,tSysProjectSearch);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto = remoteModelResult.getModel();
            List<TSysProjectList> tSysProjectLists = baseDto.getLstDto();
            if(CollectionUtils.isNotEmpty(tSysProjectLists)){
                tSysProjectList = tSysProjectLists.get(0);
            }
        }


        StyleBuilder boldCenteredStyle = stl.style().setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle).setBorder(stl.penThin()).setBackgroundColor(Color.LIGHT_GRAY);
        StyleBuilder columnStyle  = stl.style(boldCenteredStyle).setBorder(stl.penThin()).setTextAlignment(HorizontalTextAlignment.LEFT, VerticalTextAlignment.MIDDLE);


        File file = null;
        String prefix = UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
        String suffix = ".xls";
        String path = CreateFileUtil.createTempFile(prefix,suffix,null);
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(path));
            tProductOrderSearch.setProjectId(tSysProjectList.getProjectId());
            report()
                    .ignorePageWidth()
                    .setColumnTitleStyle(columnTitleStyle)
                    .setColumnStyle(columnStyle)
                    .highlightDetailEvenRows()
                    .columns(
                            col.column("序号",       "index",      type.integerType()).setStyle(stl.style(boldCenteredStyle).setBorder(stl.penThin()).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE)),
                            col.column("交易单号",   "batch_no",  type.stringType()).setWidth(200),
                            col.column("时间", "create_time", type.dateType()).setWidth(100),
                            col.column("项目", "item", type.stringType()).setWidth(300),
                            col.column("金额", "total_price", type.bigDecimalType()),
                            col.column("付款人", "buyer", type.stringType()),
                            col.column("房屋编码", "buyer_house_code", type.stringType()),
                            col.column("房屋地址", "buyer_house", type.stringType()).setWidth(300),
                            col.column("操作人", "creater_name", type.stringType()),
                            col.column("付款方式", "pay_type_name", type.stringType())
                    )
                    .ignorePagination()
                    .title(
                            cmp.text("产品收款报表（流水）").setStyle(boldCenteredStyle),
                            cmp.horizontalFlowList().add(cmp.text("项目：" + tSysProjectList.getName()).setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                                    .newRow()
                                    .add(cmp.text("日期从：" + tProductOrderSearch.getBeginTime()).setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                                    .newRow()
                                    .add(cmp.text("日期至：" + tProductOrderSearch.getEndTime()).setStyle(columnTitleStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT))
                    )
                    .setDataSource(createProductCollectingSerialNumberReportsDataSource(tProductOrderSearch))
                    .toXls(outputStream);

            file = new File(path);
            springRedisTools.addData(String.format(WyEnum.download_redis_file_by_loginname_key.getStringValue(),ctx.getLoginName()), FileUtils.readFileToByteArray(file));
        } finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {

                }
            }

            if(file != null && file.exists() && file.isFile()){
                file.delete();
            }
        }
    }

    /**
     * 创建产品收款报表（流水）数据源
     * @return
     */
    private JRDataSource createProductCollectingSerialNumberReportsDataSource(TProductOrderSearch tProductOrderSearch) {
        DRDataSource dataSource = new DRDataSource("index", "batch_no", "create_time","item","total_price","buyer","buyer_house_code","buyer_house","creater_name","pay_type_name");
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<List<Map>> remoteModelResult =  tProductOrderApi.productCollectingSerialNumberReports(ctx,tProductOrderSearch);
        if(remoteModelResult.isSuccess()){
            List<Map> list = remoteModelResult.getModel();
            if(CollectionUtils.isNotEmpty(list)){
                int index = 1;
                String buyer = "";
                for (Map map : list) {
                    buyer = map.get("buyer_common").toString().split("<br/>")[0].split("：")[1];

                    BigDecimal totalPrice = new BigDecimal(0.00);
                    if(map.containsKey("total_price")){
                        totalPrice = new BigDecimal(Double.parseDouble(map.get("total_price").toString())).setScale(2,BigDecimal.ROUND_HALF_UP);
                    }

                    dataSource.add(index ++,map.get("batch_no"),map.get("create_time"),map.get("product_name"),totalPrice,buyer,map.get("house_code"),map.get("building_full_name"),map.get("creater_name"),map.get("pay_type_name"));
                }
            }
        }
        return dataSource;
    }

}
