package com.anto.gpstrack.service;

import com.anto.gpstrack.entities.AccessToken;
import com.anto.gpstrack.repositories.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessTokenService {
    @Autowired
    private AccessTokenRepository repository;

    private static final long EXPIRATION_TIMEOUT = 60 * 60; // 1h

    public AccessToken generateToken(UUID resourceId) {
        AccessToken token = AccessToken.builder().resourceId(resourceId).isAdminToken(resourceId == null).value(UUID.randomUUID())
                .expiresOn(Instant.now().getEpochSecond() + EXPIRATION_TIMEOUT).build();
        repository.save(token);
        return token;
    }

    public boolean isValid(UUID value, UUID resourceId) {
        AccessToken token;
        if(resourceId == null){
            token = repository.findByValue(value);
        }
        else{
            token = repository.findByResourceIdAndValue(resourceId, value);
        }
        return token != null && token.getExpiresOn() > Instant.now().getEpochSecond();
    }

    public AccessToken getByValue(UUID value){
        return repository.findByValue(value);
    }
}
