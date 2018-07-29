package com.github.shumsky.documentstorecli;

public class CliArgSyntaxException extends Exception {
    public CliArgSyntaxException() {
    }

    public CliArgSyntaxException(String message) {
        super(message);
    }

    public CliArgSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }
}
