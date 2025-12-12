package com.example.task_management.module.user.controller;

import com.example.task_management.common.dto.ApiResponse;
import com.example.task_management.module.user.dto.request.LoginRequest;
import com.example.task_management.module.user.dto.response.AuthResponse;
import com.example.task_management.module.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse httpServletresponse,
            HttpServletRequest httpServletRequest) {
        AuthResponse auth = authService.login(request, httpServletresponse ,httpServletRequest);

        ApiResponse response = ApiResponse.success(
                HttpStatus.OK,
                "Login Susccecfully",
                auth.getAccessToken() != null ? auth : null,
                httpServletRequest.getRequestURI()
                );

        return ResponseEntity.ok(response);
    }

}
