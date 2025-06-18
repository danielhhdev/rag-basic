package com.dhh.ragBasic.service;

import com.dhh.ragBasic.model.embedding.EmbeddingResult;

public interface EmbeddingService {

    EmbeddingResult embedChuck(String chunkId, String text, String docId);

    float[] embedText(String text);
}
