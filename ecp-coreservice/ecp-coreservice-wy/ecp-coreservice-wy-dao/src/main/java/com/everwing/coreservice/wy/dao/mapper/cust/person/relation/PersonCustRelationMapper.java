package com.everwing.coreservice.wy.dao.mapper.cust.person.relation;

import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelation;
import com.everwing.coreservice.common.wy.entity.cust.person.relation.PersonCustRelationSearch;

import java.util.List;

public interface PersonCustRelationMapper {
    List<PersonCustRelation> listPagePersonCustRelation(PersonCustRelationSearch PersonCustRelationSearch);

    List<PersonCustRelation> getPersonCustRelationByID(String custid);

    void delete(String id);

    void insert(PersonCustRelation personCustRelation);

    int getRelationByCustcode(String custId, String custid);

}
