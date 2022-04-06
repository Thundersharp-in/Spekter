package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class Participants implements Serializable {

    public Participants(){}

    public String EMAIL,ID,NAME,PROFILE_PIC,ROLE,SEM_YEAR;

    public Participants(String EMAIL, String ID, String NAME, String PROFILE_PIC, String ROLE, String SEM_YEAR) {
        this.EMAIL = EMAIL;
        this.ID = ID;
        this.NAME = NAME;
        this.PROFILE_PIC = PROFILE_PIC;
        this.ROLE = ROLE;
        this.SEM_YEAR = SEM_YEAR;
    }
}
