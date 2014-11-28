package com.ideng.steed.common.exceptions;

/**
 * Predefined errors
 * 
 * @author Administrator
 *
 */
public enum ErrorStatus {

    OK(200, "OK"),
    UNKNOWN(100, "UNKNOWN"),
    EXECUTION_FAILED(101, "EXECUTION_FAILED");

    private ErrorStatus(int code, String type) {
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
