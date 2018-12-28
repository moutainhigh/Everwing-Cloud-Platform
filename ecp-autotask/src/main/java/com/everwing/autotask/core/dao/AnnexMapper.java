package com.everwing.autotask.core.dao;

import com.everwing.coreservice.common.wy.entity.annex.Annex;
import org.springframework.stereotype.Repository;

/**
 * Created by DELL on 2018/6/4.
 */
@Repository
public interface AnnexMapper {

    void insert(Annex annex);

}
