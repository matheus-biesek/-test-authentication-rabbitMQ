package com.stock.Inventory.restcontroller;

import com.stock.Inventory.dto.request.TokenValidationRequest;
import com.stock.Inventory.service.TokenValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/teste")
@RequiredArgsConstructor
public class TokenValidationController {

    private final TokenValidationService tokenValidationService;

    @PostMapping("/valid_token")
    public CompletableFuture<ResponseEntity<Boolean>> validateToken(@RequestBody TokenValidationRequest body) {
        return tokenValidationService.validateToken(body.getToken(), body.getRole())
                .thenApply(isValid -> ResponseEntity.ok(isValid))
                .exceptionally(e -> {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
                });
    }
}

