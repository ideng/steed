package com.ideng.steed.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        Collection<E> ret;
        if (col instanceof List) {
            ret = new ArrayList<E>(limit);
        } else if (col instanceof Set) {
            ret = new HashSet<E>(limit);
        } else {
            throw new IllegalArgumentException("unsupported collection");
        }

        for (E e : list) {
            ret.add(e);
            if (ret.size() >= limit) break;
        }

        return ret;
    }
}
