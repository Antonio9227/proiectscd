package com.anto.gpstrack.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
// AccessToken will be used to auth users on web portal
public class AccessToken {
    @Id
    @GeneratedValue
    private int id;

    boolean isAdminToken = false;

    @GeneratedValue
    private UUID value;

    private UUID resourceId;

    private long expiresOn;
}
