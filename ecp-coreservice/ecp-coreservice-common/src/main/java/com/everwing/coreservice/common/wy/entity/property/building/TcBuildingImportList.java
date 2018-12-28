package com.everwing.coreservice.common.wy.entity.property.building;/**
 * Created by wust on 2017/5/3.
 */

/**
 *
 * Function:导入excel封装数据的实体
 * Reason:
 * Date:2017/5/3
 * @author wusongti@lii.com.cn
 */
public class TcBuildingImportList extends TcBuilding {
    private static final long serialVersionUID = 8780793913009777638L;
    // 一级节点
    private String firstNode;

    // 二级节点
    private String secondNode;

    // 三级节点
    private String thirdNode;

    // 四级节点
    private String fourthNode;

    // 五级节点
    private String fifthNode;

    // 六级节点
    private String sixthNode;

    // 是否成功，必须要加该字段
    private Boolean successFlag;

    // 错误原因，必须要加该字段
    private String errorMessage;

    public String getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(String firstNode) {
        this.firstNode = firstNode;
    }

    public String getSecondNode() {
        return secondNode;
    }

    public void setSecondNode(String secondNode) {
        this.secondNode = secondNode;
    }

    public String getThirdNode() {
        return thirdNode;
    }

    public void setThirdNode(String thirdNode) {
        this.thirdNode = thirdNode;
    }

    public String getFourthNode() {
        return fourthNode;
    }

    public void setFourthNode(String fourthNode) {
        this.fourthNode = fourthNode;
    }

    public String getFifthNode() {
        return fifthNode;
    }

    public void setFifthNode(String fifthNode) {
        this.fifthNode = fifthNode;
    }

    public String getSixthNode() {
        return sixthNode;
    }

    public void setSixthNode(String sixthNode) {
        this.sixthNode = sixthNode;
    }

    public Boolean getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(Boolean successFlag) {
        this.successFlag = successFlag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return super.toString() + "\nTcBuildingImportList{" +
                "firstNode='" + firstNode + '\'' +
                ", secondNode='" + secondNode + '\'' +
                ", thirdNode='" + thirdNode + '\'' +
                ", fourthNode='" + fourthNode + '\'' +
                ", fifthNode='" + fifthNode + '\'' +
                ", sixthNode='" + sixthNode + '\'' +
                ", successFlag=" + successFlag +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
