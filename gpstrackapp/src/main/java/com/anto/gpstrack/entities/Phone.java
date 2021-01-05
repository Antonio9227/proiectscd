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
public class Phone {
    @Id
    @GeneratedValue
    private UUID uuid;

    // should have been imei but android doesn't really like giving that away anymore
    private String uniqueId;
}
