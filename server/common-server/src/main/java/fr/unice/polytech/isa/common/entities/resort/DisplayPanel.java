package fr.unice.polytech.isa.common.entities.resort;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DisplayPanel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private int externalId;

    //TODO private Location location;

    public DisplayPanel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getExternalId() {
        return externalId;
    }

    public void setExternalId(int externalId) {
        this.externalId = externalId;
    }
}
