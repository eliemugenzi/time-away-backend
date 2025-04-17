package dev.elieweb.timeaway.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private int status;
    private Object data;

    public static ApiResponse success(Object data) {
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(data)
                .message("Success")
                .build();
    }

    public static ApiResponse success(String message, Object data) {
        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .data(data)
                .message(message)
                .build();
    }

    public static ApiResponse error(String message, int status) {
        return ApiResponse.builder()
                .status(status)
                .message(message)
                .build();
    }

    public static ApiResponse error(String message) {
        return error(message, HttpStatus.BAD_REQUEST.value());
    }
} 