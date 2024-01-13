package com.sawtooth.ahacentralserver.models.centralserver;

import com.sawtooth.ahacentralserver.models.storageserver.Space;
import org.springframework.hateoas.RepresentationModel;

public class General extends RepresentationModel<General> {
    public int storageServersCount, backupServersCount, customersCount;
    public float occupied;
    public long chunksCount;
    public Space free;
}
