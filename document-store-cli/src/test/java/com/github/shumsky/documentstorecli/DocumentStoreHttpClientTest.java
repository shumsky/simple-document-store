package com.github.shumsky.documentstorecli;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DocumentStoreHttpClientTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8500);

    @Test
    public void testGetDocument() {
        DocumentStoreClient client = new DocumentStoreHttpClient("localhost", 8500);
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
