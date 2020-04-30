package io.indrian.celenganbersama.assembler;

import io.indrian.celenganbersama.controller.IncomeController;
import io.indrian.celenganbersama.model.Income;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class IncomeModelAssembler
        implements RepresentationModelAssembler<Income, EntityModel<Income>> {

    @Override
    public EntityModel<Income> toModel(Income income) {
        return new EntityModel<>(
                income,
                linkTo(methodOn(IncomeController.class).one(income.getId())).withSelfRel(),
                linkTo(methodOn(IncomeController.class).all()).withRel("inputs")
        );
    }
}
