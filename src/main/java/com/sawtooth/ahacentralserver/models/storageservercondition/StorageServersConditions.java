package com.sawtooth.ahacentralserver.models.storageservercondition;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

public class StorageServersConditions extends RepresentationModel<StorageServersConditions> {
    public final List<StorageServerCondition> conditions;

    public StorageServersConditions() {
        conditions = new ArrayList<>();
    }
}
