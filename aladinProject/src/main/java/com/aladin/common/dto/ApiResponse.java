package com.aladin.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse {
    private int status;
    private String message;
    
    public ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
