package com.stock.Inventory.dto.request;

import com.stock.Inventory.enumm.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class TokenValidationRequest  implements Serializable {
    @Getter
    private String token;
    private UserRole role;

}
