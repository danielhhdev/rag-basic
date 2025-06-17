package com.dhh.ragBasic.service.impl;

import com.dhh.ragBasic.model.DocumentChunk;
import com.dhh.ragBasic.service.ChunkingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de chunking para dividir textos largos en fragmentos (chunks) óptimos,
 * respetando los límites de palabras por chunk y garantizando que nunca se partan frases.
 *
 * Implementa solapamiento (overlapping) entre chunks para mantener el contexto,
 * y es robusto ante textos y parámetros inusuales.
 *
 * Proyecto orientado al aprendizaje de pipelines RAG y procesamiento de lenguaje natural en Java/Spring Boot.
 */
@Service
public class ChunkingServiceImpl implements ChunkingService {

    // Tamaño por defecto de cada chunk (en palabras)
    private static final int DEFAULT_CHUNK_SIZE = 300;
    // Número de palabras solapadas entre chunks por defecto
    private static final int DEFAULT_OVERLAP = 50;

    /**
     * Chunking rápido usando los valores por defecto y un docId aleatorio.
     *
     * @param text Texto completo a dividir.
     * @return Lista de chunks (DocumentChunk), listos para embeddings o búsqueda vectorial.
     */
    public List<DocumentChunk> chunkTextWithOverlap(String text) {
        return chunkTextBySentences(text, DEFAULT_CHUNK_SIZE, DEFAULT_OVERLAP, UUID.randomUUID().toString());
    }

    /**
     * Divide el texto en chunks de frases completas, con un máximo de palabras por chunk
     * y solapando frases entre chunks para no perder contexto.
     *
     * - Si una frase es más larga que el chunk, se fuerza su inclusión como chunk único.
     * - Si el overlap es mayor o igual al chunkSize, se ajusta automáticamente para evitar bucles infinitos.
     *
     * @param text Texto a chunkear.
     * @param chunkSize Número máximo de palabras por chunk.
     * @param overlap Palabras solapadas entre chunks.
     * @param docId Identificador único del documento (para trazabilidad en pipelines multi-documento).
     * @return Lista de objetos DocumentChunk con metadatos y texto.
     */
    public List<DocumentChunk> chunkTextBySentences(String text, int chunkSize, int overlap, String docId) {

        // Validación y corrección de parámetros para evitar bucles infinitos
        if (overlap >= chunkSize) {
            overlap = Math.max(chunkSize / 2, 1);
        }

        List<String> sentences = splitToSentences(text);
        List<DocumentChunk> chunks = new ArrayList<>();
        int start = 0;

        // Bucle principal de chunking
        while (start < sentences.size()) {
            int wordCount = 0;
            int end = start;

            // Acumula frases completas hasta alcanzar el máximo de palabras permitido
            while (end < sentences.size() && wordCount + countWords(sentences.get(end)) <= chunkSize) {
                wordCount += countWords(sentences.get(end));
                end++;
            }
            // Si la primera frase es más larga que el chunk, se incluye forzadamente
            if (end == start) {
                end++;
            }
            String chunkText = joinSentences(sentences, start, end);

            // Crea el chunk con metadatos relevantes
            chunks.add(new DocumentChunk(
                    UUID.randomUUID().toString(), // ID único del chunk
                    start,                        // Índice de frase inicial
                    end - 1,                      // Índice de frase final
                    chunkText,                    // Texto del chunk
                    docId                         // ID del documento original
            ));

            // Lógica de solapamiento: retrocede lo necesario para solapar frases
            int overlapWords = 0;
            int newStart = end - 1;
            while (newStart > start && overlapWords < overlap) {
                overlapWords += countWords(sentences.get(newStart));
                newStart--;
            }
            int prevStart = start;
            start = Math.max(newStart + 1, end);

            // Salvaguarda adicional para evitar bucles infinitos en casos límite
            if (start <= prevStart) {
                break;
            }
        }
        return chunks;
    }

    /**
     * Separa un texto en frases usando una expresión regular sencilla.
     *
     * Nota: Para idiomas complejos o soporte multilenguaje, puedes mejorar usando una librería NLP.
     *
     * @param text Texto completo a dividir.
     * @return Lista de frases.
     */
    private List<String> splitToSentences(String text) {
        // Regex que separa por ".", "?" o "!" seguidos de espacio
        return Arrays.asList(text.split("(?<=[.!?])\\s+"));
    }

    /**
     * Cuenta las palabras no vacías de una frase.
     *
     * @param sentence Frase a analizar.
     * @return Número de palabras en la frase.
     */
    private int countWords(String sentence) {
        return (int) Arrays.stream(sentence.trim().split("\\s+"))
                .filter(w -> !w.isBlank()).count();
    }

    /**
     * Une un subconjunto de frases en un solo string, separadas por espacio.
     *
     * @param sentences Lista de frases.
     * @param start Índice de inicio (incluido).
     * @param end Índice de fin (excluido).
     * @return Texto combinado.
     */
    private String joinSentences(List<String> sentences, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++) {
            sb.append(sentences.get(i)).append(" ");
        }
        return sb.toString().trim();
    }
}
