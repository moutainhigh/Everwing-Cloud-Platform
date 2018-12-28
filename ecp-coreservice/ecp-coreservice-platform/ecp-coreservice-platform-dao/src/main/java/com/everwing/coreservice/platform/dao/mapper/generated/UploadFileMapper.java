package com.everwing.coreservice.platform.dao.mapper.generated;

import com.everwing.coreservice.common.platform.entity.generated.UploadFile;
import com.everwing.coreservice.common.platform.entity.generated.UploadFileExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UploadFileMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    long countByExample(UploadFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    int deleteByExample(UploadFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String uploadFileId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    int insert(UploadFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    int insertSelective(UploadFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    List<UploadFile> selectByExample(UploadFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    UploadFile selectByPrimaryKey(String uploadFileId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") UploadFile record, @Param("example") UploadFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") UploadFile record, @Param("example") UploadFileExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UploadFile record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table upload_file
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UploadFile record);
}