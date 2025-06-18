package com.dhh.ragBasic.controller;
import com.dhh.ragBasic.dto.RagQueryRequestDTO;
import com.dhh.ragBasic.dto.qdrant.QdrantSearchResult;
import com.dhh.ragBasic.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    /**
     * Realiza una consulta semántica:
     * 1. Calcula el embedding de la pregunta.
     * 2. Busca los N chunks más similares en Qdrant.
     * 3. Devuelve los resultados.
     *
     * @param request DTO con el prompt/pregunta y el número de resultados.
     */
    @PostMapping("/query")
    public Mono<ResponseEntity<List<QdrantSearchResult>>> queryRag(@RequestBody RagQueryRequestDTO request) {
        return ragService.queryRag(request.getPrompt(), request.getTopK())
                .map(ResponseEntity::ok);
    }
}