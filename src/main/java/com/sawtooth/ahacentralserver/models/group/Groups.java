package com.sawtooth.ahacentralserver.models.group;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class Groups extends RepresentationModel<Groups> {
    public List<Group> groups;
}
