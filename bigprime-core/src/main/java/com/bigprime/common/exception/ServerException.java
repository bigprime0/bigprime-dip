package com.bigprime.common.exception;

import com.bigprime.common.constant.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServerException  extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;

    public ServerException(String msg) {
        super(msg);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }

    public ServerException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.code = errorCode.getCode();
        this.msg = errorCode.getMsg();
    }

    public ServerException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR.getCode();
        this.msg = msg;
    }
}
