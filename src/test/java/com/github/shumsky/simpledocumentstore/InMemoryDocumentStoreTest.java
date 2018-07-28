package com.github.shumsky.simpledocumentstore;

import com.github.shumsky.simpledocumentstore.store.DocumentStore;
import com.github.shumsky.simpledocumentstore.store.InMemoryDocumentStore;
import com.github.shumsky.simpledocumentstore.store.SimpleDocumentIndex;
import org.junit.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InMemoryDocumentStoreTest {

    @Test
    public void testInsertAndFindDocument() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());
        String document = "foo bar baz";
        String documentId = "123";
        store.insert(documentId, document);

        Optional<String> foundDocument = store.find(documentId);

        assertTrue(foundDocument.isPresent());
        assertEquals(document, foundDocument.get());
    }

    @Test
    public void testFindNoDocument() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());
        String document = "foo bar";
        String documentId = "123";
        store.insert(documentId, document);

        Optional<String> foundDocument = store.find("200");

        assertFalse(foundDocument.isPresent());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoesNotAllowToOverwriteKey() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());
        String document = "foo bar";
        String document2 = "baz";
        String documentId = "123";
        store.insert(documentId, document);
        store.insert(documentId, document2);
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

    @Test
    public void testFindMultipleDocumentsByKeyword() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());

        String document1 = "foo bar";
        String documentId1 = "101";
        String document2 = "bar baz";
        String documentId2 = "102";

        store.insert(documentId1, document1);
        store.insert(documentId2, document2);

        Set<String> foundDocumentIds = store.findByKeywords("bar");

        assertEquals(foundDocumentIds.size(), 2);
        assertTrue(foundDocumentIds.contains(documentId1));
        assertTrue(foundDocumentIds.contains(documentId2));
    }

    @Test
    public void testFindMultipleDocumentsByMultipleKeywords() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());

        String document1 = "foo";
        String documentId1 = "101";
        String document2 = "bar baz";
        String documentId2 = "102";
        String document3 = "bar foo baz";
        String documentId3 = "103";

        store.insert(documentId1, document1);
        store.insert(documentId2, document2);
        store.insert(documentId3, document3);

        Set<String> foundDocumentIds = store.findByKeywords("baz", "bar");

        assertEquals(foundDocumentIds.size(), 2);
        assertTrue(foundDocumentIds.contains(documentId2));
        assertTrue(foundDocumentIds.contains(documentId3));
    }

    @Test
    public void testFindDocumentByMultipleKeywords() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());

        String document1 = "foo bar";
        String documentId1 = "101";
        String document2 = "foo bar baz";
        String documentId2 = "102";

        store.insert(documentId1, document1);
        store.insert(documentId2, document2);

        Set<String> foundDocumentIds = store.findByKeywords("foo", "baz");

        assertEquals(foundDocumentIds.size(), 1);
        assertTrue(foundDocumentIds.contains(documentId2));
    }

    @Test
    public void testFindDocumentByMultipleSameKeywords() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());

        String document1 = "foo bar";
        String documentId1 = "101";
        String document2 = "foo bar baz";
        String documentId2 = "102";

        store.insert(documentId1, document1);
        store.insert(documentId2, document2);

        Set<String> foundDocumentIds = store.findByKeywords("bar", "bar");

        assertEquals(foundDocumentIds.size(), 2);
        assertTrue(foundDocumentIds.contains(documentId1));
        assertTrue(foundDocumentIds.contains(documentId2));
    }

    @Test
    public void testFindNoDocumentByIfAtLeastOneMatchMissing() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());

        String document = "bar baz";
        String documentId = "101";
        String document2 = "baz foo";
        String documentId2 = "102";

        store.insert(documentId, document);
        store.insert(documentId2, document2);

        Set<String> foundDocumentIds = store.findByKeywords("foo", "bar");

        assertTrue(foundDocumentIds.isEmpty());
    }

    @Test
    public void testFindNoDocumentByEmptyKeywordList() {
        DocumentStore store = new InMemoryDocumentStore(new SimpleDocumentIndex());

        String document = "bar baz";
        String documentId = "101";
        String document2 = "baz foo";
        String documentId2 = "102";

        store.insert(documentId, document);
        store.insert(documentId2, document2);

        Set<String> foundDocumentIds = store.findByKeywords();

        assertTrue(foundDocumentIds.isEmpty());
    }
}
