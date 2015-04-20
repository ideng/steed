package com.mdeng.note.es.index;

import java.io.IOException;

import org.elasticsearch.client.Client;

import com.mdeng.note.es.NodeManager;
import com.mdeng.note.es.metadata.IMetadata;

public class IndexerImpl<T extends IMetadata> implements Indexer<T> {

    @Override
    public void index(T t) {
        Client client = NodeManager.getInstance().client();
        try {
            client.prepareIndex(t.getIndex(), t.getType(), t.getId()).
                setSource(t.getSource()).setOperationThreaded(true).execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void delete(T t) {
        // TODO Auto-generated method stub

    }

}
