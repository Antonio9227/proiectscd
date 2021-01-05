package com.anto.gpstrack.repositories;

import com.anto.gpstrack.entities.AccessToken;
import com.anto.gpstrack.entities.Phone;
import org.springframework.data.repository.Repository;

import java.util.UUID;

@org.springframework.stereotype.Repository
public interface AccessTokenRepository extends Repository<AccessToken, Long> {
    AccessToken findByResourceId(UUID id);
    AccessToken findByResourceIdAndValue(UUID id, UUID value);
    void save(AccessToken token);

    AccessToken findByValue(UUID value);}
