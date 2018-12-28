package com.everwing.server.gating.advice;

import com.everwing.coreservice.common.constant.ResponseCode;
import com.everwing.coreservice.common.dto.LinphoneResult;
import com.everwing.coreservice.common.platform.exception.ArgException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static Logger logger= LogManager.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public LinphoneResult exception(Exception e, WebRequest request){
        logger.error("程序异常:{}",e);
        if (e instanceof ArgException){
            return new LinphoneResult(ResponseCode.PARAMS_IS_NULL);
        }
        return new LinphoneResult(ResponseCode.RESOLVE_FAIL,e.getMessage());
    }
}
