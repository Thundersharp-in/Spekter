package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class ProjectBasicInfo implements Serializable {

    public ProjectBasicInfo(){}

    public String ID,NAME,SHORT_DESCRIPTION,TYPE;

    public ProjectBasicInfo(String ID, String NAME, String SHORT_DESCRIPTION, String TYPE) {
        this.ID = ID;
        this.NAME = NAME;
        this.SHORT_DESCRIPTION = SHORT_DESCRIPTION;
        this.TYPE = TYPE;
    }
}
