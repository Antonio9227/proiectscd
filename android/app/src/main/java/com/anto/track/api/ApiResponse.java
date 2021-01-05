package com.anto.track.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {
    private int statusCode;
    private String body;

    public boolean isSuccessful() {
        return statusCode == 200;
    }
}
