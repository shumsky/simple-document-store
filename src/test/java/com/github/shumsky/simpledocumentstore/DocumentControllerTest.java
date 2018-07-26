package com.github.shumsky.simpledocumentstore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        mockMvc.perform(get("/{id}", documentId).accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().bytes(document.getBytes()));
    }

    @Test
    public void testPutDocument() throws Exception {
        String document = "my document";
        String documentId = "123";

        mockMvc.perform(put("/{id}", documentId).contentType(MediaType.TEXT_PLAIN).content(document))
                .andExpect(status().isCreated()).andReturn();

        verify(store).insert(documentId, document);
    }
}
