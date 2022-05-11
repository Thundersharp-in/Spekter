package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class Initiative implements Serializable {

    public Initiative(){}

    public String NAME,PLEDGE,PLEDGE_URL,READ_URL,ID,STARTED_BY,COVER_PIC;

    public Initiative(String NAME, String PLEDGE, String PLEDGE_URL, String READ_URL, String ID, String STARTED_BY, String COVER_PIC) {
        this.NAME = NAME;
        this.PLEDGE = PLEDGE;
        this.PLEDGE_URL = PLEDGE_URL;
        this.READ_URL = READ_URL;
        this.ID = ID;
        this.STARTED_BY = STARTED_BY;
        this.COVER_PIC = COVER_PIC;
    }
}
