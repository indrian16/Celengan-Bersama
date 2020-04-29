package io.indrian.celenganbersama.assembler;

import io.indrian.celenganbersama.controller.InputController;
import io.indrian.celenganbersama.model.Input;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class InputModelAssembler
        implements RepresentationModelAssembler<Input, EntityModel<Input>> {

    @Override
    public EntityModel<Input> toModel(Input input) {
        return new EntityModel<>(
                input,
                linkTo(methodOn(InputController.class).one(input.getId())).withSelfRel(),
                linkTo(methodOn(InputController.class).all()).withRel("inputs")
        );
    }
}
