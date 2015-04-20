package com.mdeng.note.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.mdeng.note.es.index.Indexable;

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

}
