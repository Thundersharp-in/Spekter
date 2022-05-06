package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class PassData implements Serializable {

    public PassData(){}

    public String SLOT,NAME,PHONE,ID,EMAIL;

    public PassData(String SLOT, String NAME, String PHONE, String ID, String EMAIL) {
        this.SLOT = SLOT;
        this.NAME = NAME;
        this.PHONE = PHONE;
        this.ID = ID;
        this.EMAIL = EMAIL;
    }
}
