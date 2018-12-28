package com.everwing.coreservice.platform.dao.mapper.generated;

import com.everwing.coreservice.common.platform.entity.generated.Announcement;
import com.everwing.coreservice.common.platform.entity.generated.AnnouncementExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnnouncementMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    long countByExample(AnnouncementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    int deleteByExample(AnnouncementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String announcementId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    int insert(Announcement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    int insertSelective(Announcement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    List<Announcement> selectByExample(AnnouncementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    Announcement selectByPrimaryKey(String announcementId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") Announcement record, @Param("example") AnnouncementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") Announcement record, @Param("example") AnnouncementExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Announcement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table announcement
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Announcement record);
}