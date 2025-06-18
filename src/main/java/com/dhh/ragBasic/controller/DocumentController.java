package com.dhh.ragBasic.controller;

import com.dhh.ragBasic.model.DocumentChunk;
import com.dhh.ragBasic.service.RagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/documents")
public class DocumentController {

    private final RagService ragService;

    @PostMapping("/upload")
    public Mono<ResponseEntity<String>> extractText(@RequestParam("file") MultipartFile file,
                                                    @RequestParam int chuckSize,
                                                    @RequestParam int overlap,
                                                    @RequestParam String docId) throws JsonProcessingException {

        return ragService.processAndStoreDocument(file, chuckSize, overlap, docId)
                .thenReturn(ResponseEntity.ok("Documento procesado correctamente. ID: " + docId));
    }
}
