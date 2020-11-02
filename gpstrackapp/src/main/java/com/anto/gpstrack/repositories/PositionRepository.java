package com.anto.gpstrack.repositories;

import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.entities.Position;
import org.springframework.data.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Repository
public interface PositionRepository extends Repository<Position,Long> {

    void save(Position dbPosition);

    void deleteByPhone(Phone phone);
}
