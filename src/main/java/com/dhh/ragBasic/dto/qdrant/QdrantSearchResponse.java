package com.dhh.ragBasic.dto.qdrant;


import lombok.Data;

import java.util.List;

@Data
public class QdrantSearchResponse {
    private List<QdrantSearchResult> result;
}