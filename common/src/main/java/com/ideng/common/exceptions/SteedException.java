package com.ideng.common.exceptions;

/**
 * Project exception base
 * 
 * @author Administrator
 *
 */
public class SteedException extends RuntimeException {
    private static final long serialVersionUID = -1514949907784004043L;

    private ErrorCode error;
    private String detail;
    private Exception innerException;

    public SteedException(ErrorCode error, String detail) {
        super(error.toString() + ": " + detail);

        this.error = error;
    }

    public SteedException(ErrorCode error, String detail, Exception inner) {
        this(error, detail);
        this.innerException = inner;
    }

    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Exception getInnerException() {
        return innerException;
    }

    public void setInnerException(Exception innerException) {
        this.innerException = innerException;
    }

}
