package com.ymss.steed.common.utils;

import org.junit.Test;

public class DatesTest {

    @Test
    public void test() {
        System.out.println(Dates.formatNow(Dates.yyyyMMdd));
        System.out.println(Dates.formatNow(Dates.MMddyyyy));
        System.out.println(Dates.formatNow(Dates.MMddyy));
        System.out.println(Dates.formatNow(Dates.MMMdyy));
        
        System.out.println(Dates.formatNow(Dates.HHmm));
        System.out.println(Dates.formatNow(Dates.HHmma));
        System.out.println(Dates.formatNow(Dates.HHmmss));
        System.out.println(Dates.formatNow(Dates.HHmmssSSS));
    }
}
