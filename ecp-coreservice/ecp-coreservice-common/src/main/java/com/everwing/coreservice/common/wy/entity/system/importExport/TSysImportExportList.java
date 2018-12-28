package com.everwing.coreservice.common.wy.entity.system.importExport;/**
 * Created by wust on 2017/5/5.
 */

/**
 *
 * Function:
 * Reason:
 * Date:2017/5/5
 * @author wusongti@lii.com.cn
 */
public class TSysImportExportList extends TSysImportExport {
    private static final long serialVersionUID = 5590142187026428262L;
    private String timeDifference;//耗时

    private String statusName;

    public String getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(String timeDifference) {
        this.timeDifference = timeDifference;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return "TSysImportExportList{" +
                "timeDifference='" + timeDifference + '\'' +
                ", statusName='" + statusName + '\'' +
                ", createrId='" + createrId + '\'' +
                ", createrName='" + createrName + '\'' +
                ", createTime=" + createTime +
                ", modifyId='" + modifyId + '\'' +
                ", modifyName='" + modifyName + '\'' +
                ", modifyTime=" + modifyTime +
                ", lan='" + lan + '\'' +
                '}';
    }
}
