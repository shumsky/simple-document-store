package com.github.shumsky.documentstorecli;

import com.github.shumsky.documentstorecli.client.DocumentStoreClient;
import com.github.shumsky.documentstorecli.client.DocumentStoreClientException;
import com.github.shumsky.documentstorecli.client.DocumentStoreHttpClient;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class DocumentStoreCli {

    private static final String USAGE = "Usage: \n" +
            "  -put <key> <value> - put a document\n" +
            "  -get <key> - get a document by key\n" +
            "  -search <token1> <token2> ... <tokenN> - search document IDs by tokens\n";

    private static final String COMMAND_PUT = "-put";
    private static final String COMMAND_GET = "-get";
    private static final String COMMAND_SEARCH = "-search";

    private static final String MESSAGE_NOT_FOUND = "[Not found]";
    private static final String MESSAGE_PUT_KEY = "[Put key '%s']";
    private static final String MESSAGE_ERROR = "[Error: %s]";

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8500;

    private final DocumentStoreClient client;
    private final MessagePrinter printer;

    public DocumentStoreCli(DocumentStoreClient client, MessagePrinter printer) {
        this.client = client;
        this.printer = printer;
    }

    public static void main(String[] args) {
        DocumentStoreHttpClient client = new DocumentStoreHttpClient(DEFAULT_HOST, DEFAULT_PORT);
        StdoutMessagePrinter printer = new StdoutMessagePrinter();
        try {
            new DocumentStoreCli(client, printer).run(args);
        } catch (CliArgSyntaxException e) {
            printer.print(USAGE);
        }
    }

    public void run(String... args) throws CliArgSyntaxException {
        Map<String, List<String>> parsedArgs = parseArgs(args);

        validate(parsedArgs);

        try {
            process(parsedArgs);
        } catch (DocumentStoreClientException e) {
            printer.print(String.format(MESSAGE_ERROR, e.getMessage()));
        }
    }

    private void process(Map<String, List<String>> parsedArgs) {
        if (parsedArgs.containsKey(COMMAND_PUT)) {

            List<String> values = parsedArgs.get(COMMAND_PUT);
            client.put(values.get(0), values.get(1));
            printer.print(String.format(MESSAGE_PUT_KEY, values.get(0)));

        } else if (parsedArgs.containsKey(COMMAND_GET)) {

            List<String> values = parsedArgs.get(COMMAND_GET);
            Optional<String> document = client.get(values.get(0));
            printer.print(document.orElse(MESSAGE_NOT_FOUND));

        } else if (parsedArgs.containsKey(COMMAND_SEARCH)) {

            List<String> values = parsedArgs.get(COMMAND_SEARCH);
            Collection<String> documentIds = client.getByTokens(values);

            if (!documentIds.isEmpty()) {
                printer.print(documentIds.stream().collect(joining(", ")));
            } else {
                printer.print(MESSAGE_NOT_FOUND);
            }
        }
    }

    private Map<String, List<String>> parseArgs(String[] args) throws CliArgSyntaxException {
        Map<String, List<String>> parsedArgs = new HashMap<>();
        List<String> currentParamValues = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                currentParamValues = new ArrayList<>();
                parsedArgs.put(args[i], currentParamValues);
            } else {
                if (currentParamValues == null) {
                    throw new CliArgSyntaxException();
                }
                currentParamValues.add(args[i]);
            }
        }

        return parsedArgs;
    }

    private void validate(Map<String, List<String>> parsedArgs) throws CliArgSyntaxException {

        long commandsSpecified = Stream.of(COMMAND_PUT, COMMAND_GET, COMMAND_SEARCH)
                .map(parsedArgs::get).filter(Objects::nonNull).count();

        if (commandsSpecified != 1) {
            throw new CliArgSyntaxException();
        }

        if (parsedArgs.containsKey(COMMAND_PUT) && parsedArgs.get(COMMAND_PUT).size() < 2) {
            throw new CliArgSyntaxException();
        }
        if (parsedArgs.containsKey(COMMAND_GET) && parsedArgs.get(COMMAND_GET).size() < 1) {
            throw new CliArgSyntaxException();
        }
        if (parsedArgs.containsKey(COMMAND_SEARCH) && parsedArgs.get(COMMAND_SEARCH).size() < 1) {
            throw new CliArgSyntaxException();
        }
    }
}
