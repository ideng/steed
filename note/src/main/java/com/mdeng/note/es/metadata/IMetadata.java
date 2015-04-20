package com.mdeng.note.es.metadata;

import java.io.IOException;

public interface IMetadata {
    String getIndex();

    String getType();

    String getId();

    String getSource() throws IOException;
}
