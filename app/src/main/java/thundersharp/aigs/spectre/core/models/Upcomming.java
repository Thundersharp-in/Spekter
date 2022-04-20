package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class Upcomming implements Serializable {

    public Upcomming(){}

    public String DESCRIPTION,ID,LOCATION,TITTLE;

    public Upcomming(String DESCRIPTION, String ID, String LOCATION, String TITTLE) {
        this.DESCRIPTION = DESCRIPTION;
        this.ID = ID;
        this.LOCATION = LOCATION;
        this.TITTLE = TITTLE;
    }
}
