package com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator.validators;


import com.sawtooth.ahacentralserver.models.customer.CustomerRegistrationModel;
import com.sawtooth.ahacentralserver.services.customervalidator.abstractions.IValidator;

public class NameValidator implements IValidator {
    @Override
    public <T> boolean Validate(T model) {
        try {
            if (model instanceof CustomerRegistrationModel customerRegistrationModel)
                return customerRegistrationModel.name().length() >= 8 && customerRegistrationModel.name().length() <= 30;
        }
        catch (Exception exception) {
            return false;
        }
        return false;
    }
}
