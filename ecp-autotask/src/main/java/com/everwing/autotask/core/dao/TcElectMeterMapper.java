package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface TcElectMeterMapper {

    ElectMeter getElectMeterByReationId(String projectId, String reationId);

    ElectMeter findMByBuildCodeAndProjectId(String projectId,String buildCode);

    ElectMeter getElectMeterByCodeAndM(String code,String projectId);

    ElectMeter findMByCCodeAndProjectId(String code,String projectId);

    ElectMeter getElectMeterByCode(String code,String projectId);

    List<String> findByparentCodeAndProjectId(String parentCode,String projectId);
}
