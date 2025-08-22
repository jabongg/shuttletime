package com.shuttletime.model.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Location {
    String street;
    String district;
    String state;
    String pincode;
}
