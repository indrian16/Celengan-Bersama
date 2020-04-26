package io.indrian.celenganbersama.controller;

import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.Saving;
import io.indrian.celenganbersama.model.User;
import io.indrian.celenganbersama.repositories.SavingRepository;
import io.indrian.celenganbersama.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/savings")
public class SavingController {

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private SavingRepository savingRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Saving> all() {

        return savingRepository.findAll();
    }

    @PostMapping
    public Saving createNewSaving(@Valid @RequestBody Saving saving) {

        User userOwner = userRepository.findById(saving.getOwnerId())
                .orElseThrow(() -> new ResourceNotFound(saving.getOwnerId()));

        userOwner.setSavings(Set.of(saving));
        saving.setUsers(Set.of(userOwner));

        return savingRepository.save(saving);
    }
}
