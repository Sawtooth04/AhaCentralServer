package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.groupfileright.GroupFileRight;
import com.sawtooth.ahacentralserver.models.groupfileright.GroupsFileRights;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.groupfileright.IGroupFileRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/group-file-right")
public class GroupFileRightController {
    private final IStorage storage;

    @Autowired
    public GroupFileRightController(IStorage storage) {
        this.storage = storage;
    }

    @PutMapping("/put/map")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Put(@RequestBody GroupsFileRights groupsFileRights) {
        RepresentationModel<?> result = new RepresentationModel<>();
        GroupFileRight tempRight = new GroupFileRight();

        try {
            result.add(linkTo(methodOn(GroupFileRightController.class).Put(null)).withSelfRel());
            for (Map.Entry<Integer, Integer[]> groupRights : groupsFileRights.groupsFileRights().entrySet()) {
                tempRight.groupID = groupRights.getKey();
                for (Integer fileRightID : groupRights.getValue()) {
                    tempRight.fileRightID = fileRightID;
                    storage.GetRepository(IGroupFileRightRepository.class).Add(tempRight);
                }
            }
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.CREATED).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }
}
