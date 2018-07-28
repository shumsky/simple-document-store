package com.github.shumsky.simpledocumentstore;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleDocumentIndex implements DocumentIndex {

    private final ConcurrentHashMap<String, Set<String>> index = new ConcurrentHashMap<>();

    @Override
    public void add(String document, String documentId) {
        String[] keywords = document.split(" ");
        Arrays.asList(keywords).forEach(keyword -> {
            index.computeIfAbsent(keyword, k -> new HashSet<>());
            index.compute(keyword, (k, v) -> {
                v.add(documentId);
                return v;
            });
        });
    }

    @Override
    public Set<String> search(String keyword) {
        Set<String> documentIds = index.get(keyword);
        return documentIds != null ? Collections.unmodifiableSet(documentIds): Collections.emptySet();
    }
}
