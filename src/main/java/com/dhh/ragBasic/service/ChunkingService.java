package com.dhh.ragBasic.service;

import com.dhh.ragBasic.model.DocumentChunk;

import java.util.List;

public interface ChunkingService {

    List<DocumentChunk> chunkTextWithOverlap(String text);

    List<DocumentChunk> chunkTextBySentences(String text, int chunkSize, int overlap, String docId);
}
