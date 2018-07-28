package com.github.shumsky.simpledocumentstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toSet;

@Component
public class InMemoryDocumentStore implements DocumentStore {

    private final ConcurrentHashMap<String, String> documents = new ConcurrentHashMap<>();
    private final DocumentIndex index;

    @Autowired
    public InMemoryDocumentStore(DocumentIndex index) {
        this.index = index;
    }

    @Override
    public void insert(String documentId, String document) {
        Objects.requireNonNull(documentId, "documentId must not be null");
        Objects.requireNonNull(document, "document must not be null");

        String existingDocument = documents.get(documentId);
        if (existingDocument != null && !existingDocument.equals(document)) {
            throw new UnsupportedOperationException("Overwrite of a key is not supported");
        }

        documents.put(documentId, document);
        index.add(document, documentId);
    }

    @Override
    public Optional<String> find(String documentId) {
        return Optional.ofNullable(documents.get(documentId));
    }

    @Override
    public Set<String> findByKeywords(String... keywords) {

        BiConsumer<Map<String, Integer>, Set<String>> accumulator = (matches, searchResult) -> {
            searchResult.forEach(id -> {
                Integer counter = matches.getOrDefault(id, 0);
                matches.put(id, counter + 1);
            });
        };

        BinaryOperator<Map<String, Integer>> combiner = (matches1, matches2) -> {
            matches2.forEach((key2, value2) -> {
                Integer value1 = matches1.getOrDefault(key2, 0);
                matches1.put(key2, value1 + value2);
            });
            return matches1;
        };

        Map<String, Integer> matches = Arrays.stream(keywords).parallel()
                .map(index::search)
                .collect(Collector.of(HashMap::new, accumulator, combiner));

        Set<String> documentIds = matches.entrySet().parallelStream()
                .filter(entry -> entry.getValue().equals(keywords.length))
                .map(Map.Entry::getKey)
                .collect(toSet());

        return documentIds;
    }
}
