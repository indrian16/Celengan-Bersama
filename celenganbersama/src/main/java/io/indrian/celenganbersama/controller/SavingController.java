package io.indrian.celenganbersama.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.indrian.celenganbersama.assembler.JoinSavingModelAssembler;
import io.indrian.celenganbersama.assembler.SavingModelAssembler;
import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.JoinSaving;
import io.indrian.celenganbersama.model.Saving;
import io.indrian.celenganbersama.model.User;
import io.indrian.celenganbersama.repositories.JoinSavingRepository;
import io.indrian.celenganbersama.repositories.SavingRepository;
import io.indrian.celenganbersama.repositories.UserRepository;
import io.indrian.celenganbersama.utils.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
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
    private JoinSavingRepository joinSavingRepository;

    @Autowired
    private SavingModelAssembler savingModelAssembler;

    @Autowired
    private JoinSavingModelAssembler joinSavingModelAssembler;

    /**
     * Method Saving
     * */

    @GetMapping
    public CollectionModel<EntityModel<Saving>> all() {

        List<EntityModel<Saving>> savings = savingRepository.findAll()
                .stream()
                .map(savingModelAssembler::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(
                savings,
                linkTo(methodOn(this.getClass()).all()).withSelfRel()
        );
    }

    @GetMapping("/{savingId}")
    public EntityModel<Saving> one(@PathVariable("savingId") Long savingId) {

        return savingRepository.findById(savingId)
                .map(savingModelAssembler::toModel)
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
                .body(savingModelAssembler.toModel(newSaving));
    }

    /**
     * Method Join Saving
     * /id/join
     * */

    @GetMapping("/{savingId}/join")
    public EntityModel<JoinSaving> getJoinSaving(
            @PathVariable("savingId") Long savingId
    ) {

        // Find Saving
        Saving saving = findSaving(savingId);
        return joinSavingModelAssembler.toModel(saving.getJoinSaving());
    }

    @PostMapping("/{savingId}/join")
    public ResponseEntity<EntityModel<JoinSaving>> create(
            @PathVariable("savingId") Long savingId
    ) {

        // Create Join
        JoinSaving joinSaving = new JoinSaving();
        String generatedString = RandomStringUtils.randomAlphanumeric(16);
        Date limitDAte = DateUtils.getFifteenDay();
        joinSaving.setCode(generatedString);
        joinSaving.setLimitDate(limitDAte);

        // Find Saving
        Saving saving = findSaving(savingId);

        // Join to Saving
        saving.setJoinSaving(joinSaving);
        joinSaving.setSaving(saving);

        // Save
        JoinSaving newJoinSaving = joinSavingRepository.save(joinSaving);
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).one(newJoinSaving.getId())).toUri())
                .body(joinSavingModelAssembler.toModel(newJoinSaving));
    }

    private Saving findSaving(Long id) {

        return savingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(id));
    }
}
