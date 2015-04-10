package com.mdeng.note.storage.impl;

import com.mdeng.note.entities.Note;
import com.mdeng.note.storage.NoteStorage;

public class HdfsNoteStorage extends HdfsStorage implements NoteStorage {

    @Override
    public void save(Note note) throws Exception {
        writeFile(getFS(), ROOT_PATH + "/" + note.getPid() + "/" + note.getId(), note);
    }

    public static void main(String[] args) throws Exception {
        Note note = new Note();
        HdfsNoteStorage storage = new HdfsNoteStorage();
        storage.save(note);

        System.exit(0);
    }
}
