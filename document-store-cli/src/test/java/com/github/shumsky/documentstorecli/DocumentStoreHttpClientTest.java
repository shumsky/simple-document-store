package com.github.shumsky.documentstorecli;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentStoreHttpClientTest {

    private static final int PORT = 8500;
    private static final String CONTENT_TYPE = "text/plain; charset=UTF-8";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(PORT);

    private DocumentStoreClient client;

    @Before
    public void init() {
        client = new DocumentStoreHttpClient("localhost", PORT);
    }

    @After
    public void after() throws IOException {
        client.close();
    }

    @Test
    public void testPutDocument() {
        String document = "some document";

        stubFor(put(urlEqualTo("/documents/123"))
                .willReturn(aResponse().withStatus(200)));

        client.put("123", document);

        verify(putRequestedFor(urlEqualTo("/documents/123"))
                .withHeader("Content-Type", equalTo(CONTENT_TYPE))
                .withRequestBody(matching(document)));
    }

    @Test
    public void testGetDocument() {
        String document = "some document";

        stubFor(get(urlEqualTo("/documents/123"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", CONTENT_TYPE)
                        .withBody(document)));

        Optional<String> foundDocument = client.get("123");

        assertTrue(foundDocument.isPresent());
        assertEquals(document, foundDocument.get());
    }
}
