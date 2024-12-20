package com.tpp.tpplab3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpp.tpplab3.models.Passengers;

@Repository
public interface PassengersRepository extends JpaRepository<Passengers, Integer> {
}