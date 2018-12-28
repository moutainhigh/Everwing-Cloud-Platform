package com.everwing.coreservice.solr.api.customer;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.solr.entity.customer.*;
import com.everwing.coreservice.common.solr.service.customer.CustomerSolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shiny
 **/
@Component
public class CustomerSolrApi {

    @Autowired
    private CustomerSolrService customerSolrService;

    public RemoteModelResult<BaseDto> listPageBuilding(String companyId, BuildingSearch buildingSearch){
        return new RemoteModelResult<>(customerSolrService.listPageBuilding(companyId,buildingSearch));
    }

    public RemoteModelResult<BaseDto> listPagePersonCust(String companyId, PersonCustSearch personCustSearch) {
        return new RemoteModelResult<>(customerSolrService.listPagePersonCust(companyId,personCustSearch));
    }

    public RemoteModelResult<BaseDto> listPageEnterpriseCust(String companyId, EnterpriseCustSearch enterpriseCustSearch) {
        return new RemoteModelResult<>(customerSolrService.listPageEnterpriseCust(companyId,enterpriseCustSearch));
    }

    public RemoteModelResult<BaseDto> listPageAsset(String companyId, AssetSearch assetSearch) {
        return new RemoteModelResult<>(customerSolrService.listPageAsset(companyId,assetSearch));
    }

    public RemoteModelResult<BaseDto> listPageConstruction(String companyId, ConstructionSearch constructionSearch) {
        return new RemoteModelResult<>(customerSolrService.listPagePersonConstruction(companyId,constructionSearch));
    }

    public RemoteModelResult<BaseDto> listPagePublicRental(String companyId, PublicRentalSearch publicRentalSearch) {
        return new RemoteModelResult<>(customerSolrService.listPagePublicRental(companyId,publicRentalSearch));
    }
}
