package com.dhh.ragBasic.service;

import com.dhh.ragBasic.config.QdrantConfig;
import com.dhh.ragBasic.dto.qdrant.QdrantSearchResponse;
import com.dhh.ragBasic.dto.qdrant.QdrantSearchResult;
import com.dhh.ragBasic.model.embedding.EmbeddingResult;
import com.dhh.ragBasic.service.impl.VectorServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VectorServiceImplTest {

    @Mock
    private WebClient qdrantWebClient;

    @Mock
    private QdrantConfig qdrantConfig;

    @Mock
    private RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RequestHeadersSpec requestHeadersSpec;

    @Mock
    private ResponseSpec responseSpec;

    @Captor
    private ArgumentCaptor<String> uriCaptor;

    @InjectMocks
    private VectorServiceImpl vectorService;


    @Test
    @DisplayName("Debe hacer un upsert batch correctamente en Qdrant")
    void testUpsertEmbeddings() throws JsonProcessingException {
        // Arrange
        EmbeddingResult embedding = new EmbeddingResult("chunkId", new float[]{1.0f, 2.0f, 3.0f}, "texto de chunk", "docId1");
        List<EmbeddingResult> embeddings = List.of(embedding);

        when(qdrantConfig.getCollection()).thenReturn("test-collection");
        // Mock chain de WebClient para POST
        when(qdrantWebClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = vectorService.upsertEmbeddings(embeddings);

        // Assert
        assertNotNull(result);
        verify(qdrantWebClient).put();
        verify(requestBodyUriSpec).uri(uriCaptor.capture());
        String expectedUri = "/collections/test-collection/points?wait=true";
        assertEquals(expectedUri, uriCaptor.getValue());
    }

    @Test
    @DisplayName("Debe buscar chunks similares correctamente en Qdrant")
    void testSearchSimilar() {
        float[] vector = new float[]{0.2f, 0.5f};
        int topK = 3;
        QdrantSearchResult mockResult = new QdrantSearchResult();
        mockResult.setId("chunk1");
        mockResult.setScore(0.9f);
        mockResult.setPayload(Map.of("text", "algo", "docId", "docid-xyz"));

        QdrantSearchResponse mockResponse = new QdrantSearchResponse();
        mockResponse.setResult(List.of(mockResult));

        when(qdrantConfig.getCollection()).thenReturn("test-collection");
        // Mock chain de WebClient para POST
        when(qdrantWebClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(QdrantSearchResponse.class)).thenReturn(Mono.just(mockResponse));

        // Act
        List<QdrantSearchResult> results = vectorService.searchSimilar(vector, topK).block();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("chunk1", results.get(0).getId());
        assertEquals(0.9f, results.get(0).getScore());
        assertEquals("algo", results.get(0).getPayload().get("text"));
    }
}
