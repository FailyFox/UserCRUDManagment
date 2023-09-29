package com.usercrudmanagment.controller;

import com.usercrudmanagment.dto.UserRequestDto;
import com.usercrudmanagment.exception.InvalidDateRangeException;
import com.usercrudmanagment.exception.PropertiesNotFoundException;
import com.usercrudmanagment.exception.UserUnder18Exception;
import com.usercrudmanagment.model.User;
import com.usercrudmanagment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final String PROPERTIES_PATH = "src/main/resources/application.properties";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody UserRequestDto user) {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(PROPERTIES_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
            if (calculatedAge(user.getBirthDate(), LocalDate.now())
                    < Integer.parseInt(properties.getProperty("MIN_AGE"))) {
                throw new UserUnder18Exception();
            }
            return userService.createUser(user);
        } catch (IOException e) {
            throw new PropertiesNotFoundException();
        }
    }
    @GetMapping("/search")
    public List<User> searchUsersByBirthDate(@RequestParam LocalDate fromDate, @RequestParam LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new InvalidDateRangeException();
        }
        return userService.searchUsersByBirthDate(fromDate, toDate);
    }
    @PatchMapping("/{id}")
    public User updateUserFields(@PathVariable Integer id,
                                 @Valid @RequestBody Map<String, Object> fields) {
        return userService.updateUserFields(id, fields);
    }
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id,
                           @Valid @RequestBody UserRequestDto user) {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(PROPERTIES_PATH);
            properties.load(fileInputStream);
            fileInputStream.close();
            if (calculatedAge(user.getBirthDate(), LocalDate.now())
                    < Integer.parseInt(properties.getProperty("MIN_AGE"))) {
                throw new UserUnder18Exception();
            }
            return userService.updateUser(id, user);
        } catch (IOException e) {
            throw new PropertiesNotFoundException();
        }
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
    private int calculatedAge(LocalDate birthDate, LocalDate currentDate) {
        return Period.between(birthDate, currentDate).getYears();
    }
}