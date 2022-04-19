package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class RELATED_FILES implements Serializable {

    public String ID, URL, TITTLE, SHORT_DESC;

    public RELATED_FILES(){}

    public RELATED_FILES(String ID, String URL, String TITTLE, String SHORT_DESC) {
        this.ID = ID;
        this.URL = URL;
        this.TITTLE = TITTLE;
        this.SHORT_DESC = SHORT_DESC;
    }
}
