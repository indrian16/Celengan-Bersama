package io.indrian.celenganbersama.controller;

import io.indrian.celenganbersama.assembler.JoinSavingModelAssembler;
import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.JoinSaving;
import io.indrian.celenganbersama.model.Saving;
import io.indrian.celenganbersama.repositories.JoinSavingRepository;
import io.indrian.celenganbersama.repositories.SavingRepository;
import io.indrian.celenganbersama.utils.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/joins")
public class JoinSavingController {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

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

    @PostMapping
    public ResponseEntity<EntityModel<JoinSaving>> generateJoin(
            @RequestBody JoinSaving joinSaving
    ) {

        // Create
        String generatedString = RandomStringUtils.randomAlphanumeric(16);
        Date limitDate = DateUtils.getFifteenDay();
        joinSaving.setCode(generatedString);
        joinSaving.setLimitDate(limitDate);

        // Joining
        Saving saving = savingRepository.findById(joinSaving.getSaving().getId())
                .orElseThrow(() -> new ResourceNotFound(joinSaving.getSaving().getId()));
        saving.setJoinSaving(joinSaving);
        joinSaving.setSaving(saving);

        // Save
        JoinSaving newJoin = joinSavingRepository.save(joinSaving);
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).one(newJoin.getId())).toUri())
                .body(assembler.toModel(newJoin));
    }
}
