package com.everwing.server.wy.util;/**
 * Created by wust on 2017/4/28.
 */

import com.everwing.coreservice.common.dto.RemoteModelResult;
import com.everwing.coreservice.common.utils.SpringContextHolder;
import com.everwing.coreservice.common.BaseDto;
import com.everwing.coreservice.common.context.WyBusinessContext;
import com.everwing.coreservice.common.MessageMap;
import com.everwing.coreservice.common.wy.common.enums.ImportExportEnum;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportList;
import com.everwing.coreservice.common.wy.entity.system.importExport.TSysImportExportSearch;
import com.everwing.coreservice.wy.api.annex.AnnexApi;
import com.everwing.coreservice.wy.api.sys.TSysImportExportApi;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * Function:导出辅助类
 * Reason:
 * Date:2017/4/28
 * @author wusongti@lii.com.cn
 */
public class ImportExportHelper {
    // 等待时间，单位：分钟
    private static final int WAIT_TIME = 5;

    private ImportExportHelper(){

    }


    /**
     * 检测是否有导入任务正在执行
     * @param companyId
     * @param moduleFlag
     * @return
     */
    public static MessageMap checkDoingTask(String companyId, String moduleFlag){
        MessageMap mm = new MessageMap();
        TSysImportExportSearch condition = new TSysImportExportSearch();
        condition.setStatus(ImportExportEnum.doing.name());
        condition.setModuleDescription(moduleFlag);
        RemoteModelResult<List<TSysImportExportList>> remoteModelResult = ((TSysImportExportApi) SpringContextHolder.getBean("tSysImportExportApi")).findByCondtion(companyId,condition);
        if(remoteModelResult.isSuccess()){
            List<TSysImportExportList> list = remoteModelResult.getModel();
            if(!CollectionUtils.isEmpty(list)){
                TSysImportExportList tSysImportExportList = list.get(0);
                long suf;
                if(tSysImportExportList.getStartTime() != null){
                    suf = (new Date().getTime() - tSysImportExportList.getStartTime().getTime()) / 1000 / 60;
                    if(suf < WAIT_TIME){
                        mm.setFlag(MessageMap.INFOR_WARNING);
                        mm.setMessage("有导入任务正在执行，最多还需要等待["+ (WAIT_TIME - suf) + "]分钟。");
                        return mm;
                    }else{
                        for (TSysImportExportList sysImportExportList : list) {
                            sysImportExportList.setStatus(ImportExportEnum.failed.name());
                            ((TSysImportExportApi) SpringContextHolder.getBean("tSysImportExportApi")).modify(WyBusinessContext.getContext().getCompanyId(),sysImportExportList);
                        }
                    }
                }
            }
        }
        return mm;
    }
    
    
    /**
     * 检查附件是否已经存在
     */
    public static MessageMap existAnnex(String companyId,String relationId,String annexName,String annexType){
    	MessageMap mm = new MessageMap();
    	try {
            RemoteModelResult<BaseDto> remoteModelResult = ((AnnexApi) SpringContextHolder.getBean("annexApi")).getListAnnexByReIdAndName(companyId, relationId, annexName,annexType);
           Object obj = remoteModelResult.getModel().getObj();
           if(null ==obj){ //附件不存在，可以新增插入
        	   mm.setFlag(MessageMap.INFOR_SUCCESS);
        	   mm.setMessage("附件在系统中不存在,可以上传");
           }else{
        	   mm.setFlag(MessageMap.INFOR_ERROR);
        	   mm.setMessage("附件在系统中已存在,不可以再次上传");
           }
    	} catch (Exception e) {
			mm.setFlag(MessageMap.INFOR_ERROR);
			mm.setMessage(e.getMessage());
		}
    	
    	return mm;
    }


    /**
     * 不安全，导入成功或失败不能及时更新状态。
     * 请参照TcBuildingServiceImpl.importTcbuilding实现导入
     * @param dir
     * @param batchNo
     * @param msg
     */
    @Deprecated
    public static void writeTxt(String dir,String batchNo,String msg) {

        File dirTempFile = new File(dir);
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }

        String txtPath = dir + File.separator + batchNo + ".txt";

        FileOutputStream os = null;
        try {
            os = new FileOutputStream(new File(txtPath), true);
            os.write((msg + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {

                }
            }
        }
    }
}

