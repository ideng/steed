package com.mdeng.note.storage.impl;

import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import com.google.common.collect.Lists;
import com.mdeng.note.entities.Notebook;
import com.mdeng.note.storage.NotebookStorage;

public class HdfsNotebookStorage extends HdfsStorage implements NotebookStorage {

    @Override
    public Notebook create(String name) throws Exception {
        Notebook notebook = new Notebook();
        notebook.setName(name);

        writeFile(getFS(), ROOT_PATH + "/" + notebook.getId() + ".meta", notebook);
        return notebook;
    }

    @Override
    public void delete(Notebook notebook) throws Exception {
        FileSystem fs = getFS();
        FileStatus[] status = fs.listStatus(new Path(ROOT_PATH + notebook.getId()));
        if (status.length > 1) {
            return;
        }

        fs.delete(new Path(ROOT_PATH + notebook.getId()), true);
    }

    @Override
    public List<Notebook> get() throws Exception {
        List<Notebook> ret = Lists.newArrayList();

        FileSystem fs = getFS();
        RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(new Path(ROOT_PATH), false);
        while (iterator.hasNext()) {
            LocatedFileStatus status = iterator.next();
            String name = status.getPath().getName() + ".meta";
            String path = status.getPath().toString() + "/" + name;

            Notebook notebook = readFile(fs, path, Notebook.class);
            ret.add(notebook);
        }

        return ret;
    }

}
