package com.sawtooth.ahacentralserver.models.customer;

public record Customer (int customerID, String name, String passwordHash) {
    public Customer WithPasswordHash(String passwordHash) {
        return new Customer(customerID, name, passwordHash);
    }
}
