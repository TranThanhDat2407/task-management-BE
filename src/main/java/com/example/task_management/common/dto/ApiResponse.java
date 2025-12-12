package com.example.task_management.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final Instant timestamp = Instant.now();
    private int statusCode;
    private String status;
    private String message;
    private T data; // data (Generic Type)
    private String path; // api path

    public static <T> ApiResponse<T> success(HttpStatus httpStatus, String message, T data, String path) {
        return ApiResponse.<T>builder()
                .statusCode(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .data(data)
                .path(path)
                .build();
    }

}