package com.ideng.common.utils;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 字符集相关工具类
 * 
 * @author hui.deng
 *
 */
public class Charsets {

    /**
     * 从字符串中移除4字节utf8编码字符
     * 
     * @param str
     * @return
     * @throws Exception
     */
    public static String remove4BytesUTF8Char(String str) throws Exception {
        // 参考: http://blog.csdn.net/wskings/article/details/12857993
        byte[] bb = str.getBytes("utf-8");
        byte[] cc = new byte[bb.length];
        int j = 0;
        for (int i = 0; i < bb.length; i++) {
            if ((bb[i] & 0xF8) == 0xF0) {
                i += 3;
            } else {
                cc[j++] = bb[i];
            }
        }
        return new String(Arrays.copyOf(cc, j), Charset.forName("utf-8"));
    }
    
    public static void main(String[] args) throws Exception {
        String t = "𠂇TR自行车";
        System.out.println(remove4BytesUTF8Char(t));
        System.out.println(remove4BytesUTF8Char("0000"));
    }
}
