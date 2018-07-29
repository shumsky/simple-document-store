package com.github.shumsky.documentstorecli;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class DocumentStoreCli {

    private static final String USAGE = "Usage: ...";

    private static final String COMMAND_PUT = "-put";
    private static final String COMMAND_GET = "-get";
    private static final String COMMAND_SEARCH = "-search";
    private static final String MESSAGE_NOT_FOUND = "[Not found]";

    private final DocumentStoreClient client;
    private final ResponsePrinter printer;

    public DocumentStoreCli(DocumentStoreClient client, ResponsePrinter printer) {
        this.client = client;
        this.printer = printer;
    }

    public static void main(String[] args) {
        DocumentStoreHttpClient client = new DocumentStoreHttpClient("localhost", 8500);
        StdoutResponsePrinter printer = new StdoutResponsePrinter();
        try {
            new DocumentStoreCli(client, printer).run(args);
        } catch (CliArgSyntaxException e) {
            printer.print(USAGE);
        }
    }

    public void run(String... args) throws CliArgSyntaxException {
        Map<String, List<String>> parsedArgs = parseArgs(args);
        validate(parsedArgs);


        if (parsedArgs.containsKey(COMMAND_PUT)) {

            List<String> values = parsedArgs.get(COMMAND_PUT);
            client.put(values.get(0), values.get(1));
            printer.print("[Put key + '" + values.get(0) + "']");

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

    private Map<String, List<String>> parseArgs(String[] args) {
        Map<String, List<String>> parsedArgs = new HashMap<>();
        List<String> currentParamValues = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                currentParamValues = new ArrayList<>();
                parsedArgs.put(args[i], currentParamValues);
            } else {
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
