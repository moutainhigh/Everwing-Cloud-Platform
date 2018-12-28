package com.everwing.coreservice.wy.dao.mapper.cust;

import com.everwing.coreservice.common.wy.dto.TBcCollectionDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 托收关联查询mapper
 *
 * @author DELL shiny
 * @create 2017/9/20
 */
@Repository
public interface CollectionExtraMapper {

    @Select("SELECT t.id, t.cust_id custId, t.cust_name custName, t.create_bank createBank,t1.bank_name createBankName," +
            "  t.card_num cardNum, t.province, t.city, t.start_time startTime, t.contract_no contractNo," +
            "  t.relate_building_code relateBuildingCode, t.charging_items chargingItems, t.attachment," +
            "  t.relate_building_full_name relateBuildingFullName, t.status,t.project_id,replace(replace(replace(replace(t.charging_items,'1','物业'),'2','本体基金'),'3','水费'),'4','电费') itemNames" +
            "  FROM t_bc_collection t LEFT JOIN t_bank_info t1 ON t.create_bank=t1.id where t.relate_building_code=#{relateBuildingCode}")
    List<TBcCollectionDto> listPageByBuildingCode(TBcCollectionDto tBcCollectionDto);

    @Select("SELECT INTE_ARRAY(" +
            "(SELECT group_concat(charging_items) FROM t_bc_collection WHERE relate_building_code = #{buildingCode} and status=1), #{chargingItems})")
    int checkExists(@Param("buildingCode") String buildingCode, @Param("chargingItems") String chargingItems);

    @Update("update t_bc_collection set status=0,update_time=now() where cust_id=#{custId}")
    int updateStatusByCustIdAndBuildingCode(@Param("custId") String custId,@Param("buildingCode")String buildingCode);
}
