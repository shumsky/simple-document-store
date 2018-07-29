package com.github.shumsky.documentstorecli;

import java.io.Closeable;
import java.util.Collection;
import java.util.Optional;

public interface DocumentStoreClient extends Closeable {

    void put(String key, String document);

    Optional<String> get(String key);

    Collection<String> getByTokens(Collection<String> tokens);
}
