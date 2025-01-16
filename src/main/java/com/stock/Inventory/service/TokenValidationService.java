package com.stock.Inventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.Inventory.dto.request.TokenValidationRequest;
import com.stock.Inventory.enumm.UserRole;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TokenValidationService {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidationService.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public CompletableFuture<Boolean> validateToken(String token, UserRole role) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        try {
            String jsonRequest = createTokenValidationRequest(token, role);
            Object response = rabbitTemplate.convertSendAndReceive("token.validation", jsonRequest);

            if (response instanceof String) {
                handleResponse((String) response, future);
            } else {
                future.completeExceptionally(new RuntimeException("Resposta inesperada do RabbitMQ"));
            }

        } catch (Exception e) {
            future.completeExceptionally(new RuntimeException("Erro ao validar o token", e));
        }
        return future;
    }

    private String createTokenValidationRequest(String token, UserRole role) throws JsonProcessingException {
        TokenValidationRequest request = new TokenValidationRequest();
        request.setToken(token);
        request.setRole(role);
        return objectMapper.writeValueAsString(request);
    }

    private void handleResponse(String response, CompletableFuture<Boolean> future) {
        try {
            if ("VALID".equals(response)) {
                logger.info("Token válido recebido.");
                future.complete(true);
            } else {
                logger.warn("Token inválido.");
                future.complete(false);
            }
        } catch (Exception e) {
            future.completeExceptionally(new RuntimeException("Erro ao processar a resposta", e));
        }
    }
}
