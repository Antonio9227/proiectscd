package com.anto.gpstrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RegisterPhoneDto {
    private String imei;
}
