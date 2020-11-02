package com.anto.gpstrack.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
public class Phone {
    @Id
    @GeneratedValue
    private UUID uuid;

    private String imei;
}
