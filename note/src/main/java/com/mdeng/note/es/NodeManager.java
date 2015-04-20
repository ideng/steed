package com.mdeng.note.es;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.node.Node;

public class NodeManager {
    private static Node instance = null;

    public static Node getInstance() {
        if (instance == null) {
            synchronized (NodeManager.class) {
                if (instance == null) {
                    instance = nodeBuilder().client(true).node();
                }
            }
        }

        return instance;
    }
}
