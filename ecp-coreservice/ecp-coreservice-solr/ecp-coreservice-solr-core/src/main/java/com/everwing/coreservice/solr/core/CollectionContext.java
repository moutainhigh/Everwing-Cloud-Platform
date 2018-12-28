package com.everwing.coreservice.solr.core;

/**
 *
 * Function:封装当前线程下的集合名称
 * Reason:
 * Date:2018/6/4
 * @author wusongti@lii.com.cn
 */
public class CollectionContext implements java.io.Serializable{
    private static final long serialVersionUID = -4791548159866747192L;

    private CollectionContext(){}



    private static final ThreadLocal<CollectionContext> LOCAL = new ThreadLocal<CollectionContext>() {
        protected CollectionContext initialValue() {
            return new CollectionContext();
        }
    };

    public static CollectionContext getContext() {
        return LOCAL.get();
    }


    private String companyId;

    private String collection;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }
}
