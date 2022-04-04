package com.margub.truecaller;

import com.margub.truecaller.model.tries.ContactTrie;


public class GlobalContacts {

    public static final GlobalContacts INSTANCE = new GlobalContacts();
    private ContactTrie contactTrie;

    private GlobalContacts() {
        contactTrie = new ContactTrie();
    }

    //Getter

    public ContactTrie getContactTrie() {
        return contactTrie;
    }
}
