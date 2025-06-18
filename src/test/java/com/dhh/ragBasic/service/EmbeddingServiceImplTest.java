package com.dhh.ragBasic.service;

import com.dhh.ragBasic.model.embedding.EmbeddingResult;
import com.dhh.ragBasic.service.impl.EmbeddingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.embedding.EmbeddingModel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmbeddingServiceImplTest {

    @Mock
    private EmbeddingModel embeddingModel;

    @InjectMocks
    private EmbeddingServiceImpl embeddingService;

    @Test
    @DisplayName("Genera un EmbeddingResult correcto a partir de un chunk de texto")
    void testEmbedChuckReturnsCorrectResult() {
        // Arrange
        String chunkId = "chunk-1";
        String text = "Texto de prueba para embedding.";
        String docId = "doc-1";
        float[] fakeVector = new float[]{0.1f, 0.2f, 0.3f};

        when(embeddingModel.embed(text)).thenReturn(fakeVector);

        // Act
        EmbeddingResult result = embeddingService.embedChuck(chunkId, text, docId);

        // Assert
        assertNotNull(result);
        assertEquals(chunkId, result.getChunkId());
        assertEquals(docId, result.getDocId());
        assertEquals(text, result.getText());
        assertArrayEquals(fakeVector, result.getVector());
    }

    @Test
    @DisplayName("Devuelve el vector adecuado incluso si el texto está vacío")
    void testEmbedTextWithEmptyChuck() {
        String chunkId = "c2";
        String text = "";
        String docId = "d2";
        float[] emptyVector = new float[0];

        when(embeddingModel.embed(text)).thenReturn(emptyVector);

        EmbeddingResult result = embeddingService.embedChuck(chunkId, text, docId);

        assertNotNull(result);
        assertEquals(0, result.getVector().length);
    }


}
