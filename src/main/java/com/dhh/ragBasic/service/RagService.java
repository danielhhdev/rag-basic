package com.dhh.ragBasic.service;

import com.dhh.ragBasic.dto.qdrant.QdrantSearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RagService {

    Mono<Void> processAndStoreDocument(MultipartFile file, int chunkSize, int overlap, String docId) throws JsonProcessingException;
    Mono<List<QdrantSearchResult>> queryRag(String question, int topK);

    String answerWithRag(String question);

}
