package com.ideng.steed.common.exceptions;

/**
 * Base to throw steed exception
 * 
 * @author Administrator
 *
 */
public class SteedError {

    protected static SteedException throwError(ErrorCode error,  String msg)
    {
        return new SteedException(error, msg);
    }
    
    public static SteedException Unknown() {
        return throwError(ErrorCode.ERROR_UNKNOWN, "Internal Error");
    }
}
