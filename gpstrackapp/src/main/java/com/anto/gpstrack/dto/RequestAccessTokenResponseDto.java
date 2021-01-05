package com.anto.gpstrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class RequestAccessTokenResponseDto {
    private UUID token;
    private boolean isAdmin = false;
}