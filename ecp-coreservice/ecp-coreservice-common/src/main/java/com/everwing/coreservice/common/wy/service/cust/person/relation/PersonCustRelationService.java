package com.everwing.coreservice.common.wy.service.cust.person.relation;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelation;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelationSearch;

public interface PersonCustRelationService {
    BaseDto listPagePersonCustRelation(WyBusinessContext ctx, PersonCustRelationSearch PersonCustRelationSearch);

    MessageMap delete(WyBusinessContext ctx, String id);

    MessageMap insert(WyBusinessContext ctx, PersonCustRelation personCustRelation);

    BaseDto getPersonCustRelationByID(WyBusinessContext ctx, String id);
}
