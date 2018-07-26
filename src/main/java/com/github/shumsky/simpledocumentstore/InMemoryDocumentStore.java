package com.github.shumsky.simpledocumentstore;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryDocumentStore implements DocumentStore {

    private final ConcurrentHashMap<String, String> documents = new ConcurrentHashMap<>();

    @Override
    public String insert(String document) {
        String documentId = UUID.randomUUID().toString();
        documents.put(documentId, document);
        return documentId;
    }

    @Override
    public String find(String documentId) {
        return documents.get(documentId);
    }
}
