package com.anto.gpstrack.repositories;

import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.entities.Position;
import org.springframework.data.repository.Repository;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface PhoneRepository extends Repository<Phone,Long> {
    void save(Position dbPosition);
    Phone findByImei(String imei);
    Phone findUuid(UUID uuid);

    Phone findByImeiAndUuid(String imei, UUID uuid);

    void delete(Phone dbPhone);
}
