package io.indrian.celenganbersama.controller;

import io.indrian.celenganbersama.assembler.ExpenseModelAssembler;
import io.indrian.celenganbersama.custombody.InputBody;
import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.Expense;
import io.indrian.celenganbersama.model.Saving;
import io.indrian.celenganbersama.model.User;
import io.indrian.celenganbersama.repositories.ExpenseRepository;
import io.indrian.celenganbersama.repositories.SavingRepository;
import io.indrian.celenganbersama.repositories.UserRepository;
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
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SavingRepository savingRepository;

    @Autowired
    private ExpenseModelAssembler expenseModelAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Expense>> all() {

        List<EntityModel<Expense>> expenses = expenseRepository.findAll()
                .stream()
                .map(expenseModelAssembler::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(
                expenses,
                linkTo(methodOn(this.getClass()).all()).withSelfRel()
        );
    }

    @GetMapping("/{inputId}")
    public EntityModel<Expense> one(@PathVariable("inputId") Long inputId) {

        return expenseRepository.findById(inputId)
                .map(input -> expenseModelAssembler.toModel(input))
                .orElseThrow(() -> new ResourceNotFound(inputId));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Expense>> expense(@RequestBody InputBody inputBody) {
        Expense expense = new Expense();
        expense.setAmount(inputBody.getAmount());
        expense.setNote(inputBody.getNote());

        // Find User and Saving
        User user = userRepository.findById(inputBody.getUserId())
                .orElseThrow(() -> new ResourceNotFound(inputBody.getUserId()));
        Saving saving = savingRepository.findById(inputBody.getSavingId())
                .orElseThrow(() ->  new ResourceNotFound(inputBody.getSavingId()));

        // Joining
        expense.setUser(user);
        expense.setSaving(saving);

        Expense newExpense = expenseRepository.save(expense);
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).one(newExpense.getId())).toUri())
                .body(expenseModelAssembler.toModel(newExpense));
    }
}
