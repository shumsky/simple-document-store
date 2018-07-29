package com.github.shumsky.documentstorecli;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.*;

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
    public void testPut() throws Exception {
        String key = "123";
        String document = "some document";

        cli.run("-put", key, document);

        verify(client).put(key, document);
        verify(printer).print(contains(key));
    }

    @Test
    public void testGet() throws Exception {
        String key = "123";
        String document = "some document";
        when(client.get(any())).thenReturn(Optional.of(document));

        cli.run("-get", key);

        verify(client).get(key);
        verify(printer).print(document);
    }

    @Test
    public void testSearch() throws Exception {
        String documentIds = "101, 102, 103";
        String token1 = "token1";
        String token2 = "token2";
        when(client.getByTokens(any()))
                .thenReturn(Arrays.asList(documentIds.split(", ")));

        cli.run("-search", token1, token2);

        verify(client).getByTokens(Arrays.asList(token1, token2));
        verify(printer).print(documentIds);
    }

    @Test(expected = CliArgSyntaxException.class)
    public void testPutNoValues() throws Exception {
        cli.run("-put");
    }

    @Test(expected = CliArgSyntaxException.class)
    public void testPutMissingValue() throws Exception {
        cli.run("-put", "123");
    }

    @Test(expected = CliArgSyntaxException.class)
    public void testGetNoValues() throws Exception {
        cli.run("-get");
    }

    @Test(expected = CliArgSyntaxException.class)
    public void testSearchNoValues() throws Exception {
        cli.run("-search");
    }

    @Test(expected = CliArgSyntaxException.class)
    public void testNoArgs() throws Exception {
        cli.run();
    }

    @Test(expected = CliArgSyntaxException.class)
    public void testUnknownCommand() throws Exception {
        cli.run("-unknown", "123");
    }
}
