package com.margub.truecaller.model.common;

import lombok.Getter;
import lombok.Setter;

public class PersonalInfo {

    private String firstName;
    private String lastName;

    public PersonalInfo(String firstName) {
        setFirstName(firstName);
    }


    //Getter and Setter
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
