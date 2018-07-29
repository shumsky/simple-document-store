package com.github.shumsky.documentstorecli;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class DocumentStoreHttpClient implements DocumentStoreClient {

    private static final int DEFAULT_TIMEOUT_MILLIS = 10000;

    private final String baseUri;
    private final CloseableHttpClient httpClient;
    private final RequestConfig requestConfig;

    public DocumentStoreHttpClient(String host, int port) {
        this(host, port, DEFAULT_TIMEOUT_MILLIS);
    }

    public DocumentStoreHttpClient(String host, int port, int timeoutMillis) {
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
        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeoutMillis)
                .setConnectTimeout(timeoutMillis)
                .setSocketTimeout(timeoutMillis)
                .build();
    }

    @Override
    public void put(String key, String document) {
        HttpPut httpPut = new HttpPut(baseUri + "/documents/" + key);
        httpPut.setConfig(requestConfig);
        httpPut.setEntity(new StringEntity(document, ContentType.create("text/plain", "utf-8")));
        try {
            CloseableHttpResponse response = httpClient.execute(httpPut);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201 && statusCode != 200) {
                throw new DocumentStoreClientException("Bad response: " + statusCode);
            }
        } catch (IOException e) {
            throw new DocumentStoreClientException("Failed to communicate with DocumentStore", e);
        }
    }

    @Override
    public Optional<String> get(String key) {
        HttpGet httpGet = new HttpGet(baseUri + "/documents/" + key);
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");
                return Optional.of(responseBody);
            } else if (statusCode == 404) {
                return Optional.empty();
            } else {
                throw new DocumentStoreClientException("Bad response: " + statusCode);
            }
        } catch (IOException e) {
            throw new DocumentStoreClientException("Failed to communicate with DocumentStore", e);
        }
    }

    @Override
    public Collection<String> getByTokens(Collection<String> tokens) {
        String tokensParam = tokens.stream().collect(joining(","));
        HttpGet httpGet = new HttpGet(baseUri + "/documents?tokens=" + tokensParam);
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity(), "utf-8");

                List<String> documentIds = new JSONArray(responseBody).toList().stream().map(el -> {
                    if (!(el instanceof String)) {
                        throw new DocumentStoreClientException("Invalid response");
                    }
                    return (String) el;
                }).collect(toList());

                return documentIds;
            } else {
                throw new DocumentStoreClientException("Bad response: " + statusCode);
            }
        } catch (IOException e) {
            throw new DocumentStoreClientException("Failed to communicate with DocumentStore", e);
        } catch (JSONException e) {
            throw new DocumentStoreClientException("Invalid response", e);
        }
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
