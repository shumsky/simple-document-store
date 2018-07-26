package com.github.shumsky.simpledocumentstore;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentIndexTest {

    @Test
    public void testIndexDocument() {
        String document = "foo bar";
        String documentId = "123";

        DocumentIndex index = new DocumentIndex();
        index.add(document, documentId);

        Set<String> fooDocuments = index.search("foo");
        Set<String> barDocuments = index.search("bar");

        assertEquals(fooDocuments.size(), 1);
        assertEquals(barDocuments.size(), 1);

        assertTrue(fooDocuments.contains(documentId));
        assertTrue(barDocuments.contains(documentId));
    }
}
