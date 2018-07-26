package com.github.shumsky.simpledocumentstore;

import java.util.Set;

public interface DocumentStore {

    void insert(String documentId, String document);

    String find(String documentId);

    Set<String> findByKeywords(String... keywords);
}
