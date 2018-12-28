package com.everwing.server.wy.web.controller.newaccount;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.solr.entity.customer.*;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.wy.fee.dto.*;
import com.everwing.coreservice.solr.api.customer.CustomerSolrApi;
import com.everwing.coreservice.wy.fee.api.AcAccountPageApi;
import com.everwing.server.wy.utils.ReportUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 新账户
 *
 * @author DELL shiny
 * @create 2018/6/21
 */
@RestController
@RequestMapping("newAccount")
public class AccountPageController {

    private static final Logger logger= LogManager.getLogger(AccountPageController.class);

    @Autowired
    private AcAccountPageApi acAccountPageApi;

    @Autowired
    private CustomerSolrApi customerSolrApi;

    @PostMapping("listPageByHouseCode")
    public BaseDto listPageByBuildingCode(@RequestBody BuildingInfoDto buildingInfoDto){
        return BaseDtoUtils.getDto(acAccountPageApi.loadBuildingInfoByBuildingCode(buildingInfoDto));
    }

    @PostMapping("listPageByCustId")
    public BaseDto listPageByCustId(@RequestBody BuildingInfoDto buildingInfoDto){
        return BaseDtoUtils.getDto(acAccountPageApi.loadBuildingInfoByCustId(buildingInfoDto));
    }

    @GetMapping("loadBillInfo/{houseCode}/{year}")
    public BaseDto loadBillInfo(@PathVariable("houseCode") String houseCode,@PathVariable("year") String year){
        return BaseDtoUtils.getDto(acAccountPageApi.loadBillInfoByHouseCodeAndYear(houseCode, year));
    }

    @PostMapping("loadAccountInfoByBuildingCode")
    public BaseDto loadAccountInfoByBuildingCode(@RequestBody BuildingInfoDto buildingInfoDto){
        return BaseDtoUtils.getDto(acAccountPageApi.loadAccountInfoByBuildingCode(buildingInfoDto.getBuildingCode()));
    }

    @PostMapping("listCurrentChargeDetail")
    public BaseDto listCurrentChargeDetail(@RequestBody OrderDetailInfoDto orderDetailInfoDto){
        return BaseDtoUtils.getDto(acAccountPageApi.listCurrentChargeDetail(orderDetailInfoDto));
    }

    @PostMapping("listLateFee")
    public BaseDto listLateFee(@RequestBody LateFeeInfoDto lateFeeInfoDto){
        return BaseDtoUtils.getDto(acAccountPageApi.listLateFee(lateFeeInfoDto));
    }

    @PostMapping("listPrestoreDetail")
    public BaseDto listPrestoreDetail(@RequestBody PreStoreInfoDto preStoreInfoDto){
        return BaseDtoUtils.getDto(acAccountPageApi.listPrestoreDetail(preStoreInfoDto));
    }

    @PostMapping("listBusinessOperaDetail")
    public BaseDto listBusinessOperaDetail(@RequestBody BusinessOperaDetailDto businessOperaDetailDto){
        logger.debug("分页加载业务操作明细,参数:{}",businessOperaDetailDto);
        return BaseDtoUtils.getDto(acAccountPageApi.listBusinessOperaDetail(businessOperaDetailDto));
    }

    @RequestMapping("downLoadBill")
    public void downLoadBill(HttpServletResponse response, String id){
        RemoteModelResult remoteModelResult=acAccountPageApi.downLoadBill(id);
        if(remoteModelResult.isSuccess()){
            BaseDto baseDto=BaseDtoUtils.getDto(remoteModelResult);
            BillDetailDto billDetailDto= (BillDetailDto) baseDto.getObj();
            try {
                File jasperFile = ResourceUtils.getFile("classpath:pdf/billFee.jasper");
                List<BillDetailDto> list=new ArrayList(1);
                list.add(billDetailDto);
                byte[] bytes= ReportUtils.generatePDF(jasperFile.getAbsolutePath(),list);
                response.getOutputStream().write(bytes);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping("searchBuilding")
    public BaseDto searchBuilding(@RequestBody BuildingSearch buildingSearch){
        String companyId=WyBusinessContext.getContext().getCompanyId();
        RemoteModelResult<BaseDto> remoteModelResult=customerSolrApi.listPageBuilding(companyId,buildingSearch);
        if(remoteModelResult.isSuccess()){
            return remoteModelResult.getModel();
        }else{
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,remoteModelResult.getMsg()));
        }
    }

    @RequestMapping("searchPersonCust")
    public BaseDto searchPersonCust(@RequestBody PersonCustSearch personCustSearch){
        String companyId=WyBusinessContext.getContext().getCompanyId();
        RemoteModelResult<BaseDto> remoteModelResult=customerSolrApi.listPagePersonCust(companyId,personCustSearch);
        if(remoteModelResult.isSuccess()){
            return remoteModelResult.getModel();
        }else{
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,remoteModelResult.getMsg()));
        }
    }

    @RequestMapping("searchEnterpriseCust")
    public BaseDto searchEnterpriseCust(@RequestBody EnterpriseCustSearch enterpriseCustSearch){
        String companyId=WyBusinessContext.getContext().getCompanyId();
        RemoteModelResult<BaseDto> remoteModelResult=customerSolrApi.listPageEnterpriseCust(companyId,enterpriseCustSearch);
        if(remoteModelResult.isSuccess()){
            return remoteModelResult.getModel();
        }else{
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,remoteModelResult.getMsg()));
        }
    }

    @RequestMapping("searchAsset")
    public BaseDto searchAsset(@RequestBody AssetSearch assetSearch){
        String companyId=WyBusinessContext.getContext().getCompanyId();
        RemoteModelResult<BaseDto> remoteModelResult=customerSolrApi.listPageAsset(companyId,assetSearch);
        if(remoteModelResult.isSuccess()){
            return remoteModelResult.getModel();
        }else{
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,remoteModelResult.getMsg()));
        }
    }

    @RequestMapping("searchConstruction")
    public BaseDto searchConstruction(@RequestBody ConstructionSearch constructionSearch){
        String companyId=WyBusinessContext.getContext().getCompanyId();
        RemoteModelResult<BaseDto> remoteModelResult=customerSolrApi.listPageConstruction(companyId,constructionSearch);
        if(remoteModelResult.isSuccess()){
            return remoteModelResult.getModel();
        }else{
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,remoteModelResult.getMsg()));
        }
    }

    @RequestMapping("searchPublicRental")
    public BaseDto searchPublicRental(@RequestBody PublicRentalSearch publicRentalSearch){
        String companyId=WyBusinessContext.getContext().getCompanyId();
        RemoteModelResult<BaseDto> remoteModelResult=customerSolrApi.listPagePublicRental(companyId,publicRentalSearch);
        if(remoteModelResult.isSuccess()){
            return remoteModelResult.getModel();
        }else{
            return new BaseDto(new MessageMap(MessageMap.INFOR_ERROR,remoteModelResult.getMsg()));
        }
    }
}
