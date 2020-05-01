package io.indrian.celenganbersama.repositories;

import io.indrian.celenganbersama.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
