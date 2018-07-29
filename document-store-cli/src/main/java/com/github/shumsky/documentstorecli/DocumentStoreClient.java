package com.github.shumsky.documentstorecli;

import java.util.Collection;
import java.util.Optional;

public interface DocumentStoreClient {

    void put(String key, String document);

    Optional<String> get(String key);

    Collection<String> getByTokens(Collection<String> tokens);
}
