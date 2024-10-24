package com.bigprime.common.exception;

import com.bigprime.common.base.Result;
import com.bigprime.common.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author lyw
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class ServerExceptionHandler {
    /**
     * 处理自定义异常
     */
    @ExceptionHandler(ServerException.class)
    public Result<String> handleException(ServerException ex) {

        return Result.error(ex.getCode(), ex.getMsg());
    }

    /**
     * SpringMVC参数绑定，Validator校验不正确
     */
    @ExceptionHandler(BindException.class)
    public Result<String> bindException(BindException ex) {
        FieldError fieldError = ex.getFieldError();
        assert fieldError != null;
        return Result.error(fieldError.getDefaultMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result<String> handleAccessDeniedException(Exception ex) {

        return Result.error(ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return Result.error("出错了！异常信息：" + ex.getMessage());
    }
}
