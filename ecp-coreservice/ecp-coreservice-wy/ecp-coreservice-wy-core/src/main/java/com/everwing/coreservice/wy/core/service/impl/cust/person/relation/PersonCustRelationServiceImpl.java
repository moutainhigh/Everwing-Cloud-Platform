package com.everwing.coreservice.wy.core.service.impl.cust.person.relation;


import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelation;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelationSearch;
import com.everwing.coreservice.common.wy.service.cust.person.relation.PersonCustRelationService;
import com.everwing.coreservice.wy.dao.mapper.cust.person.relation.PersonCustRelationMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PersonCustRelationServiceImpl")
public class PersonCustRelationServiceImpl implements PersonCustRelationService {
    private static final Logger logger = LogManager.getLogger(PersonCustRelationServiceImpl.class);
    @Autowired
    private PersonCustRelationMapper personCustRelationMapper;


    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public BaseDto listPagePersonCustRelation(WyBusinessContext ctx, PersonCustRelationSearch PersonCustRelationSearch) {
        return new BaseDto(this.personCustRelationMapper.listPagePersonCustRelation(PersonCustRelationSearch));
    }

    @Override
    public MessageMap delete(WyBusinessContext ctx, String id) {
        personCustRelationMapper.delete(id);
         return new MessageMap(null,"删除成功！");
    }

    @Override
    public MessageMap insert(WyBusinessContext ctx, PersonCustRelation personCustRelation) {
        personCustRelation.setCreateId(ctx.getUserId());
        personCustRelation.setCreateName(ctx.getStaffName());
        personCustRelationMapper.insert(personCustRelation);
        return new MessageMap(null,"删除成功！");
    }

    @Override
    public BaseDto getPersonCustRelationByID(WyBusinessContext ctx, String id) {
        return new BaseDto(this.personCustRelationMapper.getPersonCustRelationByID(id));
    }
}
