package com.mdeng.note.es.index;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Queues;
import com.mdeng.note.entities.Note;
import com.mdeng.note.es.metadata.IMetadata;
import com.mdeng.note.es.metadata.NoteMetadata;

public class Indexer {

    private BlockingQueue<Note> queue = Queues.newLinkedBlockingDeque();
    private IndexExecutor<IMetadata> executor;
    private ExecutorService service;
    
    public void start() {
        service = Executors.newCachedThreadPool();
        service.submit(new IndexThread());
    }

    public void stop() {
        service.shutdown();
    }
    
    class IndexThread implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Note note = queue.take();

                    executor.index(new NoteMetadata(note));
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

        }

    }
}
