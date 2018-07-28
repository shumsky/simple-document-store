package com.github.shumsky.simpledocumentstore;

import com.github.shumsky.simpledocumentstore.store.DocumentIndex;
import com.github.shumsky.simpledocumentstore.store.SimpleDocumentIndex;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SimpleDocumentIndexTest {

    @Test
    public void testIndexDocument() {
        String document = "foo bar";
        String documentId = "123";

        DocumentIndex index = new SimpleDocumentIndex();
        index.add(document, documentId);

        Set<String> fooDocuments = index.search("foo");
        Set<String> barDocuments = index.search("bar");

        assertEquals(fooDocuments.size(), 1);
        assertEquals(barDocuments.size(), 1);

        assertTrue(fooDocuments.contains(documentId));
        assertTrue(barDocuments.contains(documentId));
    }

    @Test
    public void testIndexDocumentWithRepeatingWords() {
        String document = "foo foo bar foo";
        String documentId = "123";

        DocumentIndex index = new SimpleDocumentIndex();
        index.add(document, documentId);

        Set<String> fooDocuments = index.search("foo");

        assertEquals(fooDocuments.size(), 1);
        assertTrue(fooDocuments.contains(documentId));
    }

    @Test
    public void testSearchMultipleDocuments() {
        String document1 = "foo bar";
        String documentId1 = "101";
        String document2 = "bar baz";
        String documentId2 = "102";

        DocumentIndex index = new SimpleDocumentIndex();
        index.add(document1, documentId1);
        index.add(document2, documentId2);

        Set<String> fooDocuments = index.search("foo");
        Set<String> barDocuments = index.search("bar");

        assertEquals(fooDocuments.size(), 1);
        assertEquals(barDocuments.size(), 2);

        assertTrue(fooDocuments.contains(documentId1));
        assertTrue(barDocuments.contains(documentId1));
        assertTrue(barDocuments.contains(documentId2));
    }

    @Test
    public void testSearchNoResult() {
        String document = "foo bar";
        String documentId = "101";

        DocumentIndex index = new SimpleDocumentIndex();
        index.add(document, documentId);

        Set<String> documents = index.search("absent");

        assertEquals(documents.size(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSearchResultIsImmutable() {
        String document = "foo bar";
        String documentId = "101";

        DocumentIndex index = new SimpleDocumentIndex();
        index.add(document, documentId);

        Set<String> documents = index.search("foo");

        documents.add("new_id");
    }
}
