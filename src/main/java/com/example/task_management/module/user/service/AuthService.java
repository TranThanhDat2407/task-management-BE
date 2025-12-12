package com.example.task_management.module.user.service;


import com.example.task_management.module.user.domain.User;
import com.example.task_management.module.user.dto.request.LoginRequest;
import com.example.task_management.module.user.dto.request.RegisterRequest;
import com.example.task_management.module.user.dto.response.AuthResponse;
import com.example.task_management.module.user.service.implement.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestHeader;

public interface AuthService {
    AuthResponse login(LoginRequest request,
                       HttpServletResponse response,
                       HttpServletRequest httpRequest
    );

    User register(RegisterRequest request);

    void logout(String accessToken, String refreshToken, HttpServletResponse response);

}
