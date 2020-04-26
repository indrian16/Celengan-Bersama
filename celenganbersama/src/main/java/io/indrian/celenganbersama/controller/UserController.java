package io.indrian.celenganbersama.controller;

import io.indrian.celenganbersama.assembler.UserModelAssembler;
import io.indrian.celenganbersama.exception.ResourceNotFound;
import io.indrian.celenganbersama.model.User;
import io.indrian.celenganbersama.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<User>> all() {

        List<EntityModel<User>> users = userRepository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(
                users,
                linkTo(methodOn(this.getClass()).all()).withSelfRel()
        );
    }

    @GetMapping("/{userId}")
    public EntityModel<User> one(@PathVariable("userId") Long userId) {

        return userRepository.findById(userId)
                .map(assembler::toModel)
                .orElseThrow(() -> new ResourceNotFound(userId));
    }

    @PostMapping
    public ResponseEntity<EntityModel<User>> createNewUser(@Valid @RequestBody User user) {

        User newUser = userRepository.save(user);
        return ResponseEntity
                .created(linkTo(methodOn(this.getClass()).one(newUser.getId())).toUri())
                .body(assembler.toModel(newUser));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> replaceUser(
            @PathVariable("userId") Long userId,
            @RequestBody User newUser
    ) {

        User updateUser = userRepository.findById(userId)
                .map(user -> {
                    user.setEmail(newUser.getEmail());
                    user.setFirstName(newUser.getFirstName());
                    user.setLastName(newUser.getLastName());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(userId);
                    return userRepository.save(newUser);
                });
        EntityModel<User> userModel = assembler.toModel(updateUser);
        return ResponseEntity
                .created(userModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(userModel);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {

        userRepository.deleteById(userId);
        return ResponseEntity.noContent().build();
    }
}
