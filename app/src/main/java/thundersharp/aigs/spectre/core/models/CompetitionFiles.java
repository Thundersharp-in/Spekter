package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class CompetitionFiles implements Serializable {

    public String ID, URL, TITLE, SHORT_DESCRIPTION;

    public CompetitionFiles(){}

    public CompetitionFiles(String ID, String URL, String TITLE, String SHORT_DESCRIPTION) {
        this.ID = ID;
        this.URL = URL;
        this.TITLE = TITLE;
        this.SHORT_DESCRIPTION = SHORT_DESCRIPTION;
    }
}
