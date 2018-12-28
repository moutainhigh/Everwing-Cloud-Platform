package com.everwing.coreservice.platform.dao.mapper.extra;

import com.everwing.coreservice.common.platform.entity.extra.IdGen;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by DELL on 2017/5/31.
 */
public interface IdGenExtraMapper {

    /**
     *通过类型查找最大的sequence id值
     * @param idGen 只带类型的对象
     * @return
     */
    int getMaxId(IdGen idGen);
    
    @Update("UPDATE increased_id SET value = value+1 WHERE type = #{type}")
    int increaseId(int type);
    
    @Select("SELECT value FROM increased_id WHERE type = #{type}")
    int getId(int type);

}
