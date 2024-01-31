package com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator.validators;

import com.sawtooth.ahacentralserver.models.customer.CustomerRegistrationModel;
import com.sawtooth.ahacentralserver.services.customervalidator.abstractions.IValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class PasswordValidator implements IValidator {
    @Value("${customer.password.max-length}")
    private int maxLength;
    @Value("${customer.password.min-length}")
    private int minLength;

    @Override
    public <T> boolean Validate(T model) {
        try {
            if (model instanceof CustomerRegistrationModel customerRegistrationModel)
                return customerRegistrationModel.password().length() >= minLength &&
                    customerRegistrationModel.password().length() <= maxLength;
        }
        catch (Exception exception) {
            return false;
        }
        return false;
    }
}
