package com.github.shumsky.simpledocumentstore;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleDocumentIndex implements DocumentIndex {

    private final ConcurrentHashMap<String, Set<String>> index = new ConcurrentHashMap<>();

    @Override
    public void add(String document, String documentId) {
        String[] keywords = document.split(" ");
        Arrays.asList(keywords).forEach(keyword -> {
            index.computeIfAbsent(keyword, k -> new LinkedHashSet<>());
            index.computeIfPresent(keyword, (k, v) -> {
                v.add(documentId);
                return v;
            });
        });
    }

    @Override
    public Set<String> search(String keyword) {
        return index.get(keyword);
    }
}
