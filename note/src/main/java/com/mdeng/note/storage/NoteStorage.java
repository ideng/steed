package com.mdeng.note.storage;

import com.mdeng.note.entities.Note;

public interface NoteStorage {

    void save(Note note) throws Exception;
}
