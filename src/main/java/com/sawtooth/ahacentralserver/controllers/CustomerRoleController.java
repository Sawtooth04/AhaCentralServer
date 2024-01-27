package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.customerrole.CustomerRole;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/customer-role")
public class CustomerRoleController {
    private final IStorage storage;

    @Autowired
    public CustomerRoleController(IStorage storage) {
        this.storage = storage;
    }

    @GetMapping("/get/{role}")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<CustomerRole>> Get(@PathVariable String role, Principal principal) {
        CustomerRole result = new CustomerRole();
        ICustomerRepository repository;

        try {
            repository = storage.GetRepository(ICustomerRepository.class);
            result.isHaveRole = repository.IsCustomerHaveRole(repository.Get(principal.getName()), role);
            return CompletableFuture.completedFuture(ResponseEntity.ok(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }
}
