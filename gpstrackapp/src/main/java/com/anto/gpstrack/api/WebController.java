package com.anto.gpstrack.api;

import com.anto.gpstrack.dto.*;
import com.anto.gpstrack.entities.AccessToken;
import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.entities.Position;
import com.anto.gpstrack.service.AccessTokenService;
import com.anto.gpstrack.service.PhoneService;
import com.anto.gpstrack.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/web")
public class WebController {
    @Autowired
    AccessTokenService tokenService;

    @Autowired
    PositionService positionService;

    @Autowired
    PhoneService phoneService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        // for now. not enough time, I gotta finish this in a couple of hours
        if(loginDto.getUsername().equals("admin") && loginDto.getPassword().equals("admin")){
            AccessToken token = tokenService.generateToken(null);
            return new ResponseEntity<>(
                    RequestAccessTokenResponseDto.builder()
                            .token(token.getValue())
                            .isAdmin(true)
                            .build(), HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/validateToken")
    public  ResponseEntity<?> validate(@RequestBody ValidateTokenDto dto){
        if(tokenService.isValid(dto.getToken(),dto.getResourceId()))
            return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/getPositions")
    public ResponseEntity<?> getPositions(@RequestBody GetPositionsDto dto){
        AccessToken token = tokenService.getByValue(dto.getToken());

        if(token==null || ! tokenService.isValid(token.getValue(), token.getResourceId()))
            return ResponseEntity.badRequest().build();

        if(dto.getResourceID()==null ^ !token.isAdminToken())
            return ResponseEntity.badRequest().build();

        ArrayList<PositionDto> positions=new ArrayList<>();
        for (Position p: positionService.getWithinInterval(token.isAdminToken()?dto.getResourceID():token.getResourceId(), dto.getTimestampStart(), dto.getTimestampEnd())) {
            positions.add(PositionDto.builder().latitude(p.getLatitude())
            .longitude(p.getLongitude()).build());
        }
        return ResponseEntity.ok(positions);
    }

    @PostMapping("/getDevices")
    public ResponseEntity<?> getDevices(@RequestBody ValidateTokenDto dto){
        AccessToken token = tokenService.getByValue(dto.getToken());
        if(token==null || !token.isAdminToken())
            return ResponseEntity.badRequest().build();

        List<String> ids= new ArrayList<>();
        for(Phone p:phoneService.getAll()){
            ids.add(p.getUuid().toString());
        }

        return ResponseEntity.ok(ids);
    }
}
