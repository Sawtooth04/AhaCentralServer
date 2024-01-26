package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.customer.CustomerRegistrationModel;
import com.sawtooth.ahacentralserver.models.registrationvalidatorresults.RegistrationValidationResults;
import com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator.RegistrationValidator;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    private final IStorage storage;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationValidator validator;

    @Autowired
    public RegistrationController(IStorage storage, PasswordEncoder passwordEncoder, RegistrationValidator validator) {
        this.storage = storage;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @PostMapping("/register")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<RegistrationValidationResults>> Register(@RequestBody CustomerRegistrationModel registrationModel) {
        try {
            if (validator.Validate(registrationModel)) {
                Customer customer = new Customer(-1, registrationModel.name(),
                    passwordEncoder.encode(registrationModel.password()));
                storage.GetRepository(ICustomerRepository.class).Add(customer);
                return CompletableFuture.completedFuture(ResponseEntity.ok(null));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(validator.GetResults()));
    }
}
