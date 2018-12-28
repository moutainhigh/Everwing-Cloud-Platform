package com.everwing.coreservice.wy.core.service.impl.annex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.everwing.coreservice.common.constant.ReturnCode;
import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.entity.annex.Annex;
import com.everwing.coreservice.common.wy.service.annex.AnnexService;
import com.everwing.coreservice.platform.api.FastDFSApi;
import com.everwing.coreservice.wy.dao.mapper.annex.AnnexMapper;

@Service("annexServiceImpl")
public class AnnexServiceImpl implements AnnexService{ 
	@Autowired
	private AnnexMapper annexMapper;
	
	@Autowired
    private FastDFSApi fastDFSApi;
	
	private static final Logger log = Logger.getLogger(AnnexServiceImpl.class);
	
	@Override
	public BaseDto getListAnnexByReIdAndName(String companyId,String relationId, String annexName,String annexType) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			Annex annex = this.annexMapper.getListAnnexByReIdAndName(relationId, annexName,annexType);
			baseDto.setObj(annex);
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage("查询失败!");
			log.info(e.getMessage());
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}
	
	/**
	 * 查询 根据关联ID查询
	 */
	@SuppressWarnings("rawtypes")
	public BaseDto queryByRelationId(String companyId,Annex annex){
		MessageMap mm = new MessageMap();
		BaseDto baseDto= new BaseDto();
		try {
			List<Annex> list = this.annexMapper.listPageEnclosure(annex);
			//拿到upload_file_id然后去平台查询地址信息
			if( CommonUtils.isNotEmpty(list) && list.size() > 0 ) {
				for (Annex annexT : list) {
					//通过annexid查询uploadfileid
					String uploadFileId=this.annexMapper.getUploadFileId(annexT.getAnnexId());
					if( CommonUtils.isEmpty(uploadFileId) )  {
						continue;
					}
					
					UploadFile uploadFile=fastDFSApi.loadFilePathById( uploadFileId ).getModel();
					if( CommonUtils.isNotEmpty(uploadFile) && CommonUtils.isNotEmpty( uploadFile.getPath() ) ) {
						annexT.setAnnexAddress( uploadFile.getPath() );
					}
				}
			}
			mm.setFlag(MessageMap.INFOR_SUCCESS);
			mm.setMessage("查询成功!");
			baseDto.setLstDto(list);
			baseDto.setPage(annex.getPage());
		} catch (Exception e) {
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage("查询异常");
			log.info(e.getMessage());
		}
		baseDto.setMessageMap(mm);
		return baseDto;
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class)
	public BaseDto addAnnex(String CompanyId, Annex annex) {
		MessageMap msgMap = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			this.annexMapper.insertAnnex(annex);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			msgMap.setMessage("新增成功!");
		} catch (Exception e) {
			msgMap.setFlag(MessageMap.INFOR_ERROR);
			msgMap.setMessage(e.getMessage());
		}
		baseDto.setMessageMap(msgMap);
		return baseDto;
	}

	@Override
	public BaseDto deleteEnclosureFile(String compamyId, Annex annex) {
		MessageMap mm = new MessageMap();
		BaseDto baseDto = new BaseDto();
		try {
			List<String> annexList =annex.getAnnexIds();
			if(null ==annexList || annexList.size()==0){
				mm.setFlag(MessageMap.INFOR_ERROR);
				mm.setMessage("传入的附件编号为空!");
			}else{
				List<Annex> listAnnex= this.annexMapper.getByAnnexIds(annexList);
				if(null == listAnnex || listAnnex.size()==0){
					mm.setFlag(MessageMap.INFOR_ERROR);
					mm.setMessage("需要删除的附件不存在;可能已经被删除!");
				}else{
					//根据annexid删除选中的附件信息,这里删除附件有一定的调整：对接fastDFS，这里删除要调用fastDFS接口进行删除,然后删除我们本地的关联信息
					for(int i=0;i<listAnnex.size();i++){
						Annex quAnnex = listAnnex.get(i);
						//删除fastDFS的文件信息
						fastDFSApi.deleteFile(quAnnex.getUploadFileId());
					}
					//删除本地的关联信息
					annexMapper.delByAnnexIds(annexList);
					mm.setFlag(MessageMap.INFOR_SUCCESS);
					mm.setMessage("删除操作成功");
				}
			}
			
		} catch (Exception e) {
			log.info(e.getMessage());
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage("删除失败!");
		}
		baseDto.setMessageMap(mm);
		return baseDto;
	}

	@Override
	public BaseDto queryBillEnclosure(WyBusinessContext ctx, Annex annex) throws ECPBusinessException{
		BaseDto baseDto = new BaseDto();
		MessageMap msgMap = new MessageMap();
		try {
			List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>();
			Map<String,Object> mapId = this.annexMapper.selectIdPerYear(annex);
			Map<String,Object> mapAnnexName =this.annexMapper.selectBillTypePerYear(annex);
			dataList.add(mapId);
			dataList.add(mapAnnexName);
			baseDto.setLstDto(dataList);
			msgMap.setFlag(MessageMap.INFOR_SUCCESS);
			baseDto.setMessageMap(msgMap);
			return baseDto;
		} catch (Exception e) {
			log.info(CommonUtils.log(e.getMessage()));
			throw new ECPBusinessException(ReturnCode.SYSTEM_ERROR);
		}
	}

	@Override
	public BaseDto queryById(String companyId, String annexId) {
		BaseDto returnDto = new BaseDto();
		returnDto.setObj(this.annexMapper.findById(annexId));
		return null;
	}
	
	
	

}
