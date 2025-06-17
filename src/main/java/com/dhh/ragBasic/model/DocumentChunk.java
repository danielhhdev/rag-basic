package com.dhh.ragBasic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentChunk {
    private String id;
    private int startSentenceIdx;
    private int endSentenceIdx;
    private String text;
    private String docId;
}