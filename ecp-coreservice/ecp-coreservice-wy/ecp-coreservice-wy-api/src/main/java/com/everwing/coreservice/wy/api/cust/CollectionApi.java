package com.everwing.coreservice.wy.api.cust;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.dto.TBcCollectionDto;
import com.everwing.coreservice.common.wy.entity.cust.TBcCollection;
import com.everwing.coreservice.common.wy.service.cust.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 银行托收api
 *
 * @author DELL shiny
 * @create 2017/9/14
 */
@Component
public class CollectionApi {

    @Autowired
    private CollectionService collectionService;

    public RemoteModelResult<BaseDto> loadCollectionListByBuildingCode(TBcCollectionDto tBcCollectionDto){
        return new RemoteModelResult<>(collectionService.queryListByBuildingCode(CommonUtils.getCompanyIdByCurrRequest(),tBcCollectionDto));
    }

    public RemoteModelResult<BaseDto> loadBanksByProjectId(String projectId) {
        return new RemoteModelResult<>(collectionService.queryBanksByProjectId(CommonUtils.getCompanyIdByCurrRequest(),projectId));
    }

    public RemoteModelResult<BaseDto> loadChargingItems(String projectId) {
        return new RemoteModelResult<>(collectionService.queryChargingItemsByProjectId(CommonUtils.getCompanyIdByCurrRequest(),projectId));
    }

    public RemoteModelResult<BaseDto> add(TBcCollection tBcCollection) {
        return new RemoteModelResult<>(collectionService.insert(CommonUtils.getCompanyIdByCurrRequest(),tBcCollection));
    }

    public RemoteModelResult update(TBcCollection tBcCollection) {
        return new RemoteModelResult<>(collectionService.update(CommonUtils.getCompanyIdByCurrRequest(),tBcCollection));
    }

    public RemoteModelResult batchDelete(String ids) {
        return new RemoteModelResult<>(collectionService.batchDelete(CommonUtils.getCompanyIdByCurrRequest(),ids));
    }

    public RemoteModelResult<BaseDto> batchEffective(String ids) {
        return new RemoteModelResult<>(collectionService.batchEffective(CommonUtils.getCompanyIdByCurrRequest(),ids));
    }

    public RemoteModelResult<BaseDto> batchUnEffective(String ids) {
        return new RemoteModelResult<>(collectionService.batchUnEffective(CommonUtils.getCompanyIdByCurrRequest(),ids));
    }
}
