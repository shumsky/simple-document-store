package com.github.shumsky.simpledocumentstore;

public interface DocumentStore {

    void insert(String documentId, String document);

    String find(String documentId);
}
