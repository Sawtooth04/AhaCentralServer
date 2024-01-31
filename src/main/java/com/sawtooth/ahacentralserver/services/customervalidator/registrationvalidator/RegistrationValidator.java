package com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator;

import com.sawtooth.ahacentralserver.models.registrationvalidatorresults.RegistrationValidationResults;
import com.sawtooth.ahacentralserver.services.customervalidator.abstractions.IValidator;
import com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator.validators.NameFreeValidator;
import com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator.validators.NameValidator;
import com.sawtooth.ahacentralserver.services.customervalidator.registrationvalidator.validators.PasswordValidator;
import com.sawtooth.ahacentralserver.storage.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationValidator implements IValidator {
    private final RegistrationValidationResults results;
    private final IValidator nameFreeValidator, nameValidator, passwordValidator;

    @Autowired
    public RegistrationValidator(NameFreeValidator nameFreeValidator, NameValidator nameValidator, PasswordValidator passwordValidator) {
        this.results = new RegistrationValidationResults();
        this.nameFreeValidator = nameFreeValidator;
        this.nameValidator = nameValidator;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public <T> boolean Validate(T model) {
        results.isNameFree = nameFreeValidator.Validate(model);
        results.isNameValid = nameValidator.Validate(model);
        results.isPasswordValid = passwordValidator.Validate(model);
        results.setTotal();
        return results.total;
    }

    public RegistrationValidationResults GetResults() {
        return results;
    }
}
