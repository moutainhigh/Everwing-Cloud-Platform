package com.everwing.coreservice.wy.core.service.impl.TcSurfaceElect;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeter;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectMeterSearch;
import com.everwing.coreservice.common.wy.entity.business.electmeter.TcElectlog;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcWaterMeter;
import com.everwing.coreservice.common.wy.entity.property.building.TcBuilding;
import com.everwing.coreservice.common.wy.service.TcSurfaceElectService.TcSurfaceElectService;
import com.everwing.coreservice.wy.dao.mapper.annex.AnnexMapper;
import com.everwing.coreservice.wy.dao.mapper.materlog.ElectlogMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcBuildingMapper;
import com.everwing.coreservice.wy.dao.mapper.property.TcSurfaceElectMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("tcSurfaceElectSericeImpl")
public class TcSurfaceElectSericeImpl implements TcSurfaceElectService{

    @Autowired
    private TcSurfaceElectMapper  tcSurfaceElectMapper;

    @Autowired
    private TcBuildingMapper TcBuildingMapper;

    @Autowired
    private ElectlogMapper  electlogMapper;


    @Autowired
    private AnnexMapper annexMapper;
    static Logger logger = LogManager.getLogger(TcSurfaceElectSericeImpl.class);

    /**
     * 分页查询水表信息
     *
     * @param
     * @return
     */
    @Override
    public BaseDto listPageElectMeterInfos(WyBusinessContext ctx, TcElectMeterSearch tcElectMeter) {
        List<TcWaterMeter> list =tcSurfaceElectMapper.listPageElectMeterInfos(tcElectMeter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcElectMeter.getPage());
        return baseDto ;
    }

    /**
     * 查询电表级别为非终端表
     *
     * @param
     * @return
     */
    @Override
    public BaseDto listPageParentInfos(WyBusinessContext ctx, TcElectMeterSearch tcElectMeter) {
        List<TcWaterMeter> list =tcSurfaceElectMapper.listPageParentInfos(tcElectMeter);
        BaseDto baseDto = new BaseDto<>();
        baseDto.setLstDto(list);
        baseDto.setPage(tcElectMeter.getPage());
        return baseDto ;
    }
    /**
     * 删除水表信息
     *
     * @param id
     * @return
     */
    @Override
    public MessageMap delete(WyBusinessContext ctx, String id) {
        MessageMap messageMap = new MessageMap();
        TcElectMeter   tcElectMeter=this.tcSurfaceElectMapper.SeleteByID(id);
        this.tcSurfaceElectMapper.deleteSurface(id);


        //添加操作日志
        TcElectlog  tcElectlog  =  new TcElectlog();
        tcElectlog.setCode(tcElectMeter.getCode());
        tcElectlog.setElectId(id);
        tcElectlog.setModifyType("delete");
        tcElectlog.setOperator(ctx.getStaffName());
        this.electlogMapper.insert(tcElectlog);

        return messageMap;
    }


    /**
     * 修改水表信息
     *
     * @param ctx
     * @param
     * @return
     */
    @Override
    public MessageMap modify(WyBusinessContext ctx, TcElectMeter  tcElectMeter) {
        MessageMap messageMap = new MessageMap();
        if (tcElectMeter.getCode() != null && !"".equals(tcElectMeter.getCode())) {
           tcElectMeter.setModifyid(tcElectMeter.getCode());
        }
        this.tcSurfaceElectMapper.modify(tcElectMeter);

        //添加操作日志
        TcElectlog  tcElectlog  =  new TcElectlog();
        tcElectlog.setElectId(tcElectMeter.getId());
        tcElectlog.setCode(tcElectMeter.getCode());
        String dirtyinfo =  StringUtils.join(tcElectMeter.getDirtyinfo(),",");
        tcElectlog.setModifyMatter(dirtyinfo);
        tcElectlog.setModifyType("edit");
        tcElectlog.setOperator(ctx.getStaffName());
        this.electlogMapper.insert(tcElectlog);

        return messageMap;
    }


    /**
     * 新增水表信息
     *
     * @param ctx
     * @param
     * @return
     */
    @Override
    public MessageMap add(WyBusinessContext ctx, TcElectMeter  tcElectMeter) {
        MessageMap messageMap = new MessageMap();
        tcElectMeter.setCompanycode(ctx.getCompanyCode());
        // 生成建筑编码 : 规则系统自动生成，项目流水码（2位）+04+流水码（从00001开始，不限位数），如110400001
        String projectId = tcElectMeter.getProjectId();
        String newCoding = projectId.substring(projectId.length() - 2) + WyEnum.CODE_TYPE_07.getStringValue();
        // 获取流水码 取最大加1
        String MaxCode = this.tcSurfaceElectMapper.getCode(projectId);
        //自动补位
        MaxCode = StringUtils.leftPad(MaxCode, 5, "0");
        newCoding = newCoding + MaxCode;
        //UUID 在JAVA 中生成 下面还要用到
        String uuid = UUID.randomUUID().toString();
        tcElectMeter.setId(uuid);
       tcElectMeter.setCode(newCoding);


        String id = CommonUtils.getUUID();
        if (CommonUtils.isNotEmpty(tcElectMeter.getAnnexs())) {
            for (Annex annex : tcElectMeter.getAnnexs()) {
                annex.setRelationId(id);
                annex.setAnnexTime(CommonUtils.getDateStr());
                annexMapper.insertAnnex(annex);
            }
        }

        TcBuilding tcBuilding  =  new TcBuilding();
        tcBuilding.setId(uuid);
        tcBuilding.setBuildingType("ElectMeter");
        List<TcBuilding>  list  =  new ArrayList<>();
        list.add(tcBuilding);
        this.TcBuildingMapper.batchInsert(list);
        this.tcSurfaceElectMapper.insert(tcElectMeter);

        //添加操作日志
        TcElectlog  tcElectlog  =  new TcElectlog();
        tcElectlog.setCode(tcElectMeter.getCode());
        tcElectlog.setElectId(tcElectlog.getId());
        tcElectlog.setModifyType("insert");
        tcElectlog.setOperator(ctx.getStaffName());
        this.electlogMapper.insert(tcElectlog);
        return  messageMap;
    }
}
