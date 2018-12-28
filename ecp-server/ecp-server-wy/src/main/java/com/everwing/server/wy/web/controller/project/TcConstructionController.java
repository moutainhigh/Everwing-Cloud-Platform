package com.everwing.server.wy.web.controller.project;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.annotations.WyOperationLogAnnotation;
import com.everwing.coreservice.common.wy.common.enums.OperationEnum;
import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstruction;
import com.everwing.coreservice.common.wy.entity.property.Engineering.TcConstructionSearch;
import com.everwing.coreservice.wy.api.building.TcConstructionApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @ClassName: TcConstructionController
 * @Author:Ck
 * @Date: 2018/7/31
 **/

@Controller
@RequestMapping("/project/TcConstructionController")
public class TcConstructionController {
    @Autowired
    private TcConstructionApi tcConstructionApi;


    /**
     * 查询工程施工
     *
     * @param tcConstruction
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "loadbyConstructionlistPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadbyConstructionlistPage(@RequestBody TcConstructionSearch tcConstruction) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        RemoteModelResult<BaseDto> result = tcConstructionApi.loadbyConstructionlistPage(WyBusinessContext.getContext(), tcConstruction);
        if (!result.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        } else {
            baseDto = result.getModel();
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 查询未关联水电工程施工
     *
     * @param tcConstruction
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "loadbyWaterElectlistPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadbyWaterElectlistPage(@RequestBody TcConstructionSearch tcConstruction) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        RemoteModelResult<BaseDto> result = tcConstructionApi.loadbyWaterElectlistPage(WyBusinessContext.getContext(), tcConstruction);
        if (!result.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        } else {
            baseDto = result.getModel();
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }

    /**
     * 查询历史施工
     *
     * @param tcConstruction
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "loadbyEhistorylistPage", method = RequestMethod.POST)
    public @ResponseBody
    BaseDto loadbyEhistorylistPage(@RequestBody TcConstructionSearch tcConstruction) {
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        RemoteModelResult<BaseDto> result = tcConstructionApi.loadbyEhistorylistPage(WyBusinessContext.getContext(), tcConstruction);
        if (!result.isSuccess()) {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        } else {
            baseDto = result.getModel();
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }




    /**
     * 删除工程施工信息
     */
    @RequestMapping(value = "deleteConstruction/{Id}", method = RequestMethod.GET)
    public @ResponseBody
    MessageMap deleteThird(HttpServletRequest request, @PathVariable String Id) throws Exception {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();

        RemoteModelResult result = tcConstructionApi.delete(ctx, Id);
        if (result.isSuccess()) {
            mm = result.getModel() == null ? null : (MessageMap) result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    /**
     * 修改工程施工信息
     *
     * @param tcConstruction   reviseComplete
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "修改工程施工信息", operationType = OperationEnum.Modify)
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap modify(@RequestBody TcConstruction tcConstruction) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        tcConstruction.setModifyId(ctx.getUserId());
        tcConstruction.setModifyName(ctx.getStaffName());
        RemoteModelResult result = tcConstructionApi.modify(ctx, tcConstruction);
        if (result.isSuccess()) {
            return mm;
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    /**
     * 修改状态为完工
     *
     * @param tcConstruction
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "修改状态为完工", operationType = OperationEnum.Modify)
    @RequestMapping(value = "/reviseComplete", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap reviseComplete(@RequestBody TcConstruction tcConstruction) {
        MessageMap mm = new MessageMap();
        WyBusinessContext  ctx = WyBusinessContext .getContext();
        tcConstruction.setModifyId(ctx.getUserId());
        tcConstruction.setModifyName(ctx.getStaffName());
        RemoteModelResult result = tcConstructionApi.reviseComplete(ctx, tcConstruction);
        if (result.isSuccess()) {
            return mm;
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }




    /**
     * 修改状态为暂停
     *
     * @param tcConstruction
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "修改状态为暂停", operationType = OperationEnum.Modify)
    @RequestMapping(value = "/suspend", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap suspend(@RequestBody TcConstruction tcConstruction) {
        MessageMap mm = new MessageMap();
        WyBusinessContext  ctx = WyBusinessContext .getContext();
        tcConstruction.setModifyId(ctx.getUserId());
        tcConstruction.setModifyName(ctx.getStaffName());
        RemoteModelResult result = tcConstructionApi.suspend(ctx, tcConstruction);
        if (result.isSuccess()) {
            return mm;
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    /**
     * 修改状态为施工中
     *
     * @param tcConstruction
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "修改状态为施工中", operationType = OperationEnum.Modify)
    @RequestMapping(value = "/startUp", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap startUp(@RequestBody TcConstruction tcConstruction) {
        MessageMap mm = new MessageMap();
        WyBusinessContext  ctx = WyBusinessContext .getContext();
        tcConstruction.setModifyId(ctx.getUserId());
        tcConstruction.setModifyName(ctx.getStaffName());
        RemoteModelResult result = tcConstructionApi.startUp(ctx, tcConstruction);
        if (result.isSuccess()) {
            return mm;
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }

    /**
     * 修改延期时间
     *
     * @param tcConstruction
     * @return
     */
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "修改延期时间", operationType = OperationEnum.Modify)
    @RequestMapping(value = "/editDelay", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap editDelay(@RequestBody TcConstruction tcConstruction) {
        MessageMap mm = new MessageMap();
        WyBusinessContext  ctx = WyBusinessContext .getContext();
        tcConstruction.setModifyId(ctx.getUserId());
        tcConstruction.setModifyName(ctx.getStaffName());
        RemoteModelResult result = tcConstructionApi.editDelay(ctx, tcConstruction);
        if (result.isSuccess()) {
            return mm;
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }
    @WyOperationLogAnnotation(moduleName = OperationEnum.Module_Building, businessName = "新增工程施工信息", operationType = OperationEnum.Insert)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody
    MessageMap add(@RequestBody TcConstruction tcConstruction) {
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        tcConstruction.setCreaterId(ctx.getUserId());
        tcConstruction.setCreaterName(ctx.getStaffName());
        RemoteModelResult<MessageMap> result = tcConstructionApi.add(ctx, tcConstruction);
        if (result.isSuccess()) {
            mm = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_ERROR);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


}
