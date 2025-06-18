package com.dhh.ragBasic.model.qdrant;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QdrantUpsertRequest {
    private List<QdrantPoint> points;
}
