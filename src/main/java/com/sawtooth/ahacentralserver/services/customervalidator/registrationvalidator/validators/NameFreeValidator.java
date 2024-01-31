package com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator.validators;

import com.sawtooth.ahacentralserver.models.customer.CustomerRegistrationModel;
import com.sawtooth.ahacentralserver.services.customervalidator.abstractions.IValidator;
import com.sawtooth.ahacentralserver.storage.IStorage;
import com.sawtooth.ahacentralserver.storage.repositories.customer.ICustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class NameFreeValidator implements IValidator {
    private final IStorage storage;

    public NameFreeValidator(IStorage storage) {
        this.storage = storage;
    }

    @Override
    public <T> boolean Validate(T model) {
        try {
            if (model instanceof CustomerRegistrationModel customerRegistrationModel)
                return storage.GetRepository(ICustomerRepository.class).IsCustomerNameFree(customerRegistrationModel.name());
        }
        catch (Exception exception) {
            return false;
        }
        return false;
    }
}
