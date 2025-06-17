package com.dhh.ragBasic.controller;

import com.dhh.ragBasic.model.DocumentChunk;
import com.dhh.ragBasic.service.ChunkingService;
import com.dhh.ragBasic.service.ExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

    private final ExtractionService extractionService;
    private final ChunkingService chunkingService;

    @PostMapping("/extract")
    public ResponseEntity<List<DocumentChunk>> extractText(@RequestParam("file") MultipartFile file,
                                                           @RequestParam int chuckSize,
                                                           @RequestParam int overlap,
                                                           @RequestParam String docId) {
        String text = extractionService.extractText(file);
        List<DocumentChunk> chunks = chunkingService.chunkTextBySentences(text, chuckSize, overlap, docId);
        return ResponseEntity.ok(chunks);
    }
}
