package io.indrian.celenganbersama.assembler;

import io.indrian.celenganbersama.controller.JoinSavingController;
import io.indrian.celenganbersama.model.JoinSaving;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class JoinSavingModelAssembler
        implements RepresentationModelAssembler<JoinSaving, EntityModel<JoinSaving>> {

    @Override
    public EntityModel<JoinSaving> toModel(JoinSaving joinSaving) {
        return new EntityModel<>(
                joinSaving,
                linkTo(methodOn(JoinSavingController.class).one(joinSaving.getId())).withSelfRel(),
                linkTo(methodOn(JoinSavingController.class).all()).withRel("join_savings")
        );
    }
}
