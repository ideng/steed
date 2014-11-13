package com.ymss.steed.common.utils;

import java.text.SimpleDateFormat;

public class DateTimeFormatter {

    public enum Format {
        YYYYMMDD, MMDDYYYY, MMDDYY, HHmmss, HHmm, HHmma, HHmmssSSS
    }
    
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat();
}
