package com.sawtooth.ahacentralserver.models.storageserverstatus;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class StorageServerStatuses extends RepresentationModel<StorageServerStatuses> {
    public List<StorageServerStatus> statuses;
}
