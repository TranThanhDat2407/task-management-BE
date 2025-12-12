package com.example.task_management.module.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
