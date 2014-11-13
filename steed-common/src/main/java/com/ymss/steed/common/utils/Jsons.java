package com.ymss.steed.common.utils;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
    public static String obj2Json(Object obj) {
        if (obj == null) return null;

        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {}

        return null;
    }

    /**
     * Convert an object to JSON and write to stream
     * 
     * @param out
     * @param obj
     */
    public static void writeAsJson(OutputStream out, Object obj) {
        if (out == null || obj == null) return;

        try {
            MAPPER.writeValue(out, obj);
        } catch (Exception e) {}
    }

    /**
     * Convert JSON to object
     * 
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T json2Obj(String json, Class<T> clazz) {
        if (json == null) return null;

        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {}

        return null;
    }

    /**
     * Convert JSON to List
     * 
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> json2List(String json, Class<T[]> clazz) {
        if (json == null) return null;

        try {
            T[] ts = MAPPER.readValue(json, clazz);
            return Arrays.asList(ts);
        } catch (Exception e) {}

        return null;
    }

    /**
     * Convert JSON to Set
     * 
     * @param json
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> json2Set(String json, Class<T> clazz) {
        if (json == null) return null;

        try {
            return MAPPER.readValue(json, Set.class);
        } catch (Exception e) {}

        return null;
    }
}
