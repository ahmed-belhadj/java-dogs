package com.dogs.restdogs;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Dog
{
    private @Id
    @GeneratedValue
    Long id;
    private String breedName;
    private int averageWeight;
    private boolean apartmentAppropriate;

    public Dog()
    {
    }

    public Dog(String breedName, int averageWeight, boolean apartmentAppropriate)
    {
        this.breedName = breedName;
        this.averageWeight = averageWeight;
        this.apartmentAppropriate = apartmentAppropriate;
    }
}
