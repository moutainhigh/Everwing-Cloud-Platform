package com.everwing.coreservice.wy.dao.mapper;

import com.everwing.coreservice.wy.dao.mapper.account.asset.AssetAccountMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-wy-dao.xml")
public class CommonMapperTest {

    @Autowired
    AssetAccountMapper assetAccountMapper;

    @Test
    public void test(){
    }
}
