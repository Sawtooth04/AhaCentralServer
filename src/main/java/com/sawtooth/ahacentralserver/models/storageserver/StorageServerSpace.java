package com.sawtooth.ahacentralserver.models.storageserver;

import org.springframework.hateoas.RepresentationModel;

public class StorageServerSpace extends RepresentationModel<StorageServerSpace> {
    public double freeSpace;
    public double totalSpace;
}
