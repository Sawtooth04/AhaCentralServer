package com.sawtooth.ahacentralserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.customer.CustomerInfos;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final IStorage storage;
    private final PasswordEncoder passwordEncoder;
    @Value("${customer.password.max-length}")
    private int passwordMaxLength;

    @Autowired
    public CustomerController(IStorage storage, PasswordEncoder passwordEncoder) {
        this.storage = storage;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/info/get")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<CustomerInfos>> GetInfo() {
        CustomerInfos result = new CustomerInfos();

        result.add(linkTo(methodOn(CustomerController.class).GetInfo()).withSelfRel());
        result.add(linkTo(methodOn(CustomerController.class).Patch(null, null, null)).withRel("patch"));
        result.add(linkTo(methodOn(CustomerController.class).Delete(null, null)).withRel("delete"));
        try {
            result.customerInfos = storage.GetRepository(ICustomerRepository.class).GetInfos();
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @PatchMapping("/patch/{name}")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Patch(Principal principal, @RequestBody JsonPatch patch, @PathVariable String name) {
        RepresentationModel<?> result = new RepresentationModel<>();
        Customer customer;
        ICustomerRepository customerRepository;
        ObjectMapper objectMapper = new ObjectMapper();

        result.add(linkTo(methodOn(CustomerController.class).GetInfo()).withRel("get"));
        result.add(linkTo(methodOn(CustomerController.class).Patch(null, null, null)).withSelfRel());
        result.add(linkTo(methodOn(CustomerController.class).Delete(null, null)).withRel("delete"));
        try {
            customerRepository = storage.GetRepository(ICustomerRepository.class);
            if (customerRepository.IsCustomerHaveRole(customerRepository.Get(principal.getName()), "admin")) {
                customer = objectMapper.treeToValue(patch.apply(objectMapper.convertValue(storage.GetRepository(ICustomerRepository.class).Get(name),
                    JsonNode.class)), Customer.class);
                if (customer.passwordHash().length() <= passwordMaxLength)
                    customer = customer.WithPasswordHash(passwordEncoder.encode(customer.passwordHash()));
                storage.GetRepository(ICustomerRepository.class).Update(customer);
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NO_CONTENT).body(result));
            }
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(result));
        }
        catch (EmptyResultDataAccessException exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @DeleteMapping("/delete/{name}")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Delete(Principal principal, @PathVariable String name) {
        RepresentationModel<?> result = new RepresentationModel<>();
        ICustomerRepository customerRepository;

        result.add(linkTo(methodOn(CustomerController.class).GetInfo()).withRel("get"));
        result.add(linkTo(methodOn(CustomerController.class).Patch(null, null, null)).withRel("patch"));
        result.add(linkTo(methodOn(CustomerController.class).Delete(null, null)).withSelfRel());
        try {
            customerRepository = storage.GetRepository(ICustomerRepository.class);
            if (customerRepository.IsCustomerHaveRole(customerRepository.Get(principal.getName()), "admin")) {
                storage.GetRepository(ICustomerRepository.class).Delete(name);
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NO_CONTENT).body(result));
            }
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }
}
