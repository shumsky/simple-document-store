package com.github.shumsky.simpledocumentstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController("/documents")
public class DocumentController {

    private final DocumentStore documentStore;

    @Autowired
    public DocumentController(DocumentStore documentStore) {
        this.documentStore = documentStore;
    }

    @GetMapping("/{documentId}")
    public @ResponseBody String get(@PathVariable String documentId) {
        return documentStore.find(documentId);
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<Void> add(@PathVariable String documentId, @RequestBody String document, UriComponentsBuilder b) {
        documentStore.insert(documentId, document);
        return ResponseEntity.created(b.path("/documents/{id}").build(documentId)).build();
    }
}
