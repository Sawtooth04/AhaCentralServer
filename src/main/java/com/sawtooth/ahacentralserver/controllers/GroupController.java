package com.sawtooth.ahacentralserver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.sawtooth.ahacentralserver.models.customer.Customer;
import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.group.Group;
import com.sawtooth.ahacentralserver.models.group.GroupAddModel;
import com.sawtooth.ahacentralserver.models.group.Groups;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import com.sawtooth.ahacentralserver.storage.repositories.file.IFileRepository;
import com.sawtooth.ahacentralserver.storage.repositories.group.GroupRepository;
import com.sawtooth.ahacentralserver.storage.repositories.group.IGroupRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.security.Principal;
import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final IStorage storage;

    @Autowired
    public GroupController(IStorage storage) {
        this.storage = storage;
    }

    private int GetGroupID(HttpServletRequest request) {
        return Integer.parseInt(((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
            .replaceAll("(.*/)", ""));
    }

    @GetMapping("/get")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<Groups>> Get(Principal principal) {
        Groups result = new Groups();
        Customer customer;

        try {
            result.add(linkTo(methodOn(GroupController.class).Get(null)).withSelfRel());
            customer = storage.GetRepository(ICustomerRepository.class).Get(principal.getName());
            result.groups = storage.GetRepository(IGroupRepository.class).Get(customer);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @GetMapping("/get/own")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<Groups>> GetOwn(Principal principal) {
        Groups result = new Groups();
        Customer customer;

        try {
            result.add(linkTo(methodOn(GroupController.class).GetOwn(null)).withSelfRel());
            customer = storage.GetRepository(ICustomerRepository.class).Get(principal.getName());
            result.groups = storage.GetRepository(IGroupRepository.class).GetOwn(customer);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @PostMapping("/add")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Add(@RequestBody GroupAddModel model, Principal principal) {
        RepresentationModel<?> result = new RepresentationModel<>();
        Customer customer;

        try {
            result.add(linkTo(methodOn(GroupController.class).Add(null, null)).withSelfRel());
            customer = storage.GetRepository(ICustomerRepository.class).Get(principal.getName());
            storage.GetRepository(IGroupRepository.class).Add(customer, new Group(-1, model.name(),  -1,
                customer.name()));
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @GetMapping("/patch")
    @ResponseBody
    public CompletableFuture<ResponseEntity<?>> Patch() {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));
    }

    @PatchMapping("/patch/**")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Patch(@RequestBody JsonPatch patch, HttpServletRequest request) {
        RepresentationModel<?> result = new RepresentationModel<>();
        Group group;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            group = storage.GetRepository(IGroupRepository.class).Get(GetGroupID(request));
            storage.GetRepository(IGroupRepository.class).Update(objectMapper.treeToValue(patch.apply(
                objectMapper.convertValue(group, JsonNode.class)), Group.class));
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NO_CONTENT).body(result));
        }
        catch (EmptyResultDataAccessException exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    public CompletableFuture<ResponseEntity<?>> Delete() {
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.FORBIDDEN).body(null));
    }

    @DeleteMapping("/delete/**")
    @Async
    @ResponseBody
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Delete(HttpServletRequest request) {
        RepresentationModel<?> result = new RepresentationModel<>();
        Group group;

        try {
            group = storage.GetRepository(IGroupRepository.class).Get(GetGroupID(request));
            storage.GetRepository(IGroupRepository.class).Delete(group);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (EmptyResultDataAccessException exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NOT_FOUND).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }
}
