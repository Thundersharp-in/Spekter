package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

import thundersharp.aigs.spectre.ui.fragments.knowUs.Commitee;

public class Committee implements Serializable {

    public Committee() {

    }

    public String DESC,EMAIL,NAME,PIC;

    public Committee(String DESC, String EMAIL, String NAME, String PIC) {
        this.DESC = DESC;
        this.EMAIL = EMAIL;
        this.NAME = NAME;
        this.PIC = PIC;
    }
}
