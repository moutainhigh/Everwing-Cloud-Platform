package com.everwing.coreservice.common.platform.entity.generated;

import java.io.Serializable;

/**
 *
 * This class was generated by MyBatis Generator.
 * This class corresponds to the database table id_gen
 *
 * @mbg.generated do_not_delete_during_merge
 */
public class IdGen implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column id_gen.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column id_gen.sub
     *
     * @mbg.generated
     */
    private String sub;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column id_gen.type
     *
     * @mbg.generated
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table id_gen
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column id_gen.id
     *
     * @return the value of id_gen.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column id_gen.id
     *
     * @param id the value for id_gen.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column id_gen.sub
     *
     * @return the value of id_gen.sub
     *
     * @mbg.generated
     */
    public String getSub() {
        return sub;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column id_gen.sub
     *
     * @param sub the value for id_gen.sub
     *
     * @mbg.generated
     */
    public void setSub(String sub) {
        this.sub = sub == null ? null : sub.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column id_gen.type
     *
     * @return the value of id_gen.type
     *
     * @mbg.generated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column id_gen.type
     *
     * @param type the value for id_gen.type
     *
     * @mbg.generated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table id_gen
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
        sb.append(", sub=").append(sub);
        sb.append(", type=").append(type);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}