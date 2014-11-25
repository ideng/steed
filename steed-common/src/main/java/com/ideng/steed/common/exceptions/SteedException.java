package com.ideng.steed.common.exceptions;

/**
 * Project exception base
 * 
 * @author Administrator
 *
 */
public class SteedException extends RuntimeException {
    private static final long serialVersionUID = -1514949907784004043L;

    private ErrorStatus error;
    private String detail;
    private Exception innerException;

    public SteedException(ErrorStatus error, String detail) {
        super(error.toString() + ": " + detail);

        this.error = error;
    }

    public SteedException(ErrorStatus error, String detail, Exception inner) {
        this(error, detail);
        this.innerException = inner;
    }

    public ErrorStatus getError() {
        return error;
    }

    public void setError(ErrorStatus error) {
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
