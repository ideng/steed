package com.ymss.steed.common.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * JSON Utilities
 * 
 * @author Administrator
 *
 */
public class Jsons {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Convert an object to JSON string
     * 
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        if (obj == null) return null;

        try {
            return MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Convert an object to JSON and write to stream
     * 
     * @param out
     * @param obj
     */
    public static void writeJson(OutputStream out, Object obj) {
        if (out == null || obj == null) return;

        try {
            MAPPER.writeValue(out, obj);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static <T> T fromJson(Class<T> clazz, String json) {
        return null;

    }

    public static <T> List<T> fromJsonAsList(Class<T[]> clazz, String json) {
        return null;

    }
}
