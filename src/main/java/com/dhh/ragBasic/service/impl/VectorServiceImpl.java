package com.dhh.ragBasic.service.impl;

import com.dhh.ragBasic.config.QdrantConfig;
import com.dhh.ragBasic.dto.qdrant.QdrantSearchResponse;
import com.dhh.ragBasic.dto.qdrant.QdrantSearchResult;
import com.dhh.ragBasic.model.embedding.EmbeddingResult;
import com.dhh.ragBasic.model.qdrant.QdrantPoint;
import com.dhh.ragBasic.model.qdrant.QdrantUpsertRequest;
import com.dhh.ragBasic.service.VectorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de la integración con Qdrant (vector database) para almacenar y consultar
 * embeddings generados a partir de los chunks de texto de documentos.
 *
 * Este servicio representa la capa de persistencia vectorial de un pipeline RAG (Retrieval-Augmented Generation):
 * - Permite almacenar ("upsert") una lista de embeddings en lote (batch) para máxima eficiencia.
 * - Permite buscar los embeddings más similares a un vector dado (búsqueda semántica).
 *
 * Utiliza la API REST de Qdrant vía WebClient (Spring WebFlux) y está totalmente desacoplado,
 * lo que facilita el testing y la adaptación a otros servicios vectoriales en el futuro.
 *
 * Proyecto orientado al aprendizaje de arquitecturas IA modernas con Java y Spring Boot.
 */
@Service
public class VectorServiceImpl implements VectorService {

    /**
     * Cliente WebClient configurado para conectar con la API REST de Qdrant.
     * Se inyecta con @Qualifier para garantizar la instancia correcta si tienes más WebClients.
     */
    private final WebClient qdrantWebClient;

    /**
     * Configuración de Qdrant: URL base, nombre de la colección, etc.
     * Centraliza la gestión de endpoints y propiedades.
     */
    private final QdrantConfig qdrantConfig;

    public VectorServiceImpl(@Qualifier("qdrantWebClient") WebClient qdrantWebClient,
                             QdrantConfig qdrantConfig) {
        this.qdrantWebClient = qdrantWebClient;
        this.qdrantConfig = qdrantConfig;
    }

    /**
     * Inserta en lote (batch upsert) una lista de embeddings (cada uno correspondiente a un chunk de texto)
     * en la colección configurada de Qdrant.
     *
     * Este método es eficiente para almacenar todos los embeddings generados a partir de un documento,
     * minimizando el número de llamadas HTTP y maximizando el rendimiento.
     *
     * @param embeddings Lista de objetos EmbeddingResult (chunkId, vector, texto, docId).
     * @return Mono<Void> reactivo indicando éxito o error de la operación.
     */
    @Override
    public Mono<Void> upsertEmbeddings(List<EmbeddingResult> embeddings) {
        // Mapea cada EmbeddingResult a la estructura esperada por Qdrant (QdrantPoint)
        List<QdrantPoint> points = embeddings.stream()
                .map(embedding -> new QdrantPoint(
                        embedding.getChunkId(),
                        embedding.getVector(),
                        Map.of(
                                "text", embedding.getText(),
                                "docId", embedding.getDocId()
                        )
                )).toList();

        QdrantUpsertRequest request = new QdrantUpsertRequest(points);

        String endpoint = "/collections/" + qdrantConfig.getCollection() + "/points?wait=true";

        // Realiza el POST a Qdrant (REST) para almacenar los embeddings
        return qdrantWebClient.post()
                .uri(endpoint)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }

    /**
     * Realiza una búsqueda de similitud en Qdrant: dado un vector (embedding de una consulta o pregunta),
     * devuelve los N chunks más similares almacenados en la colección (top K).
     *
     * Esencial para la recuperación de contexto en RAG: encuentra los fragmentos relevantes
     * para responder a la pregunta del usuario.
     *
     * @param vector Vector embedding de la consulta.
     * @param topK   Número de resultados más similares a devolver.
     * @return Mono<List<QdrantSearchResult>> Lista de resultados (chunks relevantes) con su score de similitud.
     */
    @Override
    public Mono<List<QdrantSearchResult>> searchSimilar(float[] vector, int topK) {
        Map<String, Object> query = Map.of(
                "vector", vector,
                "top", topK,
                "with_payload", true // Devuelve los metadatos junto al resultado
        );
        String endpoint = "/collections/" + qdrantConfig.getCollection() + "/points/search";

        return qdrantWebClient.post()
                .uri(endpoint)
                .bodyValue(query)
                .retrieve()
                .bodyToMono(QdrantSearchResponse.class)
                .map(QdrantSearchResponse::getResult);
    }
}
