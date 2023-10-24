package org.accommodationcal.models;

import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Builder
@Data
public class Person {

    private String name;
    private double rentMultiplier;
    private List<LocalDate> datesOfAccommodation;
}
