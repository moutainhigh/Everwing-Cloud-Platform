package com.everwing.coreservice.platform.dao.mapper.cust.personbuilding;

import com.everwing.coreservice.common.admin.entity.cust.personbuilding.PersonBuilding;

import java.util.List;
import java.util.Map;

public abstract interface PersonBuildingExtraMapper
{
  public abstract int add(PersonBuilding paramPersonBuilding);

  public abstract int del(PersonBuilding paramPersonBuilding);

  public abstract int batchAdd(List<PersonBuilding> paramList);

  public abstract int batchDel(Map<String, Object> paramMap);
}