package com.dhh.ragBasic.service.impl;

import com.dhh.ragBasic.service.ExtractionService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ExtractionServiceImpl implements ExtractionService {

    private final Tika tika;

    @Override
    public String extractText(MultipartFile file) {
        try {
            return tika.parseToString(file.getInputStream())
                    .replaceAll("\\s+", " ") // Limpiar saltos de línea, tabs, etc.
                    .trim();
        } catch (TikaException | IOException e) {
            throw new RuntimeException("Error extrayendo texto del documento: " + file.getOriginalFilename(), e);
        }
    }
}
