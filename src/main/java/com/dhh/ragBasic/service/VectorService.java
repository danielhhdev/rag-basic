package com.dhh.ragBasic.service;

import com.dhh.ragBasic.dto.qdrant.QdrantSearchResult;
import com.dhh.ragBasic.model.embedding.EmbeddingResult;
import reactor.core.publisher.Mono;

import java.util.List;

public interface VectorService {
    Mono<Void> upsertEmbeddings(List<EmbeddingResult> embeddings);

    Mono<List<QdrantSearchResult>> searchSimilar(float[] vector, int topK);
}
