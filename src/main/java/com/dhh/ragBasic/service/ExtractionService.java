package com.dhh.ragBasic.service;

import org.springframework.web.multipart.MultipartFile;

public interface ExtractionService {

    String extractText(MultipartFile file);
}
