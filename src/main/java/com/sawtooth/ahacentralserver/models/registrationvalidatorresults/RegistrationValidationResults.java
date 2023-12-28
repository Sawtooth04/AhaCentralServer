package com.sawtooth.ahacentralserver.models.registrationvalidatorresults;

public class RegistrationValidationResults {
    public boolean total, isNameFree, isNameValid, isPasswordValid;

    public void setTotal() {
        this.total = isNameFree && isNameValid && isPasswordValid;
    }
}
