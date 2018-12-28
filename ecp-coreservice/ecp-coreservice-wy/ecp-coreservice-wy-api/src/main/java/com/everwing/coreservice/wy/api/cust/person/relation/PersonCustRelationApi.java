package com.everwing.coreservice.wy.api.cust.person.relation;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelation;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelationSearch;
import com.everwing.coreservice.common.wy.service.cust.person.relation.PersonCustRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonCustRelationApi {

    @Autowired
    private PersonCustRelationService personCustRelationService;


    public RemoteModelResult<BaseDto> listPagePersonCustRelation(WyBusinessContext ctx, PersonCustRelationSearch PersonCustRelationSearch) {
        return new RemoteModelResult<BaseDto>(this.personCustRelationService.listPagePersonCustRelation(ctx,PersonCustRelationSearch));
    }

    public RemoteModelResult delete(WyBusinessContext ctx, String id) {
        return new RemoteModelResult<MessageMap>(this.personCustRelationService.delete(ctx,id));
    }

    public RemoteModelResult insert(WyBusinessContext ctx, PersonCustRelation personCustRelation) {
        return new RemoteModelResult<MessageMap>(this.personCustRelationService.insert(ctx,personCustRelation));
    }

    public RemoteModelResult<BaseDto> getPersonCustRelationByID(WyBusinessContext ctx, String id) {
        return new RemoteModelResult<BaseDto>(this.personCustRelationService.getPersonCustRelationByID(ctx,id));
    }
}
