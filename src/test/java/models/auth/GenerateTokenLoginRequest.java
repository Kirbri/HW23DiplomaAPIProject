package models.auth;

import lombok.Data;

@Data
public class GenerateTokenLoginRequest {
    String username,
            password;
}