package com.dhh.ragBasic.service;



import com.dhh.ragBasic.dto.qdrant.QdrantSearchResult;
import com.dhh.ragBasic.model.DocumentChunk;
import com.dhh.ragBasic.model.embedding.EmbeddingResult;
import com.dhh.ragBasic.service.impl.ChunkingServiceImpl;
import com.dhh.ragBasic.service.impl.EmbeddingServiceImpl;
import com.dhh.ragBasic.service.impl.ExtractionServiceImpl;
import com.dhh.ragBasic.service.impl.RagServiceImpl;
import com.dhh.ragBasic.service.impl.VectorServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RagServiceImplTest {

    @Mock
    private ExtractionServiceImpl extractionService;
    @Mock
    private ChunkingServiceImpl chunkingService;
    @Mock
    private EmbeddingServiceImpl embeddingService;
    @Mock
    private VectorServiceImpl vectorService;

    @InjectMocks
    private RagServiceImpl ragService;


    @Test
    void processAndStoreDocument_shouldExecuteFullPipeline() throws JsonProcessingException {
        // Arrange
        String docId = UUID.randomUUID().toString();
        String fileText = "Texto de ejemplo.";
        int chunkSize = 20;
        int overlap = 5;

        MockMultipartFile file = new MockMultipartFile("file", "prueba.txt", "text/plain", fileText.getBytes());

        DocumentChunk chunk = new DocumentChunk(UUID.randomUUID().toString(), 0, 0, fileText, docId);
        EmbeddingResult embedding = new EmbeddingResult(chunk.getId(), new float[]{0.1f, 0.2f}, fileText, docId);

        when(extractionService.extractText(file)).thenReturn(fileText);
        when(chunkingService.chunkTextBySentences(fileText, chunkSize, overlap, docId)).thenReturn(List.of(chunk));
        when(embeddingService.embedChuck(chunk.getId(), fileText, docId)).thenReturn(embedding);
        when(vectorService.upsertEmbeddings(List.of(embedding))).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(ragService.processAndStoreDocument(file, chunkSize, overlap, docId))
                .verifyComplete();

        verify(extractionService).extractText(file);
        verify(chunkingService).chunkTextBySentences(fileText, chunkSize, overlap, docId);
        verify(embeddingService).embedChuck(chunk.getId(), fileText, docId);
        verify(vectorService).upsertEmbeddings(List.of(embedding));
    }

    @Test
    void queryRag_shouldReturnSearchResults() {
        // Arrange
        String question = "¿Cuál es la IA más usada?";
        int topK = 2;
        float[] questionVec = new float[]{0.3f, 0.4f};
        QdrantSearchResult res = new QdrantSearchResult();

        when(embeddingService.embedText(question)).thenReturn(questionVec);
        when(vectorService.searchSimilar(questionVec, topK)).thenReturn(Mono.just(List.of(res)));

        // Act & Assert
        StepVerifier.create(ragService.queryRag(question, topK))
                .assertNext(results -> assertEquals(1, results.size()))
                .verifyComplete();

        verify(embeddingService).embedText(question);
        verify(vectorService).searchSimilar(questionVec, topK);
    }
}