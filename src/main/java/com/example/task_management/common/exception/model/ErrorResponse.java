package com.example.task_management.common.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {
    String error;                    // mã lỗi ngắn: invalid_token, not_found, ...
    String message;                  // thông báo cho dev/client
    String path;                     // URL gọi
    int status;                     // 400, 401, 404, 500
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Builder.Default
    private Instant timestamp = Instant.now();
}
