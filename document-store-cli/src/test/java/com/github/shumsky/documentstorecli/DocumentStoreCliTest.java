package com.github.shumsky.documentstorecli;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DocumentStoreCliTest {

    private DocumentStoreClient client;
    private ResponsePrinter printer;
    private DocumentStoreCli cli;

    @Before
    public void init() {
        client = mock(DocumentStoreClient.class);
        printer = mock(ResponsePrinter.class);
        cli = new DocumentStoreCli(client, printer);
    }

    @Test
    public void testPutDocument() {
        String key = "123";
        String document = "some document";

        cli.run("-put", key, document);

        verify(client).put(key, document);
    }

    @Test
    public void testGetDocument() {
        String key = "123";
        String document = "some document";
        when(client.get(key)).thenReturn(Optional.of(document));

        cli.run("-get", key);

        verify(printer).print(document);
    }

    @Test
    public void testGetDocumentsByTokens() {
        String documentIds = "101, 102, 103";
        String token1 = "token1";
        String token2 = "token2";
        when(client.getByTokens(Arrays.asList(token1, token2)))
                .thenReturn(Arrays.asList(documentIds.split(", ")));

        cli.run("-search", token1, token2);

        verify(printer).print(documentIds);
    }
}
