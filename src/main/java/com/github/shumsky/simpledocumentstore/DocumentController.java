package com.github.shumsky.simpledocumentstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/documents")
public class DocumentController {

    private final DocumentStore documentStore;

    @Autowired
    public DocumentController(DocumentStore documentStore) {
        this.documentStore = documentStore;
    }

    @GetMapping("/{documentId}")
    public @ResponseBody String find(@PathVariable String documentId) {
        return documentStore.find(documentId);
    }
}
