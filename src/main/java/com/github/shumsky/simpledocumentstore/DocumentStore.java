package com.github.shumsky.simpledocumentstore;

public interface DocumentStore {
    String insert(String document);

    String find(String documentId);
}
