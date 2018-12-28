package com.everwing.server.wy.web.controller.product;/**
 * Created by wust on 2017/12/8.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.product.TDepositDetail;
import com.everwing.coreservice.common.wy.entity.product.TDepositDetailList;
import com.everwing.coreservice.common.wy.entity.product.TDepositDetailSearch;
import com.everwing.coreservice.common.wy.entity.product.TDepositSearch;
import com.everwing.coreservice.wy.api.product.TDepositApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * Function:
 * Reason:
 * Date:2017/12/8
 * @author wusongti@lii.com.cn
 */
@Controller
@RequestMapping("/TDepositController")
public class TDepositController {
    @Autowired
    private TDepositApi tDepositApi;

    @WyOperationLogAnnotation(moduleName= OperationEnum.Module_Deposit,businessName="分页查询押金",operationType= OperationEnum.Search)
    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto listPage(@RequestBody TDepositSearch tDepositSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<BaseDto> result = tDepositApi.listPage(ctx,tDepositSearch);
        if(result.isSuccess()){
            baseDto = result.getModel();
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 查询退押明细
     * @param tDepositDetailSearch
     * @return
     */
    @RequestMapping(value = "/findTDepositDetailByCondition",method = RequestMethod.POST)
    public @ResponseBody
    BaseDto findTDepositDetailByCondition(@RequestBody TDepositDetailSearch tDepositDetailSearch) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult<List<TDepositDetailList>> result = tDepositApi.findTDepositDetailByCondition(ctx,tDepositDetailSearch);
        if(result.isSuccess()){
            baseDto.setLstDto(result.getModel());
        }else{
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

}
