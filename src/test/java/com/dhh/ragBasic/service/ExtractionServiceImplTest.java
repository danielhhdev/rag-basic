package com.dhh.ragBasic.service;

import com.dhh.ragBasic.service.impl.ExtractionServiceImpl;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExtractionServiceImplTest {

    @InjectMocks
    private ExtractionServiceImpl extractionService;

    @Mock
    private Tika tika;

    @Test
    @DisplayName("Extrae texto correctamente de un archivo de texto plano")
    void testExtractTextFromTxtFile() throws TikaException, IOException {
        String contenido = "Esto es un ejemplo de texto para probar la extracción.";
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "ejemplo.txt",
                "text/plain",
                contenido.getBytes()
        );

        when(tika.parseToString(any(InputStream.class))).thenReturn(contenido);

        String result = extractionService.extractText(mockFile);
        assertNotNull(result);
        assertTrue(result.contains("ejemplo de texto"));
        assertFalse(result.isBlank());
    }

    @Test
    @DisplayName("Extrae texto correctamente de un archivo PDF simple")
    void testExtractTextFromPdfFile() throws TikaException, IOException {
        // Puedes generar un PDF de prueba básico o usar un recurso de test
        // Aquí te muestro cómo hacerlo en memoria con texto
        // (Tika puede extraer texto simple de PDFs sencillos generados así)
        String texto = "Este es un PDF de prueba.";
        MockMultipartFile pdfFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                texto.getBytes()
        );

        when(tika.parseToString(any(InputStream.class))).thenReturn(texto);

        String result = extractionService.extractText(pdfFile);
        assertNotNull(result);
        assertFalse(result.isBlank());
    }

}
