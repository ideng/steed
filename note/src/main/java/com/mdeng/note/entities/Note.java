package com.mdeng.note.entities;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.elasticsearch.common.xcontent.XContentBuilder;

import com.google.common.collect.Lists;
import com.mdeng.note.es.index.Indexable;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class Note implements Indexable {

    private String id = UUID.randomUUID().toString();
    private String pid;
    private String title;
    private List<String> tags = Lists.newArrayList();
    private Date createdTime;
    private Date modifiedTime;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public String getIndex() {
        return "mdeng_1";
    }

    @Override
    public String getType() {
        return "note";
    }

    @Override
    public String getSource() throws IOException {
        XContentBuilder builder = jsonBuilder()
                .startObject()
                  .field("id", id)
                  .field("title", title)
                  .field("createdTime", createdTime)
                  .field("modifiedTime", modifiedTime)
                  .field("content", content);
        
        builder.startArray();
        for (String tag : tags) {
            builder.field(tag);
        }
        builder.endArray();
        
        return builder.toString();
    }

}
