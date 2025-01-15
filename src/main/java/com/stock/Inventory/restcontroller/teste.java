package com.stock.Inventory.restcontroller;

import com.stock.Inventory.dto.request.TokenValidationRequest;
import com.stock.Inventory.service.TokenValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/master")
@RequiredArgsConstructor
public class teste {

    private final TokenValidationService tokenValidationService;

    @PostMapping("/valid_token")
    public boolean deleteUser( @RequestBody TokenValidationRequest body, BindingResult result) {
        return this.tokenValidationService.validateToken(body.getToken(), body.getRole());
    }
}
