package com.everwing.server.linphone.controller;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.platform.annotation.ApiVersion;
import com.everwing.coreservice.common.wy.entity.property.house.TcHouseList;
import com.everwing.coreservice.common.wy.entity.system.project.TSysProjectSearch;
import com.everwing.coreservice.platform.api.BuildingApi;
import com.everwing.coreservice.wy.api.building.TcBuildingApi;
import com.everwing.coreservice.wy.api.sys.TSysProjectApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/{version}/assets")
public class BuildingController {

    @Autowired
    private BuildingApi buildingApi;

    @Autowired
    private TcBuildingApi tcBuildingApi;

    @Autowired
    private TSysProjectApi tSysProjectApi;

    @PostMapping("queryByMobile")
    @ApiVersion(1.0)
    public LinphoneResult queryByMobile(String accountId, String mobile){
        return buildingApi.getByMobile(accountId,mobile);
    }

    @PostMapping("getInfoByBuildingId")
    @ApiVersion(1.0)
    public LinphoneResult getInfoByBuildingId(String buildingId){
        RemoteModelResult result=buildingApi.getByBuildingId(buildingId);
        if(result.isSuccess()){
            Map<String,Object> map= (Map<String, Object>) result.getModel();
            String buildingCode= (String) map.get("buildingCode");
            String companyId= (String) map.get("companyId");
            String projectId= (String) map.get("projectId");
//            RemoteModelResult<TcHouseList> modelResult=tcBuildingApi.findTcHouseByBuildingCode(companyId,buildingCode);
//            TSysProjectSearch tSysProjectSearch=new TSysProjectSearch();
//            tSysProjectSearch.setProjectId(projectId);
////            RemoteModelResult<BaseDto> projectResult=tSysProjectApi.findByCondtion(companyId,tSysProjectSearch);
//            if(modelResult.isSuccess()){
//                TcHouseList houseList=modelResult.getModel();
//                if(houseList!=null) {
//                    if (houseList.getHouseTypeName() != null) {
//                        map.put("houseType", houseList.getHouseTypeName());
//                    }
//                    if (houseList.getHousePropertyName() != null) {
//                        map.put("buildingAttributes", houseList.getHousePropertyName());
//                    }
//                }
//            }
//            if(projectResult.isSuccess()){
//                TSysProject project= ((List<TSysProject>)projectResult.getModel().getLstDto()).get(0);
//                map.put("projectName",project.getName());
//            }
            return new LinphoneResult(map);
        }
        return new LinphoneResult(ReturnCode.API_RESOLVE_FAIL);
    }
}
