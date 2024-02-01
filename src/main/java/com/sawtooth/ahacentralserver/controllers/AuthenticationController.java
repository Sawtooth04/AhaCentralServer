package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.authentication.Authentication;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    @GetMapping("/get")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<RepresentationModel<Authentication>>> Get(Principal principal) {
        Authentication authentication = new Authentication();

        authentication.add(linkTo(methodOn(AuthenticationController.class).Get(null)).withSelfRel());
        if (principal != null) {
            authentication.isAuthenticated = true;
            authentication.name = principal.getName();
        }
        return CompletableFuture.completedFuture(ResponseEntity.ok(authentication));
    }
}
