package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.authentication.Authentication;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

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
        Authentication result = new Authentication();

        result.add(linkTo(methodOn(AuthenticationController.class).Get(null)).withSelfRel());
        result.add(linkTo(methodOn(AuthenticationController.class).Logout(new Response())).withRel("logout"));
        if (principal != null) {
            result.isAuthenticated = true;
            result.name = principal.getName();
        }
        return CompletableFuture.completedFuture(ResponseEntity.ok(result));
    }

    @PostMapping("/logout")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Logout(HttpServletResponse response) {
        RepresentationModel<?> result = new RepresentationModel<>();
        Cookie cookie = new Cookie("JSESSIONID", "");

        result.add(linkTo(methodOn(AuthenticationController.class).Logout(new Response())).withSelfRel());
        result.add(linkTo(methodOn(AuthenticationController.class).Get(null)).withRel("get"));
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return CompletableFuture.completedFuture(ResponseEntity.ok(result));
    }
}
