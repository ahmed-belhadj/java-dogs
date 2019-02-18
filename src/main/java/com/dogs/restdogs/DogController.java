package com.dogs.restdogs;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class DogController
{
    private final DogRepository dogRepository;
    private final DogResourceAssembler assembler;

    public DogController(DogRepository dogRepository, DogResourceAssembler assembler)
    {
        this.dogRepository = dogRepository;
        this.assembler = assembler;
    }

    @GetMapping("/dogs")
    public Resources<Resource<Dog>> all()
    {
        List<Resource<Dog>> dogs = dogRepository.findAll()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/{id}")
    public Resource<Dog> findOne(@PathVariable Long id)
    {
        Dog foundDog = dogRepository.findById(id).orElseThrow(() -> new DogNotFoundException(id));

        return assembler.toResource(foundDog);
    }

    @GetMapping("/dogs/breeds")
    public Resources<Resource<Dog>> getDogsByBreed()
    {
        List<Resource<Dog>> dogs = dogRepository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        dogs.sort((d1, d2) -> d1.getContent().getBreedName().compareToIgnoreCase(d2.getContent().getBreedName()));

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/breeds/{breed}")
    public Resources<Resource<Dog>> getBreed(@PathVariable String breed)
    {
        List<Resource<Dog>> dogs = dogRepository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        dogs.removeIf(d -> d.getContent().getBreedName().compareToIgnoreCase(breed) != 0);

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/weight")
    public Resources<Resource<Dog>> getDogsByWeight()
    {
        List<Resource<Dog>> dogs = dogRepository.findAll()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        dogs.sort((d1, d2) -> d1.getContent().getAverageWeight() - d2.getContent().getAverageWeight());

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @GetMapping("/dogs/apartment")
    public Resources<Resource<Dog>> getApartmentDogs()
    {
        List<Resource<Dog>> dogs = dogRepository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        dogs.removeIf(d -> !d.getContent().isApartmentAppropriate());

        return new Resources<>(dogs, linkTo(methodOn(DogController.class).all()).withSelfRel());
    }

    @PutMapping("/dogs/{id}")
    public ResponseEntity<?> putById(@RequestBody Dog newDog, @PathVariable Long id)
            throws URISyntaxException
    {
        Dog updatedDog = dogRepository.findById(id)
                .map(dog ->
                {
                    dog.setBreedName(newDog.getBreedName());
                    dog.setAverageWeight(newDog.getAverageWeight());
                    dog.setApartmentAppropriate(newDog.isApartmentAppropriate());
                    return dogRepository.save(dog);
                })
                .orElseGet(() ->
                {
                    newDog.setId(id);
                    return dogRepository.save(newDog);
                });

        Resource<Dog> resource = assembler.toResource(updatedDog);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PostMapping("/dogs")
    public ResponseEntity<?> putById(@RequestBody Dog newDog)
            throws URISyntaxException
    {
        dogRepository.save(newDog);
        Resource<Dog> resource = assembler.toResource(newDog);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }


    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id)
    {
        dogRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
