package com.dhh.ragBasic.service.impl;

import com.dhh.ragBasic.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChatModel chatModel;

    public String call(String question, String context) {
        String promptText = """
                Responde a la siguiente pregunta SOLO usando la información del contexto. Si la respuesta no está en el contexto, responde 'No lo sé'.
                
                Contexto:
                %s
                
                Pregunta: %s
                Respuesta:
                """.formatted(context, question);

        try {
            log.info("Llamando a Ollama para: {}", question);

            Prompt prompt = new Prompt(promptText,
                    OllamaOptions.builder()
                            .model("mistral")
                            .temperature(0.1)
                            .build());

            String response = chatModel.call(prompt).getResult().getOutput().getText();
            log.info("Respuesta de Ollama recibida");
            return response;

        } catch (Exception e) {
            log.error("Error llamando a Ollama: {}", e.getMessage(), e);
            return "Lo siento, no pude procesar tu pregunta en este momento.";
        }
    }
}


