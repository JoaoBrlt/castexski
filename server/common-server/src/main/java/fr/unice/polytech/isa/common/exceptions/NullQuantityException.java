package fr.unice.polytech.isa.common.exceptions;


import java.io.Serializable;

public class NullQuantityException extends Exception implements Serializable {
    public NullQuantityException() {}

    public NullQuantityException(String item) {
        super(item + ": NullQuantityException");
    }
}
