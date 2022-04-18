package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class Workshops implements Serializable {

    public Workshops(){}

    public String ID,COVER,TITTLE,ORGANISED_BY,DURATION,MODE;

    public Workshops(String ID, String COVER, String TITTLE, String ORGANISED_BY, String DURATION, String MODE) {
        this.ID = ID;
        this.COVER = COVER;
        this.TITTLE = TITTLE;
        this.ORGANISED_BY = ORGANISED_BY;
        this.DURATION = DURATION;
        this.MODE = MODE;
    }
}
