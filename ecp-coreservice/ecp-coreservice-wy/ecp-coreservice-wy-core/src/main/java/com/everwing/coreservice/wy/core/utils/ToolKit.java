package com.everwing.coreservice.wy.core.utils;

import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.MeterEnum;
import com.everwing.coreservice.common.wy.entity.business.electmeter.ElectMeter;
import com.everwing.coreservice.common.wy.entity.business.meterdata.TcMeterData;
import com.everwing.coreservice.common.wy.entity.business.meterrelation.TcMeterRelation;
import com.everwing.coreservice.common.wy.entity.business.watermeter.TcHydroMeterOperationRecord;

import java.util.ArrayList;
import java.util.List;

public class ToolKit {
	
	public static TcHydroMeterOperationRecord createTcHyMeterOpearRecord(ElectMeter beforChangeElectMeter,TcMeterData betcData,ElectMeter beforeelect,ElectMeter afterMeter){
		TcHydroMeterOperationRecord tc = new TcHydroMeterOperationRecord();
		tc.setReplaceBeforeCode(beforChangeElectMeter.getCode());
		tc.setReplaceAfterCode(afterMeter.getCode());
		tc.setMeterType(MeterEnum.RECORD_METERTYPE_ELECT.getIntValue()); //电表
		tc.setReadingBefore(beforChangeElectMeter.getInitamount());
		tc.setReadingAfter(afterMeter.getInitamount());
		tc.setOperationType(MeterEnum.RECORD_OPEARTETYPE_CHANGE.getIntValue());//更换
		tc.setOperationReason(afterMeter.getChangereason());
		tc.setOperationUser(afterMeter.getModifyid());
		tc.setFairValueBefore(Double.parseDouble((String.valueOf(beforChangeElectMeter.getInitaveragevalue()))));
		tc.setPeakValueBefore(Double.parseDouble(String.valueOf(beforChangeElectMeter.getInitpeakvalue())));
		tc.setValleyValueBefore(Double.parseDouble(String.valueOf(beforChangeElectMeter.getInitvalleyvalue())));
		tc.setFairValueAfter(Double.parseDouble(String.valueOf(afterMeter.getInitaveragevalue())));
		tc.setPeakValueAfter(Double.parseDouble(String.valueOf(afterMeter.getInitpeakvalue())));
		tc.setValleyValueAfter(Double.parseDouble(String.valueOf(afterMeter.getInitvalleyvalue())));
		if(CommonUtils.isEmpty(betcData)){//说明该表是新表还没抄表过就要被更换；用量为更换前当前读数-初始读数;如果抄表过用当前读数减去最近一次抄表的读数
			tc.setUsedAmount(beforChangeElectMeter.getInitamount()-beforeelect.getInitamount());
			tc.setUsedFairAmount(Double.parseDouble(String.valueOf(beforChangeElectMeter.getInitaveragevalue()-beforeelect.getInitaveragevalue())));
			tc.setUsedPeakAmount(Double.parseDouble(String.valueOf(beforChangeElectMeter.getInitpeakvalue()-beforeelect.getInitpeakvalue())));
			tc.setUsedValleyAmount(Double.parseDouble(String.valueOf(beforChangeElectMeter.getInitvalleyvalue()-beforeelect.getInitvalleyvalue())));
		}else{
			tc.setUsedAmount((beforChangeElectMeter.getInitamount()==null?0:beforChangeElectMeter.getInitamount())-(betcData.getTotalReading()==null?0:betcData.getTotalReading()));
			tc.setUsedFairAmount(beforChangeElectMeter.getInitaveragevalue()-(betcData.getCommonReading()==null?0:betcData.getCommonReading()));
			tc.setUsedPeakAmount(beforChangeElectMeter.getInitpeakvalue()-(betcData.getPeakReading()==null?0:betcData.getPeakReading()));
			tc.setUsedValleyAmount(beforChangeElectMeter.getInitvalleyvalue()-(betcData.getVallyReading()==null?0:betcData.getVallyReading()));
		}
		return tc;
	}
	
	
	public static List<TcMeterRelation> createRelation(String[] builds,String relationId){
		
		List<TcMeterRelation> relationList = new ArrayList<TcMeterRelation>();
		for(int i=0;i<builds.length;i++){
			TcMeterRelation tcMeterRelation = new TcMeterRelation();
			tcMeterRelation.setBuildingCode(builds[i]);
			tcMeterRelation.setRelationId(relationId);
			tcMeterRelation.setType(MeterEnum.RECORD_METERTYPE_ELECT.getIntValue()); //1表示电表
			relationList.add(tcMeterRelation);
		}
		return relationList;
	}
	
}
