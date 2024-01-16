package com.sawtooth.ahacentralserver.models.file;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class DirectoryItems extends RepresentationModel<DirectoryItems> {
    public List<DirectoryItem> items;
}
