package com.anto.gpstrack.api;

import com.anto.gpstrack.dto.PositionDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.anto.gpstrack.service.PositionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/position")
public class PositionController {

    private PositionService positionService;

    @PostMapping("/")
    public ResponseEntity<?> submitPosition(@RequestBody PositionDto position) {
        try {
            positionService.registerPosition(position);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
