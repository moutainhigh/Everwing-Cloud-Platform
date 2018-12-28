package com.everwing.coreservice.wy.core.service.impl.TcPublicAsset;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.operation.Operationlog;
import com.everwing.coreservice.common.wy.entity.personbuilding.PersonBuildingNew;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAsset;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAssetList;
import com.everwing.coreservice.common.wy.entity.property.PublicAsset.TcPublicAssetSearch;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.service.TcPublicAsset.TcPublicAssetService;
import com.everwing.coreservice.wy.dao.mapper.operation.OperationMapper;
import com.everwing.coreservice.wy.dao.mapper.personbuilding.PersonBuildingNewMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcPublicAssetMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("tcPublicAssetServiceImpl")
public class TcPublicAssetServiceImpl  implements TcPublicAssetService {
    @Autowired
    private TcPublicAssetMapper tcPublicAssetMapper ;

    @Autowired
    private PersonBuildingNewMapper personBuildingNewMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Autowired
    private TcBuildingMapper tcBuildingMapper;

    @Override
    public BaseDto loadPublicAssetlistPage(WyBusinessContext ctx, TcPublicAssetSearch tcPublicAssetSearch) {
        List<TcPublicAsset> list =tcPublicAssetMapper.loadPublicAssetlistPage(tcPublicAssetSearch);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcPublicAssetSearch.getPage());
        return baseDto;
    }

    @Override
    public BaseDto loadPublicWaterlistPage(WyBusinessContext ctx, TcPublicAssetSearch tcPublicAssetSearch) {
        List<TcPublicAsset> list =tcPublicAssetMapper.loadPublicWaterlistPage(tcPublicAssetSearch);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcPublicAssetSearch.getPage());
        return baseDto;
    }
    @Override
    public MessageMap InsertAsset(WyBusinessContext ctx, TcPublicAsset tcPublicAsset) {
        MessageMap messageMap = new MessageMap();
        // 先判断是否自持. 两种生成编码不一样
        String projectId = tcPublicAsset.getProjectId();
        if(tcPublicAsset.getIsHold().equals("Y")){
            //自持找出最大的编码
            //不是自持找出最大的编码
            String HouseCodeNew = projectId.substring(projectId.length() - 2) + WyEnum.CODE_TYPE_05.getStringValue();
            String MaxCode = this.tcPublicAssetMapper.SearchMaxCode(tcPublicAsset.getIsHold(),projectId);
            //自动补位
            MaxCode = StringUtils.leftPad(MaxCode, 5, "0");
            tcPublicAsset.setHouseCodeNew(HouseCodeNew + MaxCode);
        }else{
            //不是自持找出最大的编码
            String HouseCodeNew = projectId.substring(projectId.length() - 2) + WyEnum.CODE_TYPE_04.getStringValue();
            String MaxCode = this.tcPublicAssetMapper.SearchMaxCode(tcPublicAsset.getIsHold(),projectId);
            //自动补位
            MaxCode = StringUtils.leftPad(MaxCode, 5, "0");
            tcPublicAsset.setHouseCodeNew(HouseCodeNew + MaxCode);
        }
        String uuid = UUID.randomUUID().toString();
        tcPublicAsset.setId(uuid);
        tcPublicAsset.setBuildingCode(UUID.randomUUID().toString());
        tcPublicAsset.setCreaterId(ctx.getUserId());
        tcPublicAsset.setCreaterName(ctx.getStaffName());
        tcPublicAsset.setModifyId(ctx.getUserId());
        tcPublicAsset.setModifyName(ctx.getStaffName());
        this.tcPublicAssetMapper.insert(tcPublicAsset);


        // 还需往tc_building表插入基本数据

        TcBuilding tcBuilding = new TcBuilding();
        tcBuilding.setId(uuid);
        tcBuilding.setCreaterId(ctx.getUserId());
        tcBuilding.setCreaterName(ctx.getStaffName());
        tcBuilding.setProjectId(ctx.getProjectCode());
        tcBuilding.setCompanyId(ctx.getCompanyId());
        List<TcBuilding> tcBuildingList = new ArrayList<>();
        tcBuildingList.add(tcBuilding);
        tcBuildingMapper.batchInsert(tcBuildingList);




        for(int i = 0 ;i<tcPublicAsset.getBuildingPerson().size();i++){
            tcPublicAsset.getBuildingPerson().get(i).setPersonBuildingId(UUID.randomUUID().toString());
            tcPublicAsset.getBuildingPerson().get(i).setBuildingId(uuid);
        }
        if(tcPublicAsset.getBuildingPerson().size()>0){
            this.personBuildingNewMapper.insertList(tcPublicAsset.getBuildingPerson());
        }


        //添加操作日志
        Operationlog operationlog = new Operationlog();
        operationlog.setBuildingid(tcPublicAsset.getId());
        operationlog.setBuildingFullName(tcPublicAsset.getBuildingFullName());
        operationlog.setAddress(tcPublicAsset.getAddress());
        operationlog.setHouseCodeNew(tcPublicAsset.getHouseCodeNew());
        String dirtyinfo =  StringUtils.join(tcPublicAsset.getDirtyInfo(), ",");
        operationlog.setModifyMatter(dirtyinfo);
        operationlog.setModifyType("insert");
        operationlog.setOperator(ctx.getStaffName());
        this.operationMapper.insert(operationlog);
        return messageMap;
    }

    @Override
    public MessageMap deletePublicAsset(WyBusinessContext ctx, String id) {
        MessageMap messageMap = new MessageMap();

        TcPublicAsset tcPublicAsset = this.tcPublicAssetMapper.selectById(id);

        tcPublicAssetMapper.deletePublicAsset(id);

        Operationlog operationlog = new Operationlog();
        operationlog.setBuildingid(tcPublicAsset.getId());
        operationlog.setBuildingFullName(tcPublicAsset.getBuildingFullName());
        operationlog.setAddress(tcPublicAsset.getAddress());
        operationlog.setHouseCodeNew(tcPublicAsset.getHouseCodeNew());
        String dirtyinfo =  StringUtils.join(tcPublicAsset.getDirtyInfo(), ",");
        operationlog.setModifyMatter(dirtyinfo);
        operationlog.setModifyType("delete");
        operationlog.setOperator(ctx.getStaffName());
        this.operationMapper.insert(operationlog);

        return messageMap;
    }

    @Override
    public MessageMap EditAsset(WyBusinessContext ctx, TcPublicAsset tcPublicAsset) {
        MessageMap messageMap = new MessageMap();
        tcPublicAsset.setModifyId(ctx.getUserId());
        tcPublicAsset.setModifyName(ctx.getStaffName());
        this.tcPublicAssetMapper.EditAsset(tcPublicAsset);
        // 如果是自持就没有产权人
        if(!tcPublicAsset.getIsHold().equals("Y")){
            if(tcPublicAsset.getBuildingPerson().size()>0){
                //在外面定义 里面会删除重复的数据
                //编辑时还要检查有无数据库里有的
                List<PersonBuildingNew> personBuildingNewList = new ArrayList<>();
                for(PersonBuildingNew list : tcPublicAsset.getBuildingPerson()){
                    String CustId = list.getCustId();
                    String EnterpriseId = list.getEnterpriseId();
                    int count = this.personBuildingNewMapper.selectByCustomer(CustId,EnterpriseId,tcPublicAsset.getId());
                    if(count == 0){
                        list.setPersonBuildingId(UUID.randomUUID().toString());
                        list.setBuildingId(tcPublicAsset.getId());
                        personBuildingNewList.add(list);
                    }
                }
                if(personBuildingNewList.size()>0){
                    this.personBuildingNewMapper.insertList(personBuildingNewList);
                }
            }
        }
        //添加操作日志
        Operationlog operationlog = new Operationlog();
        operationlog.setBuildingid(tcPublicAsset.getId());
        operationlog.setBuildingFullName(tcPublicAsset.getBuildingFullName());
        operationlog.setAddress(tcPublicAsset.getAddress());
        operationlog.setHouseCodeNew(tcPublicAsset.getHouseCodeNew());
        String dirtyinfo =  StringUtils.join(tcPublicAsset.getDirtyInfo(), ",");
        operationlog.setModifyMatter(dirtyinfo);
        operationlog.setModifyType("edit");
        operationlog.setOperator(ctx.getStaffName());
        this.operationMapper.insert(operationlog);


        return messageMap;
    }

    @Override
    public BaseDto SearchAsset(WyBusinessContext ctx, String project) {
        List<TcPublicAssetList> list =tcPublicAssetMapper.SearchAsset(project);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        return baseDto;
    }
}
