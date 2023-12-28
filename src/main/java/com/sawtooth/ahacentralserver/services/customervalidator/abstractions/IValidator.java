package com.sawtooth.ahacentralserver.services.customervalidator.abstractions;

public interface IValidator {
    public <T> boolean Validate(T model);
}
