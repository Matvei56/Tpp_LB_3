package com.tpp.tpplab3.service;

import com.tpp.tpplab3.models.Passengers;
import com.tpp.tpplab3.repository.PassengersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PassengerService {

    @Autowired
    private PassengersRepository passengerRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Passengers> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Optional<Passengers> findById(int id) {
        return passengerRepository.findById(id);
    }

    public void addPassenger(Passengers passenger) {
        passengerRepository.save(passenger);
    }

    public void updatePassenger(Passengers updatedPassenger) {
        Passengers existingPassenger = passengerRepository.findById(updatedPassenger.getPassengerId())
                .orElseThrow(() -> new IllegalArgumentException("Passenger not found"));

        existingPassenger.setName(updatedPassenger.getName());
        existingPassenger.setSurname(updatedPassenger.getSurname());
        existingPassenger.setPhone(updatedPassenger.getPhone());
        existingPassenger.setEmail(updatedPassenger.getEmail());

        existingPassenger.setOrders(updatedPassenger.getOrders());

        passengerRepository.save(existingPassenger);
    }

    public void deletePassenger(int id) {
        passengerRepository.deleteById(id);
    }

    public List<Map<String, Object>> executeQuery(String sqlQuery) {
        return jdbcTemplate.queryForList(sqlQuery);
    }
}
