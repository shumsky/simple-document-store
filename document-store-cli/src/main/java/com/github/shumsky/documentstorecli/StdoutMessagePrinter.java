package com.github.shumsky.documentstorecli;

public class StdoutMessagePrinter implements MessagePrinter {

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
