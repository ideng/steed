package com.mdeng.note.storage;

import java.util.List;

import com.mdeng.note.entities.Notebook;

public interface NotebookStorage {

    Notebook create(String name) throws Exception;

    void delete(Notebook notebook) throws Exception;

    List<Notebook> get() throws Exception;

}
