package com.github.shumsky.documentstorecli;

public class StdoutResponsePrinter implements ResponsePrinter {

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
