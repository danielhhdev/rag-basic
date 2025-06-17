package com.dhh.ragBasic.service;

import com.dhh.ragBasic.model.EmbeddingResult;

public interface EmbeddingService {

    EmbeddingResult embedText(String chunkId, String text, String docId);
}
