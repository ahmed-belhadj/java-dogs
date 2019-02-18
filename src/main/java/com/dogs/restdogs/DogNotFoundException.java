package com.dogs.restdogs;

public class DogNotFoundException extends RuntimeException
{
    public DogNotFoundException(Long id)
    {
        super("Could not find dog " + id);
    }
}
