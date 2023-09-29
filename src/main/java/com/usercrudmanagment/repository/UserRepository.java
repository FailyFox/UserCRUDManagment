package com.usercrudmanagment.repository;

import com.usercrudmanagment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByBirthDateBetween(LocalDate fromDate, LocalDate toDate);
}