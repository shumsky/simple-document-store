package com.github.shumsky.simpledocumentstore;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemoryDocumentStoreTest {

    @Test
    public void testInsertAndFindDocument() {
        DocumentStore store = new InMemoryDocumentStore();
        String document = "foo bar baz";
        String documentId = "foo bar baz";
        store.insert(documentId, document);
        String foundDocument = store.find(documentId);
        assertEquals(document, foundDocument);
    }
}
