package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class StudentsDetails implements Serializable {

    public StudentsDetails(){}

    public String EMAIL,ID,NAME,PHONE;

    public StudentsDetails(String EMAIL, String ID, String NAME, String PHONE) {
        this.EMAIL = EMAIL;
        this.ID = ID;
        this.NAME = NAME;
        this.PHONE = PHONE;
    }

    @Override
    public String toString() {
        return "StudentsDetails{" +
                "EMAIL='" + EMAIL + '\'' +
                ", ID='" + ID + '\'' +
                ", NAME='" + NAME + '\'' +
                ", PHONE='" + PHONE + '\'' +
                '}';
    }
}
