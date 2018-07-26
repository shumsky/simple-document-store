package com.github.shumsky.simpledocumentstore;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DocumentStoreTest {

    @Test
    public void testInsertAndFindDocument() {
        DocumentStore store = new DocumentStore();
        String document = "foo bar baz";
        String documentId = store.insert(document);
        String foundDocument = store.find(documentId);
        assertEquals(document, foundDocument);
    }
}
