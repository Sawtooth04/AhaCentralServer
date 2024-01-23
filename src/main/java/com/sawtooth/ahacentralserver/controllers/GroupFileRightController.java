package com.sawtooth.ahacentralserver.controllers;

import com.sawtooth.ahacentralserver.models.file.File;
import com.sawtooth.ahacentralserver.models.groupfileright.GroupFileRight;
import com.sawtooth.ahacentralserver.models.groupfileright.GroupFileRightPostModel;
import com.sawtooth.ahacentralserver.services.filepathprocessor.IFilePathProcessor;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.file.IFileRepository;
import com.sawtooth.ahacentralserver.storage.repositories.groupfileright.IGroupFileRightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/group-file-right")
public class GroupFileRightController {
    private final IStorage storage;
    private final IFilePathProcessor filePathProcessor;

    @Autowired
    public GroupFileRightController(IStorage storage, IFilePathProcessor filePathProcessor) {
        this.storage = storage;
        this.filePathProcessor = filePathProcessor;
    }

    @PutMapping("/post")
    @ResponseBody
    @Async
    public CompletableFuture<ResponseEntity<RepresentationModel<?>>> Post(@RequestBody GroupFileRightPostModel model) {
        RepresentationModel<?> result = new RepresentationModel<>();
        File file;

        try {
            model = model.WithPath(filePathProcessor.ReplaceFilePathParts(model.path()));
            result.add(linkTo(methodOn(GroupFileRightController.class).Post(null)).withSelfRel());
            file = storage.GetRepository(IFileRepository.class).Get(model.path(), model.fileName());
            storage.GetRepository(IGroupFileRightRepository.class).Add(model.groupFileRight().WithFileID(file.fileID()));
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(result));
        }
        catch (Exception exception) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result));
        }
    }
}
