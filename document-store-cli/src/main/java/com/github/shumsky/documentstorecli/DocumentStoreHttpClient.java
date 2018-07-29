package com.github.shumsky.documentstorecli;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

public class DocumentStoreHttpClient implements DocumentStoreClient {

    private final String baseUri;
    private final CloseableHttpClient httpClient;

    public DocumentStoreHttpClient(String host, int port) {
        try {
            this.baseUri = new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPort(port)
                    .build().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        httpClient = HttpClients.createDefault();
    }

    @Override
    public void put(String key, String document) {
        HttpPut httpPut = new HttpPut(baseUri + "/documents/" + key);
        httpPut.setEntity(new StringEntity(document, ContentType.create("text/plain", "utf-8")));
        try {
            CloseableHttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201 && statusCode != 200) {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<String> get(String key) {
        HttpGet httpGet = new HttpGet(baseUri + "/documents/" + key);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
                return Optional.of(responseBody);
            } else if (statusCode == 404) {
                return Optional.empty();
            } else {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<String> getByTokens(Collection<String> tokens) {
        return null;
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
