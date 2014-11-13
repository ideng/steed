package com.ymss.steed.common.utils;

import com.thoughtworks.xstream.XStream;

/**
 * XML Utilities
 * 
 * @author Administrator
 *
 */
public class Xmls {

    /**
     * Convert object to XML
     * 
     * @param obj
     * @return
     */
    public static String obj2Xml(Object obj) {
        if (obj == null) return null;

        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        return xstream.toXML(obj);
    }

    /**
     * Convert XML to object
     * 
     * @param xml
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2Obj(String xml, Class<T> clazz) {
        if (xml == null) return null;

        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        return (T) xstream.fromXML(xml);
    }
}
