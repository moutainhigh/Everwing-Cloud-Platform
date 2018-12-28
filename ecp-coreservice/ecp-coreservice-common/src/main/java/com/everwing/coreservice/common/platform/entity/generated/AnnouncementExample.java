package com.everwing.coreservice.common.platform.entity.generated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnouncementExample implements java.io.Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table announcement
     *
     * @mbg.generated
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table announcement
     *
     * @mbg.generated
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table announcement
     *
     * @mbg.generated
     */
    protected List<Criteria> oredCriteria;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table announcement
     *
     * @mbg.generated
     */
    protected int limitStart = -1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table announcement
     *
     * @mbg.generated
     */
    protected int limitEnd = -1;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public AnnouncementExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public void setLimitStart(int limitStart) {
        this.limitStart=limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public int getLimitStart() {
        return limitStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public void setLimitEnd(int limitEnd) {
        this.limitEnd=limitEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public int getLimitEnd() {
        return limitEnd;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table announcement
     *
     * @mbg.generated
     */
    protected abstract static class GeneratedCriteria implements java.io.Serializable{
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andAnnouncementIdIsNull() {
            addCriterion("announcement_id is null");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdIsNotNull() {
            addCriterion("announcement_id is not null");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdEqualTo(String value) {
            addCriterion("announcement_id =", value, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdNotEqualTo(String value) {
            addCriterion("announcement_id <>", value, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdGreaterThan(String value) {
            addCriterion("announcement_id >", value, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdGreaterThanOrEqualTo(String value) {
            addCriterion("announcement_id >=", value, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdLessThan(String value) {
            addCriterion("announcement_id <", value, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdLessThanOrEqualTo(String value) {
            addCriterion("announcement_id <=", value, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdLike(String value) {
            addCriterion("announcement_id like", value, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdNotLike(String value) {
            addCriterion("announcement_id not like", value, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdIn(List<String> values) {
            addCriterion("announcement_id in", values, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdNotIn(List<String> values) {
            addCriterion("announcement_id not in", values, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdBetween(String value1, String value2) {
            addCriterion("announcement_id between", value1, value2, "announcementId");
            return (Criteria) this;
        }

        public Criteria andAnnouncementIdNotBetween(String value1, String value2) {
            addCriterion("announcement_id not between", value1, value2, "announcementId");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andContentIsNull() {
            addCriterion("content is null");
            return (Criteria) this;
        }

        public Criteria andContentIsNotNull() {
            addCriterion("content is not null");
            return (Criteria) this;
        }

        public Criteria andContentEqualTo(String value) {
            addCriterion("content =", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotEqualTo(String value) {
            addCriterion("content <>", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThan(String value) {
            addCriterion("content >", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentGreaterThanOrEqualTo(String value) {
            addCriterion("content >=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThan(String value) {
            addCriterion("content <", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLessThanOrEqualTo(String value) {
            addCriterion("content <=", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentLike(String value) {
            addCriterion("content like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotLike(String value) {
            addCriterion("content not like", value, "content");
            return (Criteria) this;
        }

        public Criteria andContentIn(List<String> values) {
            addCriterion("content in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotIn(List<String> values) {
            addCriterion("content not in", values, "content");
            return (Criteria) this;
        }

        public Criteria andContentBetween(String value1, String value2) {
            addCriterion("content between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andContentNotBetween(String value1, String value2) {
            addCriterion("content not between", value1, value2, "content");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdIsNull() {
            addCriterion("target_company_id is null");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdIsNotNull() {
            addCriterion("target_company_id is not null");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdEqualTo(String value) {
            addCriterion("target_company_id =", value, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdNotEqualTo(String value) {
            addCriterion("target_company_id <>", value, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdGreaterThan(String value) {
            addCriterion("target_company_id >", value, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdGreaterThanOrEqualTo(String value) {
            addCriterion("target_company_id >=", value, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdLessThan(String value) {
            addCriterion("target_company_id <", value, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdLessThanOrEqualTo(String value) {
            addCriterion("target_company_id <=", value, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdLike(String value) {
            addCriterion("target_company_id like", value, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdNotLike(String value) {
            addCriterion("target_company_id not like", value, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdIn(List<String> values) {
            addCriterion("target_company_id in", values, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdNotIn(List<String> values) {
            addCriterion("target_company_id not in", values, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdBetween(String value1, String value2) {
            addCriterion("target_company_id between", value1, value2, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andTargetCompanyIdNotBetween(String value1, String value2) {
            addCriterion("target_company_id not between", value1, value2, "targetCompanyId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdIsNull() {
            addCriterion("create_account_id is null");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdIsNotNull() {
            addCriterion("create_account_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdEqualTo(String value) {
            addCriterion("create_account_id =", value, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdNotEqualTo(String value) {
            addCriterion("create_account_id <>", value, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdGreaterThan(String value) {
            addCriterion("create_account_id >", value, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdGreaterThanOrEqualTo(String value) {
            addCriterion("create_account_id >=", value, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdLessThan(String value) {
            addCriterion("create_account_id <", value, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdLessThanOrEqualTo(String value) {
            addCriterion("create_account_id <=", value, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdLike(String value) {
            addCriterion("create_account_id like", value, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdNotLike(String value) {
            addCriterion("create_account_id not like", value, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdIn(List<String> values) {
            addCriterion("create_account_id in", values, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdNotIn(List<String> values) {
            addCriterion("create_account_id not in", values, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdBetween(String value1, String value2) {
            addCriterion("create_account_id between", value1, value2, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateAccountIdNotBetween(String value1, String value2) {
            addCriterion("create_account_id not between", value1, value2, "createAccountId");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNull() {
            addCriterion("create_date is null");
            return (Criteria) this;
        }

        public Criteria andCreateDateIsNotNull() {
            addCriterion("create_date is not null");
            return (Criteria) this;
        }

        public Criteria andCreateDateEqualTo(Date value) {
            addCriterion("create_date =", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotEqualTo(Date value) {
            addCriterion("create_date <>", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThan(Date value) {
            addCriterion("create_date >", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            addCriterion("create_date >=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThan(Date value) {
            addCriterion("create_date <", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateLessThanOrEqualTo(Date value) {
            addCriterion("create_date <=", value, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateIn(List<Date> values) {
            addCriterion("create_date in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotIn(List<Date> values) {
            addCriterion("create_date not in", values, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateBetween(Date value1, Date value2) {
            addCriterion("create_date between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andCreateDateNotBetween(Date value1, Date value2) {
            addCriterion("create_date not between", value1, value2, "createDate");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andIsReadedIsNull() {
            addCriterion("is_readed is null");
            return (Criteria) this;
        }

        public Criteria andIsReadedIsNotNull() {
            addCriterion("is_readed is not null");
            return (Criteria) this;
        }

        public Criteria andIsReadedEqualTo(Boolean value) {
            addCriterion("is_readed =", value, "isReaded");
            return (Criteria) this;
        }

        public Criteria andIsReadedNotEqualTo(Boolean value) {
            addCriterion("is_readed <>", value, "isReaded");
            return (Criteria) this;
        }

        public Criteria andIsReadedGreaterThan(Boolean value) {
            addCriterion("is_readed >", value, "isReaded");
            return (Criteria) this;
        }

        public Criteria andIsReadedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_readed >=", value, "isReaded");
            return (Criteria) this;
        }

        public Criteria andIsReadedLessThan(Boolean value) {
            addCriterion("is_readed <", value, "isReaded");
            return (Criteria) this;
        }

        public Criteria andIsReadedLessThanOrEqualTo(Boolean value) {
            addCriterion("is_readed <=", value, "isReaded");
            return (Criteria) this;
        }

        public Criteria andIsReadedIn(List<Boolean> values) {
            addCriterion("is_readed in", values, "isReaded");
            return (Criteria) this;
        }

        public Criteria andIsReadedNotIn(List<Boolean> values) {
            addCriterion("is_readed not in", values, "isReaded");
            return (Criteria) this;
        }

        public Criteria andIsReadedBetween(Boolean value1, Boolean value2) {
            addCriterion("is_readed between", value1, value2, "isReaded");
            return (Criteria) this;
        }

        public Criteria andIsReadedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_readed not between", value1, value2, "isReaded");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table announcement
     *
     * @mbg.generated do_not_delete_during_merge
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table announcement
     *
     * @mbg.generated
     */
    public static class Criterion implements java.io.Serializable{
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}