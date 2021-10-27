package fr.unice.polytech.isa.common.exceptions;


import java.io.Serializable;

public class UnknownCatalogEntryException extends Exception implements Serializable {

    public UnknownCatalogEntryException() {}

    public UnknownCatalogEntryException(String itemName) {
        super(itemName + ": UnknownCatalogEntryException");
    }

    public UnknownCatalogEntryException(int itemId) {
        super(itemId + ": UnknownCatalogEntryException");
    }}
