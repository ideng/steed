package com.mdeng.note.storage.impl;

import java.net.URI;
import java.nio.charset.Charset;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;

import com.mdeng.common.utils.Jsons;
import com.mdeng.note.entities.Note;
import com.mdeng.note.storage.NoteStorage;

public class HdfsNoteStorage implements NoteStorage {

    @Value("hdfs.uri")
    private String uri;

    @Override
    public void save(Note note) throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(uri), conf);
        FSDataOutputStream os = fs.create(computePath(note));
        os.write(Jsons.obj2Json(note).getBytes(Charset.forName("utf-8")));
        os.hflush();
    }

    private Path computePath(Note note) {
        return new Path("/note");
    }

    public static void main(String[] args) throws Exception {
        Note note = new Note();
        HdfsNoteStorage storage = new HdfsNoteStorage();
        storage.save(note);

        System.exit(0);
    }
}
