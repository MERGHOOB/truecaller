package com.margub.truecaller.model;

import com.margub.truecaller.model.common.BusinessSize;
import com.margub.truecaller.model.common.Contact;
import com.margub.truecaller.model.common.PersonalInfo;
import com.margub.truecaller.model.common.SocialInfo;
import com.margub.truecaller.model.common.Tag;

import java.util.Map;

public class Business {
    private String businessName;
    private String businessDescription;
    private Tag tag;
    private BusinessSize businessSize;
    private Map<Days, OperatingHours> openHours;
    private Contact contact;
    private PersonalInfo personalInfo;
    private SocialInfo socialInfo;


    public Business(String name, Tag tag) {
        this.businessName = name;
        this.tag = tag;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public BusinessSize getBusinessSize() {
        return businessSize;
    }

    public void setBusinessSize(BusinessSize businessSize) {
        this.businessSize = businessSize;
    }

    public Map<Days, OperatingHours> getOpenHours() {
        return openHours;
    }

    public void setOpenHours(Map<Days, OperatingHours> openHours) {
        this.openHours = openHours;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public SocialInfo getSocialInfo() {
        return socialInfo;
    }

    public void setSocialInfo(SocialInfo socialInfo) {
        this.socialInfo = socialInfo;
    }
}
