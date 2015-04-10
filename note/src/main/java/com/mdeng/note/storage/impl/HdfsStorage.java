package com.mdeng.note.storage.impl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;

import com.mdeng.common.utils.Jsons;

public class HdfsStorage {

    @Value("hdfs.uri")
    protected String uri;
    protected final String ROOT_PATH = "/note";

    protected FileSystem getFS() throws IOException, URISyntaxException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI(uri), conf);
        return fs;
    }

    protected void writeFile(FileSystem fs, String path, Object object) throws Exception {
        FSDataOutputStream fsout = fs.create(new Path(path));
        fsout.write(Jsons.obj2Json(object).getBytes(Charset.forName("utf-8")));
        fsout.hflush();
    }

    protected <T> T readFile(FileSystem fs, String path, Class<T> clazz) throws Exception {
        FSDataInputStream fsin = fs.open(new Path(path));
        return Jsons.json2Obj(fsin, clazz);
    }
}
