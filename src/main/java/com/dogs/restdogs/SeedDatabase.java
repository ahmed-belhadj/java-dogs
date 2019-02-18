package com.dogs.restdogs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SeedDatabase
{
    @Bean
    public CommandLineRunner initializeDB(DogRepository dogRepository)
    {
        return args ->
        {
            log.info("Seeding " + dogRepository.save(new Dog("Springer", 50, false)));
            log.info("Seeding " + dogRepository.save(new Dog("Bulldog", 50, true)));
            log.info("Seeding " + dogRepository.save(new Dog("Collie", 50, false)));
            log.info("Seeding " + dogRepository.save(new Dog("Boston Terrier", 35, true)));
            log.info("Seeding " + dogRepository.save(new Dog("Corgi", 35, true)));
        };
    }
}
