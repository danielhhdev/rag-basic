package com.dhh.ragBasic.service.impl;

import com.dhh.ragBasic.service.ExtractionService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
/**
 * Servicio encargado de la extracción automática de texto desde documentos subidos por el usuario.
 *
 * Utiliza Apache Tika, una librería robusta para parsing y extracción de texto desde múltiples formatos
 * (PDF, DOCX, TXT, etc.), permitiendo así trabajar con documentos de entrada variados en pipelines de IA.
 *
 * Este servicio representa el primer paso en un pipeline RAG (Retrieval-Augmented Generation): transformar
 * un documento binario en texto plano que pueda ser procesado, chunked y embebido.
 */
@Service
@RequiredArgsConstructor
public class ExtractionServiceImpl implements ExtractionService {

    /**
     * Dependencia de Apache Tika, inyectada por Spring. Centraliza la lógica de extracción de texto.
     */
    private final Tika tika;

    /**
     * Extrae el texto plano de un archivo subido, limpiando espacios y saltos innecesarios.
     *
     * @param file Documento subido (PDF, DOCX, etc.) en formato MultipartFile.
     * @return     Texto extraído y normalizado.
     *
     * @throws RuntimeException Si hay un error durante la extracción (por formato o archivo corrupto).
     */
    @Override
    public String extractText(MultipartFile file) {
        try {
            // Extrae el texto con Tika y limpia espacios, tabs y saltos para homogeneizar el resultado
            return tika.parseToString(file.getInputStream())
                    .replaceAll("\\s+", " ") // Sustituye todos los espacios y saltos múltiples por un solo espacio
                    .trim();
        } catch (TikaException | IOException e) {
            // Si ocurre cualquier error, encapsula la excepción en una RuntimeException personalizada
            throw new RuntimeException("Error extrayendo texto del documento: " + file.getOriginalFilename(), e);
        }
    }
}