package com.everwing.coreservice.common.wy.entity.cust.enterprise;

public class EnterpriseCustNewSearch extends EnterpriseCustNew {


    private static final long serialVersionUID = -7383495392591293539L;

    /** 法人代表名称 **/
    private String representativeName;

    /** 企业委托人名称 **/
    private String principalName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    @Override
    public String toString() {
        return "EnterpriseCustNewSearch{" +
                "representativeName='" + representativeName + '\'' +
                ", principalName='" + principalName + '\'' +
                '}';
    }
}
