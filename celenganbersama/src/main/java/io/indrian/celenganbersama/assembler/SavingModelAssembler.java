package io.indrian.celenganbersama.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import io.indrian.celenganbersama.controller.SavingController;
import io.indrian.celenganbersama.model.Saving;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SavingModelAssembler implements RepresentationModelAssembler<Saving, EntityModel<Saving>> {

    @Override
    public EntityModel<Saving> toModel(Saving saving) {
        return new EntityModel<>(
                saving,
                linkTo(methodOn(SavingController.class).one(saving.getId())).withSelfRel(),
                linkTo(methodOn(SavingController.class).all()).withRel("savings")
        );
    }
}
