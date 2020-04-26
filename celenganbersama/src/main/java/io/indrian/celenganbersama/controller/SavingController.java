package io.indrian.celenganbersama.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.indrian.celenganbersama.assembler.SavingModelAssembler;
import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.Saving;
import io.indrian.celenganbersama.model.User;
import io.indrian.celenganbersama.repositories.SavingRepository;
import io.indrian.celenganbersama.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/savings")
public class SavingController {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private SavingRepository savingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SavingModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Saving>> all() {

        List<EntityModel<Saving>> savings = savingRepository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(
                savings,
                linkTo(methodOn(this.getClass()).all()).withSelfRel()
        );
    }

    @GetMapping("/{savingId}")
    public EntityModel<Saving> one(@PathVariable("savingId") Long savingId) {

        return savingRepository.findById(savingId)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFound(savingId));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Saving>> createNewSaving(@Valid @RequestBody Saving saving) {

        User userOwner = userRepository.findById(saving.getOwnerId())
                .orElseThrow(() -> new ResourceNotFound(saving.getOwnerId()));

        userOwner.setSavings(Set.of(saving));
        saving.setUsers(Set.of(userOwner));

        Saving newSaving = savingRepository.save(saving);
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).one(newSaving.getId())).toUri())
                .body(assembler.toModel(newSaving));
    }
}
