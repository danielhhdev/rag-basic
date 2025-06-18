package com.dhh.ragBasic.service.impl;

import com.dhh.ragBasic.model.embedding.EmbeddingResult;
import com.dhh.ragBasic.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de obtener los embeddings vectoriales de fragmentos de texto ("chunks"),
 * utilizando el modelo de embeddings configurado (por ejemplo, Ollama local, OpenAI, Cohere, etc.).
 * <p>
 * Los embeddings son representaciones matemáticas (vectores de floats) que permiten comparar
 * semánticamente textos, buscar similitudes, o almacenar el conocimiento de un documento
 * en una base de datos vectorial como Qdrant.
 * <p>
 * Esta implementación está pensada para integrarse fácilmente con Spring AI y proyectos
 * de aprendizaje sobre RAG (Retrieval-Augmented Generation) en Java/Spring Boot.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingServiceImpl implements EmbeddingService {

    /**
     * Cliente/modelo de embeddings proporcionado por Spring AI, configurado vía application.yml.
     * Puede ser Ollama, OpenAI, Cohere, etc., según la configuración del proyecto.
     */
    private final EmbeddingModel embeddingModel;

    /**
     * Genera el embedding vectorial para un chunk de texto.
     *
     * @param chunkId Identificador único del fragmento de texto.
     * @param text    Texto a embebir (normalmente un chunk del documento original).
     * @param docId   Identificador del documento fuente (para trazabilidad y búsquedas).
     * @return Un EmbeddingResult con el vector generado, el texto original y los metadatos.
     */
    @Override
    public EmbeddingResult embedChuck(String chunkId, String text, String docId) {
        log.info("Se hace el embedding del chuck: {}", chunkId);
        // Llama al modelo configurado para obtener el embedding (vector de floats)
        float[] vector = embeddingModel.embed(text);

        // Construye el resultado con el vector, el texto y los metadatos
        return new EmbeddingResult(chunkId, vector, text, docId);
    }

    public float[] embedText(String text) {
        log.info("Se hace el embedding del texto: {}", text);
        return embeddingModel.embed(text);
    }
}