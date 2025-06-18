package com.dhh.ragBasic.service;

import com.dhh.ragBasic.dto.qdrant.QdrantSearchResult;
import com.dhh.ragBasic.model.embedding.EmbeddingResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import reactor.core.publisher.Mono;

import java.util.List;

public interface VectorService {
    Mono<Void> upsertEmbeddings(List<EmbeddingResult> embeddings) throws JsonProcessingException;

    Mono<List<QdrantSearchResult>> searchSimilar(float[] vector, int topK);
}
