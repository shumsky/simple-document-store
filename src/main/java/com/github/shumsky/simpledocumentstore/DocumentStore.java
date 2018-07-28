package com.github.shumsky.simpledocumentstore;

import java.util.Optional;
import java.util.Set;

public interface DocumentStore {

    void insert(String documentId, String document);

    Optional<String> find(String documentId);

    Set<String> findByKeywords(String... keywords);
}
