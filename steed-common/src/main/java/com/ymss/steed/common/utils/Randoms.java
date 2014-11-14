package com.ymss.steed.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Collections2;

/**
 * Random utilities
 * 
 * @author hui.deng
 *
 */
public class Randoms {

    /**
     * Random select limit counts from collection
     * 
     * @param col
     * @param limit
     * @return
     */
    public static <E> Collection<E> rand(Collection<E> col, int limit) {
        if (col == null || col.size() == 0) return col;

        List<E> list = new ArrayList<E>(col);
        Collections.shuffle(list);
        List<T> retList = new ArrayList<T>();
        Iterator<T> iterator = srcList.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            retList.add(next);
            if (retList.size() >= count) {
                break;
            }
        }

        return retList;
    }
}
