package com.mdeng.note.es.index;

public interface Indexer<T extends Indexable> {

    void index(T t);

    void delete(T t);
}
