package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class WorkshopFiles implements Serializable {

    public String ID, URL, TITLE, SHORT_DESCRIPTION;

    public WorkshopFiles (){}

    public WorkshopFiles(String ID, String URL, String TITLE, String SHORT_DESCRIPTION) {
        this.ID = ID;
        this.URL = URL;
        this.TITLE = TITLE;
        this.SHORT_DESCRIPTION = SHORT_DESCRIPTION;
    }
}
