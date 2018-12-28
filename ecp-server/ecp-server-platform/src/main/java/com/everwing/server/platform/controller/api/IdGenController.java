package com.everwing.server.platform.controller.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shiny on 2017/5/31.
 */
@RestController
@RequestMapping("idGen")
public class IdGenController extends BaseApiController{

    /**
     * 根据类型生成最大id值
     * @param type 类型
     * @return 包含最大id值的do
     */
    @PostMapping("/generateMaxId")
    public String getMaxIdByType(@RequestParam int type){
        return renderJson(idGenApi.queryMaxId(type));
    }
}
