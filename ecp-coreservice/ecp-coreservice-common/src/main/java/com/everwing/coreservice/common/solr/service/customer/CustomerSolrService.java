package com.everwing.coreservice.common.solr.service.customer;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.solr.entity.customer.*;
import com.everwing.coreservice.common.solr.service.SolrDefaultService;

/**
 * @author shiny
 **/
public interface CustomerSolrService extends SolrDefaultService {

    /**
     * 分页查询
     * @param companyId
     * @param building
     * @return
     */
    BaseDto listPageBuilding(String companyId, BuildingSearch building);


    BaseDto listPagePersonCust(String companyId, PersonCustSearch personCustSearch);

    BaseDto listPageEnterpriseCust(String companyId, EnterpriseCustSearch enterpriseCustSearch);

    BaseDto listPageAsset(String companyId, AssetSearch assetSearch);

    BaseDto listPagePersonConstruction(String companyId, ConstructionSearch constructionSearch);

    BaseDto listPagePublicRental(String companyId, PublicRentalSearch publicRentalSearch);
}
