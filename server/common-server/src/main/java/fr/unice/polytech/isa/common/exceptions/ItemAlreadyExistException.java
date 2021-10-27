package fr.unice.polytech.isa.common.exceptions;


import java.io.Serializable;

public class ItemAlreadyExistException extends Exception implements Serializable {
    private String item;

    public ItemAlreadyExistException() {}

    public ItemAlreadyExistException(String item) {
        super(item + ": ItemAlreadyExistException");
        this.item = item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }
}
