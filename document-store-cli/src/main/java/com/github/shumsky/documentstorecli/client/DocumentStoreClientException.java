package com.github.shumsky.documentstorecli.client;

public class DocumentStoreClientException extends RuntimeException {
    public DocumentStoreClientException() {
    }

    public DocumentStoreClientException(String message) {
        super(message);
    }

    public DocumentStoreClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
