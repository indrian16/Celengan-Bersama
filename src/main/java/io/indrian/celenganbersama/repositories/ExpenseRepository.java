package io.indrian.celenganbersama.repositories;

import io.indrian.celenganbersama.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> { }
