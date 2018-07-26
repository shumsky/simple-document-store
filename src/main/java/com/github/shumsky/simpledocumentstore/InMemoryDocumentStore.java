package com.github.shumsky.simpledocumentstore;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryDocumentStore implements DocumentStore {

    private final ConcurrentHashMap<String, String> documents = new ConcurrentHashMap<>();

    @Override
    public void insert(String documentId, String document) {
        documents.put(documentId, document);
    }

    @Override
    public String find(String documentId) {
        return documents.get(documentId);
    }
}
