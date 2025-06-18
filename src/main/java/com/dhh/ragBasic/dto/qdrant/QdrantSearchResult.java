package com.dhh.ragBasic.dto.qdrant;

import lombok.Data;

import java.util.Map;

@Data
public class QdrantSearchResult {
    private String id;
    private float score;
    private Map<String, Object> payload;
}