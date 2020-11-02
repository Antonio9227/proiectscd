package com.anto.gpstrack.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;


@Entity
@Builder
@AllArgsConstructor
public class Position {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "phone_uuid", referencedColumnName = "uuid")
    private Phone phoneId;

    private double latitude;
    private double longitude;

    private long creationTimestamp;
}
