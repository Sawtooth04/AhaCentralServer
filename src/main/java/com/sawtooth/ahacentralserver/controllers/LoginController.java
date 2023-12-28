package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.customer.CustomerLoginModel;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/login")
public class LoginController {
    private final IStorage storage;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(IStorage storage, PasswordEncoder passwordEncoder) {
        this.storage = storage;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<?>> Login(@RequestBody CustomerLoginModel loginModel) {
        try {
            Customer customer = storage.GetRepository(ICustomerRepository.class).Get(loginModel.name());
            if (passwordEncoder.matches(loginModel.password(), customer.passwordHash())) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customer.name(),
                        customer.passwordHash());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).build());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}
