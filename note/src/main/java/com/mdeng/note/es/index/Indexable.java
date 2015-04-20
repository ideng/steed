package com.mdeng.note.es.index;

import java.io.IOException;

public interface Indexable {

    String getIndex();
    
    String getType();
    
    String getId();
    
    String getSource() throws IOException;
}
