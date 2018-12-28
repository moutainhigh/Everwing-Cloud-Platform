package com.everwing.coreservice.platform.dao.mapper.generated;


import com.everwing.coreservice.common.platform.entity.generated.AccountInformation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface InformationMapper {
    @Select("select name name,card_num id,mobile mobile from account_Information where account_code = #{accountCode}")
    List<Map<String,String>> getIdentity(@Param("accountCode") String accountCode);

    @Insert("insert into account_Information (`id`, `account_id`,`account_code`, `card_num`, `name`, `mobile`, `create_time`) VALUES (UUID(),#{accountid},#{accountCode},#{cardnum},#{name},#{mobile},now())")
    int setIdentity(AccountInformation AccountInformation);

    @Select("select count(1) from account_Information where account_id = #{accountId}")
    int checkExists(@Param("accountId") String accountId);
}