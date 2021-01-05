package com.anto.gpstrack.repositories;

import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.entities.Position;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface PositionRepository extends Repository<Position,Long> {

    void save(Position dbPosition);

    void deleteByPhoneId(UUID phoneId);

    List<Position> findByPhoneIdAndCreationTimestampAfterAndCreationTimestampBefore(Phone phoneId, long start, long end);
}
