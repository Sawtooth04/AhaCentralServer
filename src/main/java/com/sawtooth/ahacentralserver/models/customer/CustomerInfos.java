package com.sawtooth.ahacentralserver.models.customer;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class CustomerInfos extends RepresentationModel<CustomerInfos> {
    public List<CustomerInfo> customerInfos;
}
