package com.everwing.coreservice.wy.api.configuration.bc.collection;

import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.jrl.TBcJrlHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.TBcUnionCollectionHead;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackBody;
import com.everwing.coreservice.common.wy.entity.configuration.bc.collection.union.back.TBcUnionBackHead;
import com.everwing.coreservice.common.wy.service.configuration.bc.collection.TBcCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("tBcCollectionApi")
public class TBcCollectionApi {

	@Autowired
	private TBcCollectionService tBcCollectionService;

	public RemoteModelResult<BaseDto> listPageUnionCollHeads(String companyId,TBcUnionCollectionHead head) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.listPageUnionCollHeads(companyId,head));
	}

	public RemoteModelResult<BaseDto> listPageCollBodyInfos(String companyId,TBcUnionCollectionBody body) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.listPageCollBodyInfos(companyId,body));
	}

	public RemoteModelResult<BaseDto> listPageUnionBackHeads(String companyId,TBcUnionBackHead head) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.listPageUnionBackHeads(companyId,head));
	}

	public RemoteModelResult<BaseDto> listPageUnionBackbodies(String companyId,TBcUnionBackBody body) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.listPageUnionBackbodies(companyId,body)); 
	}

	public RemoteModelResult<BaseDto> genCollTxtFile(String companyId,TBcUnionCollectionHead head) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.genCollTxtFile(companyId,head));  
	}

	public RemoteModelResult<BaseDto> exportCollTxtFile(String companyId,TBcUnionCollectionHead head,Integer type) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.exportCollTxtFile(companyId,head , type));
	}

	public RemoteModelResult<BaseDto> listPageJrlHeads(String companyId,TBcJrlHead head) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.listPageJrlHeads(companyId,head));
	}

	public RemoteModelResult<BaseDto> listPageJrlBodies(String companyId,TBcJrlBody body) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.listPageJrlBodies(companyId,body));
	}

	public RemoteModelResult<BaseDto> genJrlCollZipFile(String companyId,TBcJrlHead head) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.genJrlCollZipFile(companyId,head));
	}

	public RemoteModelResult<MessageMap> importFile(String companyId, String fileContent,String fileName, Integer flag, String totalId, String projectId,String userId) {
		return new RemoteModelResult<MessageMap>(this.tBcCollectionService.importFile(companyId,fileContent, fileName, flag , totalId,projectId, userId));
	}


	public RemoteModelResult<BaseDto> genCollByManaul(String companyId,String projectId, Integer isStopCommon) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.genCollByManaul(companyId,projectId,isStopCommon));
	}

	public RemoteModelResult<MessageMap> importFileByQueue(String companyId,String fileContent, String fileName, Integer flag, String totalId,String projectId, String userId) {
		return new RemoteModelResult<MessageMap>(this.tBcCollectionService.importFileByQueue(companyId, fileContent, fileName, flag , totalId,projectId, userId));
	}

	public RemoteModelResult<BaseDto> findDatas(String companyId,String projectId, Date createTime, Integer collectionType) {
		return new RemoteModelResult<BaseDto>(this.tBcCollectionService.findDatas(companyId,projectId, createTime, collectionType));
	}
	
	public RemoteModelResult<MessageMap> separateAddServiceChargeForUnion(String companyId,String projectId) {
		return new RemoteModelResult<MessageMap>(this.tBcCollectionService.separateAddServiceChargeForUnion(companyId,projectId));
	}
	
	
	
}
