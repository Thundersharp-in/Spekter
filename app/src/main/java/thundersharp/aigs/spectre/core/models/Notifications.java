package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class Notifications implements Serializable {

    public Notifications(){}

    public String TITTLE;
    public String NOTIFICATION;
    public String PAYLOAD;
    public String ID;
    public String PUBLISHER;

    public Notifications(String TITTLE, String NOTIFICATION, String PAYLOAD, String ID, String PUBLISHER) {
        this.TITTLE = TITTLE;
        this.NOTIFICATION = NOTIFICATION;
        this.PAYLOAD = PAYLOAD;
        this.ID = ID;
        this.PUBLISHER = PUBLISHER;
    }
}
