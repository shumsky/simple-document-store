package com.github.shumsky.simpledocumentstore;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemoryDocumentStoreTest {

    @Test
    public void testInsertAndFindDocument() {
        DocumentStore store = new InMemoryDocumentStore(index);
        String document = "foo bar baz";
        String documentId = "foo bar baz";
        store.insert(documentId, document);
        String foundDocument = store.find(documentId);
        assertEquals(document, foundDocument);
    }

    @Test
    public void testFindDocumentByKeyword() {
        DocumentStore store = new InMemoryDocumentStore(index);
        String document = "foo bar";
        String documentId = "123";
        store.insert(documentId, document);
        String foundDocument = store.findByKeywords("bar");
        assertEquals(document, foundDocument);
    }
}
