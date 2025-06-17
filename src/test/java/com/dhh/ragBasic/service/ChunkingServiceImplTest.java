package com.dhh.ragBasic.service;

import com.dhh.ragBasic.model.DocumentChunk;
import com.dhh.ragBasic.service.impl.ChunkingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ChunkingServiceImplTest {

    @InjectMocks
    private ChunkingServiceImpl chunkingService;

    @Test
    @DisplayName("Chunking básico por frases, solapando correctamente")
    void testChunkingBySentences_NormalCase() {
        String texto = "Esta es la primera frase. Aquí viene la segunda. " +
                "Y una tercera frase para probar. Por último, la cuarta frase.";

        int chunkSize = 6; // palabras por chunk
        int overlap = 2;   // palabras solapadas
        String docId = UUID.randomUUID().toString();

        List<DocumentChunk> chunks = chunkingService.chunkTextBySentences(texto, chunkSize, overlap, docId);

        assertNotNull(chunks);
        assertFalse(chunks.isEmpty());
        // Debe haber más de un chunk (por el tamaño de chunkSize)
        assertTrue(chunks.size() > 1);

        // Cada chunk no puede exceder chunkSize (salvo casos de frase larga)
        for (DocumentChunk chunk : chunks) {
            int wordCount = chunk.getText().split("\\s+").length;
            assertTrue(wordCount <= chunkSize || chunk.getStartSentenceIdx() == chunk.getEndSentenceIdx());
            assertEquals(docId, chunk.getDocId());
            assertNotNull(chunk.getId());
        }
    }

    @Test
    @DisplayName("Chunking fuerza inclusión de frase si es más larga que chunkSize")
    void testChunking_FragmentoMuyLargo() {
        String texto = "EstaFraseEsExtremadamenteLargaYNoDebeSerCortadaAunSiSuperaElChunkSize.";
        int chunkSize = 2; // muy pequeño comparado con la frase
        int overlap = 1;
        String docId = "doc-prueba";

        List<DocumentChunk> chunks = chunkingService.chunkTextBySentences(texto, chunkSize, overlap, docId);
        assertEquals(1, chunks.size());
        assertTrue(chunks.get(0).getText().contains("EstaFraseEsExtremadamenteLarga"));
    }

    @Test
    @DisplayName("Si overlap >= chunkSize, el método lo ajusta para evitar bucles infinitos")
    void testChunking_OverlapMayorChunkSize() {
        String texto = "Primera frase. Segunda frase corta. Otra frase.";
        int chunkSize = 3;
        int overlap = 4; // mayor que chunkSize
        String docId = "doc-test";

        List<DocumentChunk> chunks = chunkingService.chunkTextBySentences(texto, chunkSize, overlap, docId);
        assertFalse(chunks.isEmpty());
    }

    @Test
    @DisplayName("Chunking de texto muy corto: solo un chunk")
    void testChunking_TextoCorto() {
        String texto = "Solo una frase corta.";
        int chunkSize = 10;
        int overlap = 3;
        String docId = "corto";

        List<DocumentChunk> chunks = chunkingService.chunkTextBySentences(texto, chunkSize, overlap, docId);
        assertEquals(1, chunks.size());
        assertTrue(chunks.get(0).getText().contains("Solo una frase corta"));
    }
}
