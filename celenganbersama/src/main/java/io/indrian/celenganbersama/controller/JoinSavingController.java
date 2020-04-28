package io.indrian.celenganbersama.controller;

import io.indrian.celenganbersama.assembler.JoinSavingModelAssembler;
import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.JoinSaving;
import io.indrian.celenganbersama.repositories.JoinSavingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/join")
public class JoinSavingController {

    @Autowired
    private JoinSavingRepository joinSavingRepository;

    @Autowired
    private JoinSavingModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<JoinSaving>> all() {

        List<EntityModel<JoinSaving>> joinSavings = joinSavingRepository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(
                joinSavings,
                linkTo(methodOn(this.getClass()).all()).withSelfRel()
        );
    }

    @GetMapping("/{id}")
    public EntityModel<JoinSaving> one(@PathVariable("id") Long id) {

        return joinSavingRepository.findById(id)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFound(id));
    }
}
