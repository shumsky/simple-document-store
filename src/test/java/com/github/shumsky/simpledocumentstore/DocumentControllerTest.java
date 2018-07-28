package com.github.shumsky.simpledocumentstore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(DocumentController.class)
public class DocumentControllerTest {

    @MockBean
    private DocumentStore store;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetDocument() throws Exception {
        String document = "my document";
        String documentId = "123";

        when(store.find(documentId)).thenReturn(document);

        mockMvc.perform(get("/documents/{id}", documentId).accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().bytes(document.getBytes()));
    }

    @Test
    public void testPutDocument() throws Exception {
        String document = "my document";
        String documentId = "123";

        mockMvc.perform(put("/documents/{id}", documentId).contentType(MediaType.TEXT_PLAIN).content(document))
                .andExpect(status().isCreated()).andReturn();

        verify(store).insert(documentId, document);
    }

    @Test
    public void testPutEmptyDocument() throws Exception {
        String documentId = "123";

        mockMvc.perform(put("/documents/{id}", documentId).contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void testGetDocumentsByTokens() throws Exception {
        String documentId1 = "101";
        String documentId2 = "102";

        Set<String> documentsIds = new HashSet<>();
        documentsIds.add(documentId1);
        documentsIds.add(documentId2);

        when(store.findByKeywords("token1", "token2")).thenReturn(documentsIds);

        mockMvc.perform(get("/documents?tokens=token1,token2").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", containsInAnyOrder(documentId1, documentId2)));
    }

    @Test
    public void testGetNoDocumentsByToken() throws Exception {
        when(store.findByKeywords("token1")).thenReturn(Collections.emptySet());

        mockMvc.perform(get("/documents?tokens=token1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetDocumentsByEmptyTokenList() throws Exception {
        mockMvc.perform(get("/documents?tokens=").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllDocuments() throws Exception {
        mockMvc.perform(get("/documents").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }
}
