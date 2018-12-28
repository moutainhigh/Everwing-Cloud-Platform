package com.everwing.server.wy.web.controller.cust.person.relation;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.BaseDtoUtils;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelation;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelationSearch;
import com.everwing.coreservice.wy.api.cust.person.relation.PersonCustRelationApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value="/PersonCustRelation")
public class PersonCustRelationController {
    private static final Logger LOG = LoggerFactory.getLogger(PersonCustRelationController.class);

    @Autowired
    @Qualifier("personCustRelationApi")
    private PersonCustRelationApi personCustRelationApi;//控制层调用api接口

    @SuppressWarnings("rawtypes")
    @RequestMapping(value="/listPagePersonCustRelation",method= RequestMethod.POST)
    @ResponseBody
    public BaseDto listPersonCustPhonePageNew(HttpServletRequest req, @RequestBody PersonCustRelationSearch PersonCustRelationSearch){
        WyBusinessContext ctx = WyBusinessContext.getContext();
        return BaseDtoUtils.getDto(this.personCustRelationApi.listPagePersonCustRelation(ctx,PersonCustRelationSearch));
    }


    @SuppressWarnings("rawtypes")
    @RequestMapping(value="/getPersonCustRelationByID/{custId}",method= RequestMethod.GET)
    @ResponseBody
    public BaseDto loadPersonRelationByID(HttpServletRequest req, @PathVariable String custId ){
        BaseDto baseDto = new BaseDto();
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult<BaseDto> result = personCustRelationApi.getPersonCustRelationByID(ctx, custId);
        if (result.isSuccess()) {
            baseDto = result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        baseDto.setMessageMap(mm);
        return baseDto;
    }


    @RequestMapping(value="/delete/{id}",method= RequestMethod.GET)
    @ResponseBody
    public MessageMap delete(HttpServletRequest req, @PathVariable String id){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult result = this.personCustRelationApi.delete(ctx,id);
        if (result.isSuccess()) {
            mm = result.getModel() == null ? null : (MessageMap) result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }


    @RequestMapping(value="/insert",method= RequestMethod.POST)
    @ResponseBody
    public MessageMap insert(HttpServletRequest req, @RequestBody PersonCustRelation personCustRelation){
        MessageMap mm = new MessageMap();
        WyBusinessContext ctx = WyBusinessContext.getContext();
        RemoteModelResult result = this.personCustRelationApi.insert(ctx,personCustRelation);
        if (result.isSuccess()) {
            mm = result.getModel() == null ? null : (MessageMap) result.getModel();
        } else {
            mm.setFlag(MessageMap.INFOR_WARNING);
            mm.setMessage(result.getMsg());
        }
        return mm;
    }

}
