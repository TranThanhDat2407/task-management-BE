package com.example.task_management.module.user.service.implement;

import com.example.task_management.common.util.CookieUtils;
import com.example.task_management.module.user.domain.User;
import com.example.task_management.module.user.dto.request.LoginRequest;
import com.example.task_management.module.user.dto.request.RegisterRequest;
import com.example.task_management.module.user.dto.response.AuthResponse;
import com.example.task_management.module.user.enums.AuthProvider;
import com.example.task_management.module.user.repository.UserRepository;
import com.example.task_management.module.user.service.AuthService;
import com.example.task_management.sercurity.jwt.JwtService;
import com.example.task_management.sercurity.user.CustomUserDetails;
import com.example.task_management.sercurity.user.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final CookieUtils cookieUtils;

    private static final String CLIENT_TYPE_HEADER = "X-Client-Type";

    @Override
    public AuthResponse login(LoginRequest request,
                              HttpServletResponse response,
                              HttpServletRequest httpRequest
    ) {

        String clientType = httpRequest.getHeader(CLIENT_TYPE_HEADER);

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User Not Found");
        }

        User existingUser = userOptional.get();

        if (existingUser.getProvider() == AuthProvider.GOOGLE) {
            throw new BadCredentialsException("This account was registered via Google. " +
                    "Use Google Login instead.");
        }

        if (!passwordEncoder.matches(request.getPassword(), existingUser.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        if (!existingUser.getIsActive()) {
            throw new BadCredentialsException("User is locked");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(existingUser.getEmail());

        //create ACCESS TOKEN and REFRESH TOKEN
        String accessToken = jwtService.generateAccessToken((CustomUserDetails) userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        //CHECK LOGIN TYPE
        boolean isMobile = clientType != null && clientType.equalsIgnoreCase("MOBILE");

        if(!isMobile){
            cookieUtils.addHttpOnlyCookie(
                    response,
                    "refresh_token",
                    refreshToken,
                    jwtService.getRemainingSeconds(refreshToken)
            );

            cookieUtils.addHttpOnlyCookie(
                    response,
                    "access_token",
                    refreshToken,
                    jwtService.getRemainingSeconds(refreshToken)
            );

            return AuthResponse.builder().build();
        }

        AuthResponse authResponse = AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return authResponse;

    }

    @Override
    public User register(RegisterRequest request) {
        return null;
    }

    @Override
    public void logout(String accessToken, String refreshToken, HttpServletResponse response) {

    }



}
