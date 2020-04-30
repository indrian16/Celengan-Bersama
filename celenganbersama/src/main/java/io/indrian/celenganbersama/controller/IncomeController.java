package io.indrian.celenganbersama.controller;

import io.indrian.celenganbersama.assembler.IncomeModelAssembler;
import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.Income;
import io.indrian.celenganbersama.repositories.IncomeRepository;
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
@RequestMapping("/api/v1/incomes")
public class IncomeController {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private IncomeModelAssembler incomeModelAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Income>> all() {

        List<EntityModel<Income>> inputs = incomeRepository.findAll()
                .stream()
                .map(incomeModelAssembler::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(
                inputs,
                linkTo(methodOn(this.getClass()).all()).withSelfRel()
        );
    }

    @GetMapping("/{inputId}")
    public EntityModel<Income> one(@PathVariable("inputId") Long inputId) {

        return incomeRepository.findById(inputId)
                .map(input -> incomeModelAssembler.toModel(input))
                .orElseThrow(() -> new ResourceNotFound(inputId));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Income>> saveMoney(@RequestBody Income income) {

        Income newIncome = incomeRepository.save(income);
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).one(newIncome.getId())).toUri())
                .body(incomeModelAssembler.toModel(newIncome));
    }
}
