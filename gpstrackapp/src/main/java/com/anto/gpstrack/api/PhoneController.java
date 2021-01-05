package com.anto.gpstrack.api;

import com.anto.gpstrack.dto.*;
import com.anto.gpstrack.entities.AccessToken;
import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.service.AccessTokenService;
import com.anto.gpstrack.service.PhoneService;
import com.anto.gpstrack.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phone")
public class PhoneController {
    @Autowired
    PhoneService phoneService;

    @Autowired
    PositionService positionService;

    @Autowired
    AccessTokenService tokenService;

    private static final int MAGIC_NUMBER = 63;

    @PostMapping("/token")
    public ResponseEntity<RequestAccessTokenResponseDto> acquireToken(@RequestBody RequestAccessTokenDto request) {
        Phone p = phoneService.getPhone(request.getImei(), request.getUuid());
        if (p == null) {
            return ResponseEntity.badRequest().build();
        }
        AccessToken token = tokenService.generateToken(request.getUuid());
        return new ResponseEntity<>(
                RequestAccessTokenResponseDto.builder()
                        .token(token.getValue())
                        .build(),HttpStatus.OK);
    }

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

    // no longer imei
    // 4c251a9328fe44aex48 b7e97903-0a74-4559-b76b-3c8c35388ed8 should be valid, test me!
    private boolean isImeiValid(String deviceId) {
        try {
            String[] strings = deviceId.split("x");
            byte[] bytes = strings[0].getBytes();

            int cks = 0;
            for (int i = 0; i < strings[0].length(); i++)
                cks += bytes[i];

            String checkId = strings[0] + "x" + cks % MAGIC_NUMBER;

            return deviceId.equals(checkId);
        } catch (Exception e) {
            return false;
        }
    }
}
