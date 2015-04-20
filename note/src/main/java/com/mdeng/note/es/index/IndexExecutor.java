package com.mdeng.note.es.index;

import com.mdeng.note.es.metadata.IMetadata;

public interface IndexExecutor<T extends IMetadata> {

    void index(T t);

    void delete(T t);
}
