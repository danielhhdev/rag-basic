package com.dhh.ragBasic.model.qdrant;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class QdrantPoint {
    private String id;
    private float[] vector;
    private Map<String, Object> payload;
}