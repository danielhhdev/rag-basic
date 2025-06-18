package com.dhh.ragBasic.service.impl;

import com.dhh.ragBasic.dto.qdrant.QdrantSearchResult;
import com.dhh.ragBasic.model.DocumentChunk;
import com.dhh.ragBasic.model.embedding.EmbeddingResult;
import com.dhh.ragBasic.service.RagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio orquestador del pipeline RAG (Retrieval-Augmented Generation).
 *
 * Su objetivo es conectar todos los módulos del sistema:
 * - Extracción de texto (ExtractionService)
 * - Chunking por frases (ChunkingService)
 * - Generación de embeddings (EmbeddingService)
 * - Almacenamiento y búsqueda en vector DB (VectorService/Qdrant)
 *
 * Este servicio ofrece un único punto de entrada para procesar documentos
 * y para consultar el sistema de recuperación semántica.
 */
@Service
@RequiredArgsConstructor
public class RagServiceImpl implements RagService {

    private final ExtractionServiceImpl extractionService;
    private final ChunkingServiceImpl chunkingService;
    private final EmbeddingServiceImpl embeddingService;
    private final VectorServiceImpl vectorService;

    /**
     * Procesa y almacena un documento completo en el sistema vectorial.
     *
     * Flujo:
     *  1. Extrae texto del archivo.
     *  2. Divide el texto en chunks/frases (con solapamiento).
     *  3. Calcula el embedding de cada chunk.
     *  4. Almacena todos los embeddings en Qdrant mediante un batch upsert.
     *
     * @param file      Archivo subido (PDF, DOCX, TXT, etc.)
     * @param chunkSize Número máximo de palabras por chunk.
     * @param overlap   Número de palabras solapadas entre chunks.
     * @param docId     Identificador único del documento (trazabilidad).
     * @return          Mono<Void> (operación reactiva, pero puedes bloquear con .block() en el controller si lo prefieres).
     */
    @Override
    public Mono<Void> processAndStoreDocument(MultipartFile file, int chunkSize, int overlap, String docId) throws JsonProcessingException {
        // 1. Extraer texto
        String text = extractionService.extractText(file);

        // 2. Chunking
        List<DocumentChunk> chunks = chunkingService.chunkTextBySentences(text, chunkSize, overlap, docId);

        // 3. Embeddings para cada chunk
        List<EmbeddingResult> embeddings = chunks.stream()
                .map(chunk -> embeddingService.embedChuck(
                        chunk.getId(), chunk.getText(), docId))
                .collect(Collectors.toList());

        // 4. Batch upsert en Qdrant (persistencia vectorial)
        return vectorService.upsertEmbeddings(embeddings);
    }

    /**
     * Realiza una búsqueda semántica en el sistema vectorial dado un prompt o pregunta.
     *
     * Flujo:
     *  1. Calcula el embedding de la pregunta.
     *  2. Busca los N chunks más similares en Qdrant.
     *  3. (Opcional) Puedes pasar estos chunks a un modelo generativo para respuesta aumentada.
     *
     * @param question Pregunta o consulta en lenguaje natural.
     * @param topK     Número de chunks más similares a devolver.
     * @return         Lista de QdrantSearchResult (chunks relevantes).
     */
    @Override
    public Mono<List<QdrantSearchResult>> queryRag(String question, int topK) {
        // 1. Calcula el embedding de la pregunta
        float[] questionVector = embeddingService.embedText(question);

        // 2. Busca los chunks más similares en Qdrant
        return vectorService.searchSimilar(questionVector, topK);
    }
}
