package com.anto.gpstrack.service;

import com.anto.gpstrack.dto.DeletePhoneDto;
import com.anto.gpstrack.dto.RegisterPhoneDto;
import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.repositories.PhoneRepository;
import com.anto.gpstrack.repositories.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneService {
    @Autowired
    PhoneRepository phoneRepository;

    @Autowired
    PositionRepository positionRepository;

    /**
     * Registers phone, generates new uuid.
     *
     * @return UUID of registered phone
     */
    public UUID register(RegisterPhoneDto phone) {
        Phone dbPhone = phoneRepository.findByUniqueId(phone.getImei());
        if (dbPhone == null) {
            dbPhone = Phone.builder()
                    .uniqueId(phone.getImei())
                    .uuid(UUID.randomUUID())
                    .build();
        }
        phoneRepository.save(dbPhone);
        return dbPhone.getUuid();
    }

    public void clearData(DeletePhoneDto phone) throws Exception {
        Phone dbPhone = phoneRepository.findByUniqueIdAndUuid(phone.getImei(), phone.getUuid());
        if (dbPhone == null) {
            throw new Exception("Data mismatch or phone unregistered");
        }

        positionRepository.deleteByPhoneId(dbPhone.getUuid());
        phoneRepository.delete(dbPhone);
    }

    public Phone getPhone(String uniqueId, UUID uuid){
        return phoneRepository.findByUniqueIdAndUuid(uniqueId, uuid);
    }

    public List<Phone> getAll() {
        return phoneRepository.findAll();
    }
}
