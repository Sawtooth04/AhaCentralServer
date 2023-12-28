package com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator.validators;

import com.sawtooth.ahacentralserver.models.customer.CustomerRegistrationModel;
import com.sawtooth.ahacentralserver.services.customervalidator.abstractions.IValidator;

public class PasswordValidator implements IValidator {
    @Override
    public <T> boolean Validate(T model) {
        try {
            if (model instanceof CustomerRegistrationModel customerRegistrationModel) {
                return customerRegistrationModel.password().length() >= 8 &&
                    customerRegistrationModel.password().length() <= 20;
            }
        }
        catch (Exception exception) {
            return false;
        }
        return false;
    }
}
