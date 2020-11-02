package com.anto.gpstrack.service;

import com.anto.gpstrack.dto.PositionDto;
import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.entities.Position;
import com.anto.gpstrack.repositories.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.anto.gpstrack.repositories.PositionRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PositionService {
    private PositionRepository repository;
    private PhoneRepository phoneRepository;

    public void registerPosition(PositionDto position) throws Exception {

        Phone dbPhone = phoneRepository.findUuid(position.getPhoneId());

        if(dbPhone == null){
            throw new Exception("Unregistered phone.");
        }

        Position dbPosition = Position.builder()
                .phoneId(dbPhone)
                .latitude(position.getLatitude())
                .longitude(position.getLongitude())
                .creationTimestamp(Instant.now().getEpochSecond())
                .build();

        repository.save(dbPosition);
    }

}
