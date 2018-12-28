package com.everwing.coreservice.common.platform.entity.generated;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table t_pro_exe_info
 *
 * @mbg.generated do_not_delete_during_merge
 */
public class TProExeInfo implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pro_exe_info.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     * Database Column Remarks:
     *   执行内容
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pro_exe_info.content
     *
     * @mbg.generated
     */
    private String content;

    /**
     * Database Column Remarks:
     *   存储过程执行时间
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_pro_exe_info.execute_time
     *
     * @mbg.generated
     */
    private Date executeTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_pro_exe_info
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pro_exe_info.id
     *
     * @return the value of t_pro_exe_info.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pro_exe_info.id
     *
     * @param id the value for t_pro_exe_info.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pro_exe_info.content
     *
     * @return the value of t_pro_exe_info.content
     *
     * @mbg.generated
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pro_exe_info.content
     *
     * @param content the value for t_pro_exe_info.content
     *
     * @mbg.generated
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_pro_exe_info.execute_time
     *
     * @return the value of t_pro_exe_info.execute_time
     *
     * @mbg.generated
     */
    public Date getExecuteTime() {
        return executeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_pro_exe_info.execute_time
     *
     * @param executeTime the value for t_pro_exe_info.execute_time
     *
     * @mbg.generated
     */
    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_pro_exe_info
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", content=").append(content);
        sb.append(", executeTime=").append(executeTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}