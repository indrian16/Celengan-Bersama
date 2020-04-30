package io.indrian.celenganbersama.assembler;

import io.indrian.celenganbersama.controller.ExpenseController;
import io.indrian.celenganbersama.model.Expense;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ExpenseModelAssembler
        implements RepresentationModelAssembler<Expense, EntityModel<Expense>> {

    @Override
    public EntityModel<Expense> toModel(Expense expense) {
        return new EntityModel<>(
                expense,
                linkTo(methodOn(ExpenseController.class).one(expense.getId())).withSelfRel(),
                linkTo(methodOn(ExpenseController.class).all()).withRel("expenses")
        );
    }
}