package com.anto.gpstrack.api;

import com.anto.gpstrack.dto.*;
import com.anto.gpstrack.service.PhoneService;
import com.anto.gpstrack.service.PositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/phone")
public class PhoneController {
    PhoneService phoneService;
    PositionService positionService;

    @PostMapping("/")
    public ResponseEntity<RegisterPhoneResponseDto> register(@RequestBody RegisterPhoneDto phone) {
        String imei = phone.getImei();

        if (imei == null || imei.isEmpty() || !isImeiValid(imei)) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(RegisterPhoneResponseDto.builder()
                .uuid(phoneService.register(phone))
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteData(@RequestBody DeletePhoneDto phone) {
        try {
            phoneService.clearData(phone);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    private boolean isImeiValid(String imei) {
        if (imei.length() != 15)
            return false;

        int last = imei.charAt(14) - 48;

        int curr;
        int sum = 0;
        for (int i = 0; i < 14; i++) {
            curr = imei.charAt(i) - 48;
            if (i % 2 != 0) {
                curr = 2 * curr;
                if (curr > 9) {
                    curr = (curr / 10) + (curr - 10);
                }
            }
            sum += curr;
        }
        int round = sum % 10 == 0 ? sum : ((sum / 10 + 1) * 10);

        return (round - sum == last);
    }
}
