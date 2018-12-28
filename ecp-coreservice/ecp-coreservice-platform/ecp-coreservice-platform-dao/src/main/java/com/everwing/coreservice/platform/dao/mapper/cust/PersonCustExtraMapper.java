package com.everwing.coreservice.platform.dao.mapper.cust;

import com.everwing.coreservice.common.admin.entity.cust.PersonCust;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PersonCustExtraMapper {

	/**
	 * info: 调用的时候,只有增删改查以及批量增删七个接口
	 */

	public List<PersonCust> getPersonCustList(PersonCust cust);

	public PersonCust getPersonCustById(PersonCust cust);

	public int insert(PersonCust cust) throws DataAccessException;

	public int update(PersonCust cust) throws DataAccessException;

	public int delete(PersonCust cust) throws DataAccessException;

	public int batchInsert(List<PersonCust> custs) throws DataAccessException;

	public int batchDelete(List<String> ids) throws DataAccessException;

}
