package com.ideng.steed.common.exceptions;

/**
 * Base to throw steed exception
 * 
 * @author Administrator
 *
 */
public class SteedError {

    protected static SteedException throwError(ErrorStatus error, String msg)
    {
        return new SteedException(error, msg);
    }
    
    protected static SteedException throwError(ErrorStatus error, String msg, Exception inner)
    {
        return new SteedException(error, msg, inner);
    }
    
    public static SteedException Unknown() {
        return throwError(ErrorStatus.UNKNOWN, "internal error");
    }
    
    public static SteedException ExecutionError(Exception inner) {
        return throwError(ErrorStatus.EXECUTION_FAILED, "execution failed", inner);
    }
}
