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

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8500);

    private DocumentStoreClient client;

    @Before
    public void init() {
        client = new DocumentStoreHttpClient("localhost", 8500);
    }

    @After
    public void after() throws IOException {
        client.close();
    }

    @Test
    public void testGetDocument() {
        String document = "some document";

        stubFor(get(urlEqualTo("/documents/123"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(document)));

        Optional<String> foundDocument = client.get("123");

        assertTrue(foundDocument.isPresent());
        assertEquals(document, foundDocument.get());
    }
}
