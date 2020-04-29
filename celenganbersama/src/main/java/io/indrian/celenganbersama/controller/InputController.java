package io.indrian.celenganbersama.controller;

import io.indrian.celenganbersama.assembler.InputModelAssembler;
import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.Input;
import io.indrian.celenganbersama.repositories.InputRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/inputs")
public class InputController {

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private InputModelAssembler inputModelAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Input>> all() {

        List<EntityModel<Input>> inputs = inputRepository.findAll()
                .stream()
                .map(inputModelAssembler::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(
                inputs,
                linkTo(methodOn(this.getClass()).all()).withSelfRel()
        );
    }

    @GetMapping("/{inputId}")
    public EntityModel<Input> one(@PathVariable("inputId") Long inputId) {

        return inputRepository.findById(inputId)
                .map(input -> inputModelAssembler.toModel(input))
                .orElseThrow(() -> new ResourceNotFound(inputId));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Input>> saveMoney(@RequestBody Input input) {

        Input newInput = inputRepository.save(input);
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).one(newInput.getId())).toUri())
                .body(inputModelAssembler.toModel(newInput));
    }
}
