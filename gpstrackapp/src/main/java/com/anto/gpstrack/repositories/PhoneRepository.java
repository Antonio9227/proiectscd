package com.anto.gpstrack.repositories;

import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.entities.Position;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface PhoneRepository extends Repository<Phone,Long> {
    Phone findByUniqueId(String uniqueId);
    Phone findByUuid(UUID uuid);

    Phone findByUniqueIdAndUuid(String uniqueId, UUID uuid);

    void delete(Phone dbPhone);

    void save(Phone dbPhone);

    List<Phone> findAll();
}
