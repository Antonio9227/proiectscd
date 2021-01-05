package com.anto.gpstrack.service;

import com.anto.gpstrack.dto.PositionDto;
import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.entities.Position;
import com.anto.gpstrack.repositories.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.anto.gpstrack.repositories.PositionRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PositionService {
    @Autowired
    private PositionRepository repository;

    @Autowired
    private PhoneRepository phoneRepository;

    public void registerPosition(PositionDto position) throws Exception {

        Phone dbPhone = phoneRepository.findByUuid(position.getPhoneId());

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

    public List<Position> getWithinInterval(UUID resourceId, long start, long end){
        return repository.findByPhoneIdAndCreationTimestampAfterAndCreationTimestampBefore(
                phoneRepository.findByUuid(resourceId),start,end);
    }

}
