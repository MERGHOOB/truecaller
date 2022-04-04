package com.margub.truecaller.model.common;

import lombok.Getter;
import lombok.Setter;

public class Contact {

    private String phoneNumber, email, countryCode;
    public Contact(String phoneNumber, String email, String countryCode) {
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setCountryCode(countryCode);
    }

    //Getter and Setter

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
