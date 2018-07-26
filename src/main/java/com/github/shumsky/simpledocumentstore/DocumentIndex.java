package com.github.shumsky.simpledocumentstore;

import java.util.Set;

public interface DocumentIndex {

    void add(String document, String documentId);

    Set<String> search(String keyword);
}
