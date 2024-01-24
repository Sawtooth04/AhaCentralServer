package com.sawtooth.ahacentralserver.models.groupfileright;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class GroupFileRights extends RepresentationModel<GroupFileRights> {
    public List<GroupFileRight> groupFileRights;
}
