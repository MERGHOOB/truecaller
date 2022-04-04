package com.margub.truecaller.model;

import static com.margub.truecaller.Constants.*;
import com.margub.truecaller.GlobalContacts;
import com.margub.truecaller.model.common.Contact;
import com.margub.truecaller.model.common.GlobalSpam;
import com.margub.truecaller.model.common.PersonalInfo;
import com.margub.truecaller.model.common.UserCategory;
import com.margub.truecaller.model.exceptions.BlockLimitExceededException;
import com.margub.truecaller.model.exceptions.ContactDoesNotExistException;
import com.margub.truecaller.model.exceptions.ContactsExceedException;
import com.margub.truecaller.model.tries.ContactTrie;
import orestes.bloomfilter.FilterBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class User extends Account {

    public User() {
        ;
        setContactTrie(new ContactTrie());
    }

    public User(String phoneNumber, String firstName) {
        super(phoneNumber, firstName);
    }

    public User(String phoneNumber, String firstName, String lastName) {
        super(phoneNumber, firstName, lastName);
    }

    @Override
    public void register(UserCategory userCategory,
                         String userName,
                         String password,
                         String email,
                         String phoneNumber,
                         String countryCode,
                         String firstName) {

        setId(UUID.randomUUID().toString());
        setUserCategory(userCategory);
        setUserName(userName);
        setPassword(password);
        setContact(new Contact(phoneNumber, email, countryCode));
        setPersonalInfo(new PersonalInfo(firstName));

        init(userCategory);
        insertToTries(phoneNumber, firstName);
    }

    private void insertToTries(String phoneNumber, String firstName) {
        getContactTrie().insert(phoneNumber);
        getContactTrie().insert(firstName);
        GlobalContacts.INSTANCE.getContactTrie().insert(phoneNumber);
        GlobalContacts.INSTANCE.getContactTrie().insert(firstName);
    }

    private void init(UserCategory userCategory) {
        switch (userCategory) {
            case FREE:
                setContacts(new HashMap<>(MAX_FREE_USER_CONTACTS));
                setBlockedContacts(new FilterBuilder(MAX_FREE_USER_BLOCKED_CONTACTS, .01)
                        .buildCountingBloomFilter());
                setBlockedSet(new HashSet<>(MAX_FREE_USER_BLOCKED_CONTACTS));
                break;
            case GOLD:
                setContacts(new HashMap<>(MAX_GOLD_USER_CONTACTS));
                setBlockedContacts(new FilterBuilder(MAX_GOLD_USER_BLOCKED_CONTACTS, .01)
                        .buildCountingBloomFilter());
                setBlockedSet(new HashSet<>(MAX_GOLD_USER_BLOCKED_CONTACTS));
                break;
            case PLATINUM:
                setContacts(new HashMap<>(MAX_PLATINUM_USER_CONTACTS));
                setBlockedContacts(new FilterBuilder(MAX_PLATINUM_USER_BLOCKED_CONTACTS, .01)
                        .buildCountingBloomFilter());
                setBlockedSet(new HashSet<>(MAX_PLATINUM_USER_BLOCKED_CONTACTS));
                break;
        }
    }

    @Override
    public void addContact(User user) throws ContactsExceedException {
        checkAddUser();
        getContacts().putIfAbsent(user.getPhoneNumber(), user);
        insertToTries(user.getPhoneNumber(), user.getPersonalInfo().getFirstName());
    }

    private void checkAddUser() throws ContactsExceedException {
        switch (this.getUserCategory()) {
            case FREE:
                if (this.getContacts().size() >= MAX_FREE_USER_CONTACTS) {
                    throw new ContactsExceedException("Default contact size exceeded");
                }
                break;
            case GOLD:
                if (this.getContacts().size() >= MAX_GOLD_USER_CONTACTS) {
                    throw new ContactsExceedException("Default contact size exceeded");
                }
                break;
            case PLATINUM:
                if (this.getContacts().size() >= MAX_PLATINUM_USER_CONTACTS) {
                    throw new ContactsExceedException("Default contact size exceeded");
                }
                break;

        }
    }

    @Override
    public void removeContact(String phoneNumber) throws ContactDoesNotExistException {
        User contact = getContacts().get(phoneNumber);
        if (contact == null) {
            throw new ContactDoesNotExistException("Contact does not exist");
        }
        getContacts().remove(phoneNumber);
        getContactTrie().delete(phoneNumber);
        getContactTrie().delete(contact.getPersonalInfo().getFirstName());
    }

    @Override
    public void blockNumber(String number) throws BlockLimitExceededException {
        checkBlockUser();
        getBlockedContacts().add(number);
    }

    private void checkBlockUser() {
        //TODO;
    }

    @Override
    public void unblockNumber(String number) {
        getBlockedContacts().remove(number);
    }

    @Override
    public void reportSpam(String number, String reason) {
        getBlockedContacts().add(number);
        GlobalSpam.INSTANCE.reportSpam(number, this.getPhoneNumber(), reason);
    }

    @Override
    public void upgrade(UserCategory userCategory) {
        int count = 0;
        int blockedCount = 0;
        switch (userCategory) {
            case GOLD:
                count = MAX_GOLD_USER_CONTACTS;
                blockedCount = MAX_GOLD_USER_BLOCKED_CONTACTS;
                break;
            case PLATINUM:
                count = MAX_PLATINUM_USER_CONTACTS;
                blockedCount = MAX_PLATINUM_USER_BLOCKED_CONTACTS;
                break;
        }
        upgradeContacts(count);
        upgradeBlockedContact(blockedCount);
    }

    private void upgradeBlockedContact(int blockedCount) {
        setBlockedContacts(new FilterBuilder(blockedCount, 0.1)
                .buildCountingBloomFilter());
        Set<String> upgradedSet = new HashSet<>(blockedCount);
        upgradedSet.addAll(getBlockedSet());
        setBlockedSet(upgradedSet);
    }

    private void upgradeContacts(int count) {
        Map<String, User> upgradedContacts = new HashMap<>(count);
        for (Map.Entry<String, User> entry : getContacts().entrySet()) {
            upgradedContacts.putIfAbsent(entry.getKey(), entry.getValue());
        }
        setContacts(upgradedContacts);
    }

    @Override
    public boolean isBlocked(String phoneNumber) {
        return getBlockedContacts().contains(phoneNumber);
    }

    @Override
    public boolean canReceive(String phoneNumber) {
        return !isBlocked(phoneNumber) && !GlobalSpam.INSTANCE.isGlobalSpam(phoneNumber);
    }

    @Override
    public boolean importContacts(List<User> users) {
        for (User user : users) {
            try {
                addContact(user);
            } catch (ContactsExceedException e) {
                System.out.println("Some of the contacts could not be imported as limit exceeded");
                return false;
            }

        }
        return true;
    }

}
