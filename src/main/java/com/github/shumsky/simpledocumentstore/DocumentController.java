package com.github.shumsky.simpledocumentstore;

import com.github.shumsky.simpledocumentstore.store.DocumentStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@RestController
public class DocumentController {

    private final DocumentStore documentStore;

    @Autowired
    public DocumentController(DocumentStore documentStore) {
        this.documentStore = documentStore;
    }

    @GetMapping("/documents/{documentId}")
    public @ResponseBody ResponseEntity<String> get(@PathVariable String documentId) {
        return documentStore.find(documentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/documents")
    public @ResponseBody Set<String> getByTerms(@RequestParam("tokens") String tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("'tokens' must be non-empty");
        }
        return documentStore.findByKeywords(tokens.split(","));
    }

    @PutMapping("/documents/{documentId}")
    public ResponseEntity<Void> add(@PathVariable String documentId, @RequestBody String document, UriComponentsBuilder b) {
        documentStore.insert(documentId, document);
        return ResponseEntity.created(b.path("/documents/{id}").build(documentId)).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> exceptionHandler() {
        return ResponseEntity.badRequest().build();
    }
}
