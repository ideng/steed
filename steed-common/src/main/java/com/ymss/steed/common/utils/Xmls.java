package com.ymss.steed.common.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.SortableFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.SunUnsafeReflectionProvider;

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
        xstream.processAnnotations(obj.getClass());
        return xstream.toXML(obj);
    }

    /**
     * Convert object to XML with ordered nodes
     * 
     * @param obj
     * @param sorter
     * @return
     */
    public static String obj2Xml(Object obj, SortableFieldKeySorter sorter) {
        if (obj == null || sorter == null) return obj2Xml(obj);

        XStream xstream = new XStream(new SunUnsafeReflectionProvider(new FieldDictionary(sorter)));;
        xstream.processAnnotations(obj.getClass());
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
        xstream.processAnnotations(clazz);
        return (T) xstream.fromXML(xml);
    }
}
