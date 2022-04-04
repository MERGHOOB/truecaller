package com.margub.truecaller.model.tries;

import java.util.ArrayList;
import java.util.List;

public class ContactTrie {

    private TrieNode root;
    private int indexOfSingleChild;

    public ContactTrie() {
        this.root = new TrieNode("");
    }

    public void insert(String key) {
        TrieNode tempTrieNode = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (tempTrieNode.getChild(c) == null) {

                TrieNode trieNode = new TrieNode(String.valueOf(c));
                tempTrieNode.setChild(c, trieNode);

            }
            tempTrieNode = tempTrieNode.getChild(c);
        }
        tempTrieNode.setLeaf(true);

    }

    public List<String> allWordsWithPrefix(String prefix) {
        TrieNode trieNode = root;
        List<String> allWords = new ArrayList<>();
        for (int i = 0; i < prefix.length(); i++) {
            char c = prefix.charAt(i);
            trieNode = trieNode.getChild(c);
        }
        getSuffixes(trieNode, prefix, allWords);
        return allWords;
    }


    private void getSuffixes(TrieNode trieNode, String prefix, List<String> allWords) {
        if (trieNode == null) {
            return;
        }
        if (trieNode.isLeaf()) {
            allWords.add(prefix);
        }

        for (TrieNode childNode : trieNode.getChildren()) {
            if (childNode == null) {
                continue;
            }
            String childCharacter = childNode.getCharacter();
            getSuffixes(childNode, prefix + childCharacter, allWords);
        }

    }

    public String longestCommonPrefix() {
        TrieNode trieNode = root;
        String longestCommonPrefix = "";
        while (countNumOfChildren(trieNode) == 1 && !trieNode.isLeaf()) {
            trieNode = trieNode.getChild(indexOfSingleChild);
            longestCommonPrefix = longestCommonPrefix + (char) indexOfSingleChild + 'a';
        }
        return longestCommonPrefix;
    }

    private int countNumOfChildren(TrieNode trieNode) {
        int numOfChildren = 0;
        for (int i = 0; i < trieNode.getChildren().length; i++) {
            if (trieNode.getChild(i) != null) {
                numOfChildren++;
                indexOfSingleChild = i;
            }
        }
        return numOfChildren;
    }

    public boolean search(String key) {
        TrieNode currentNode = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (currentNode.getChild(c) != null) {
                currentNode = currentNode.getChild(c);
            } else {
                return false;
            }
        }
        return true;
    }

    public void delete(String key) {
        if (root == null || key == null) {
            System.out.println("Null Key or Empty trie error");
            return;
        }
        deleteHelper(key, root, key.length(), 0);

    }

    private boolean deleteHelper(String key, TrieNode currentTrieNode, int length, int level) {
        boolean deletedSelf = false;
        if (currentTrieNode == null) {
            System.out.println("Key does not exist");
            return deletedSelf;
        }
        if (level == length) {
            if (currentTrieNode.isLeaf()) {
                currentTrieNode = null;
                deletedSelf = true;
            } else {
                currentTrieNode.setLeaf(false);
                deletedSelf = false;
            }
        } else {

            TrieNode childNode = currentTrieNode.getChild(key.charAt(level));
            boolean childDeleted = deleteHelper(key, childNode, length, level + 1);

            if (childDeleted) {
                currentTrieNode.setChild(key.charAt(level), null);
                if (currentTrieNode.isLeaf()) {
                    deletedSelf = false;
                } else if (currentTrieNode.getChildren().length > 0) {
                    deletedSelf = false;
                } else {
                    currentTrieNode = null;
                    deletedSelf = true;
                }
            } else {
                deletedSelf = false;
            }

        }
        return deletedSelf;
    }
}
