package com.dhh.ragBasic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmbeddingResult {
    private String chunkId;
    private float[] vector;
    private String text;
    private String docId;
}