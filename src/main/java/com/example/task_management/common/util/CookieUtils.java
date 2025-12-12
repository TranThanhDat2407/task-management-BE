package com.example.task_management.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {
    @Value("${server.ssl.enabled:false}")
    private boolean isSecure;

    public void addHttpOnlyCookie(HttpServletResponse response, String name, String value, long maxAgeSeconds) {
        Cookie cookie = new Cookie(name, value);

        cookie.setHttpOnly(true); //  XSS
        cookie.setSecure(isSecure);   // only HTTPS ( prod )
        cookie.setPath("/");      // scope ( all system )
        cookie.setMaxAge((int) maxAgeSeconds); // time live

        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        addHttpOnlyCookie(response, name, null, 0); // maxAge = 0 delete Cookie
    }
}
