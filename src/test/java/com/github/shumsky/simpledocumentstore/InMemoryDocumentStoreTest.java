package com.github.shumsky.simpledocumentstore;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InMemoryDocumentStoreTest {

    @Test
    public void testInsertAndFindDocument() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());
        String document = "foo bar baz";
        String documentId = "foo bar baz";
        store.insert(documentId, document);
        String foundDocument = store.find(documentId);
        assertEquals(document, foundDocument);
    }

    @Test
    public void testFindDocumentByKeyword() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());

        String document = "foo bar";
        String documentId = "123";
        store.insert(documentId, document);

        Set<String> foundDocumentIds = store.findByKeywords("bar");

        assertEquals(foundDocumentIds.size(), 1);
        assertTrue(foundDocumentIds.contains(documentId));
    }
}
