package io.indrian.celenganbersama.controller;

import io.indrian.celenganbersama.assembler.JoinSavingModelAssembler;
import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.JoinSaving;
import io.indrian.celenganbersama.model.Saving;
import io.indrian.celenganbersama.repositories.JoinSavingRepository;
import io.indrian.celenganbersama.repositories.SavingRepository;
import io.indrian.celenganbersama.utils.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
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
    private SavingRepository savingRepository;

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

    @PostMapping("/{savingId}")
    public ResponseEntity<EntityModel<JoinSaving>> create(
            @PathVariable("savingId") Long id
    ) {

        // Create Join
        JoinSaving joinSaving = new JoinSaving();
        String generatedString = RandomStringUtils.randomAlphanumeric(16);
        Date limitDAte = DateUtils.getFifteenDay();
        joinSaving.setCode(generatedString);
        joinSaving.setLimitDate(limitDAte);

        // Find Saving
        Saving saving = savingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(id));

        // Join to Saving
        saving.setJoinSaving(joinSaving);
        joinSaving.setSaving(saving);

        // Save
        JoinSaving newJoinSaving = joinSavingRepository.save(joinSaving);
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).one(newJoinSaving.getId())).toUri())
                .body(assembler.toModel(newJoinSaving));
    }
}
