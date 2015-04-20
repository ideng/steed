package com.mdeng.note.es.metadata;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;

import com.mdeng.note.entities.Note;

public class NoteMetadata implements IMetadata {

    private static final String mapping;
    private Note entity;
    
    static {
        XContentBuilder builder = null;
        try {
            builder = jsonBuilder()
                    .startObject("note")
                      .startObject("properties")
                        .startObject("id")
                          .field("type", "string")
                          .field("index", "no")
                        .endObject()
                        .startObject("title")
                          .field("type", "string")
                        .endObject()
                        .startObject("createdTime")
                          .field("type", "date")
                        .endObject()
                        .startObject("modifiedTime")
                          .field("type", "date")
                        .endObject()
                        .startObject("content")
                          .field("type", "string")
                        .endObject()
                        .startObject("tags")
                          .field("type", "string")
                          .field("index", "not_analyzed")
                        .endObject()
                      .endObject()
                    .endObject();
        } catch (Exception e) {
            builder = null;
        }
       
        mapping = builder!=null?builder.toString():null;
    }
    
    public NoteMetadata(Note note) {
        entity = note;
    }
    
    public static String getMapping() {
        return mapping;
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
    public String getId() {
        return entity.getId();
    }

    @Override
    public String getSource() throws IOException {
        XContentBuilder builder = jsonBuilder()
                .startObject()
                  .field("id", entity.getId())
                  .field("title", entity.getTitle())
                  .field("createdTime", entity.getCreatedTime())
                  .field("modifiedTime", entity.getModifiedTime())
                  .field("content", entity.getContent());
        
        builder.startArray();
        for (String tag : entity.getTags()) {
            builder.field(tag);
        }
        builder.endArray();
        
        return builder.toString();
    }

}
