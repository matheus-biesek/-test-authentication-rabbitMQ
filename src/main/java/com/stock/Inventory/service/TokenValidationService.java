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

@Service
@RequiredArgsConstructor
public class TokenValidationService {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidationService.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public boolean validateToken(String token, UserRole role) {
        try {
            String jsonRequest = createTokenValidationRequest(token, role);
            String response = sendValidationRequest(jsonRequest);
            return "VALID".equals(response);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao validar o token:\n", e);
        }
    }

    private String createTokenValidationRequest(String token, UserRole role) throws JsonProcessingException {
        TokenValidationRequest request = new TokenValidationRequest();
        request.setToken(token);
        request.setRole(role);
        return objectMapper.writeValueAsString(request);
    }

    private String sendValidationRequest(String jsonRequest) {
        return (String) rabbitTemplate.convertSendAndReceive("token.validation", jsonRequest);
    }

}
