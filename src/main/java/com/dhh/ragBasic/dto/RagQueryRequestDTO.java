package com.dhh.ragBasic.dto;

import lombok.Data;

@Data
public class RagQueryRequestDTO {
    private String prompt;
    private int topK = 5;
}