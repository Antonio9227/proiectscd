package com.anto.track.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class PositionDto {
    private double latitude;
    private double longitude;

    private UUID phoneId;
}
