package com.anto.track.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DeletePhoneDto {
    private String imei;
    private UUID uuid;
}
