package com.anto.gpstrack.service;

import com.anto.gpstrack.dto.DeletePhoneDto;
import com.anto.gpstrack.dto.RegisterPhoneDto;
import com.anto.gpstrack.entities.Phone;
import com.anto.gpstrack.repositories.PhoneRepository;
import com.anto.gpstrack.repositories.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhoneService {
    PhoneRepository phoneRepository;
    PositionRepository positionRepository;

    /**
     * Registers phone, generates new uuid.
     *
     * @return UUID of registered phone
     */
    public UUID register(RegisterPhoneDto phone) {
        Phone dbPhone = phoneRepository.findByImei(phone.getImei());
        if (dbPhone == null) {
            dbPhone = Phone.builder()
                    .imei(phone.getImei())
                    .uuid(UUID.randomUUID())
                    .build();
        }
        return dbPhone.getUuid();
    }

    public void clearData(DeletePhoneDto phone) throws Exception {
        Phone dbPhone = phoneRepository.findByImeiAndUuid(phone.getImei(), phone.getUuid());
        if (dbPhone == null) {
            throw new Exception("Data mismatch or phone unregistered");
        }

        positionRepository.deleteByPhone(dbPhone);
        phoneRepository.delete(dbPhone);
    }
}
