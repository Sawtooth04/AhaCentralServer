package com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator.validators;


import com.sawtooth.ahacentralserver.models.customer.CustomerRegistrationModel;
import com.sawtooth.ahacentralserver.services.customervalidator.abstractions.IValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NameValidator implements IValidator {
    @Value("${customer.name.max-length}")
    private int maxLength;
    @Value("${customer.name.min-length}")
    private int minLength;

    @Override
    public <T> boolean Validate(T model) {
        try {
            if (model instanceof CustomerRegistrationModel customerRegistrationModel)
                return customerRegistrationModel.name().length() >= minLength &&
                    customerRegistrationModel.name().length() <= maxLength;
        }
        catch (Exception exception) {
            return false;
        }
        return false;
    }
}
