package com.tpp.tpplab3.controllers;

import com.tpp.tpplab3.models.Passengers;
import com.tpp.tpplab3.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public String listPassengers(Model model) {
        model.addAttribute("passengers", passengerService.getAllPassengers());
        return "passengers";
    }

    @GetMapping("/add")
    public String addPassengerForm(Model model) {
        model.addAttribute("passenger", new Passengers());
        return "add-passenger";
    }

    @PostMapping("/add")
    public String addPassenger(@Valid @ModelAttribute("passenger") Passengers passenger, BindingResult result) {
        if (result.hasErrors()) {
            return "add-passenger";
        }
        passengerService.addPassenger(passenger);
        return "redirect:/passengers";
    }

    @GetMapping("/edit/{id}")
    public String editPassengerForm(@PathVariable("id") int id, Model model) {
        Passengers passenger = passengerService.findById(id).orElse(null);
        if (passenger != null) {
            model.addAttribute("passenger", passenger);
            return "edit-passenger";
        } else {
            return "redirect:/passengers";
        }
    }

    @PostMapping("/update/{id}")
    public String updatePassenger(@PathVariable("id") int id, @Valid @ModelAttribute("passenger") Passengers passenger, BindingResult result) {
        if (result.hasErrors()) {
            return "edit-passenger";
        }
        passenger.setPassengerId(id);
        passengerService.updatePassenger(passenger);
        return "redirect:/passengers";
    }

    @GetMapping("/delete/{id}")
    public String deletePassenger(@PathVariable("id") int id) {
        passengerService.deletePassenger(id);
        return "redirect:/passengers";
    }
    
    @PostMapping("/execute-query")
    public String executeQuery(@RequestParam("sqlQuery") String sqlQuery, Model model) {
        try {
            String trimmedQuery = sqlQuery.trim().toLowerCase();
            
            // Check if query is of type SELECT
            if (trimmedQuery.startsWith("select")) {
                List<Map<String, Object>> result = jdbcTemplate.queryForList(sqlQuery);
                model.addAttribute("queryResult", result);
            } else {
                // Execute update for INSERT, UPDATE, DELETE
                int rowsAffected = jdbcTemplate.update(sqlQuery);
                model.addAttribute("message", "Query executed successfully. Rows affected: " + rowsAffected);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error executing query: " + e.getMessage());
        }
        return "passengers";
    }
}
