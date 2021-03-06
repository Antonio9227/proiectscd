package com.anto.track.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class RegisterPhoneResponseDto {
    private UUID uuid;
}
