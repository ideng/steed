package com.mdeng.common.exceptions;

/**
 * Predefined errors
 * 
 * @author Administrator
 *
 */
public enum ErrorCode {

    OK(200, "OK"),
    ERROR_UNKNOWN(100, "ERROR_UNKNOWN");

    private ErrorCode(int code, String type) {
        this.code = code;
        this.type = type;
    }

    private int code;
    private String type;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "(" + code + ", " + type + ")";
    }
}
