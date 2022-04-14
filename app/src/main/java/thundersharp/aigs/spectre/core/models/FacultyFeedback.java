package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class FacultyFeedback implements Serializable {

    public FacultyFeedback(){}

    public String FACULTY,FACULTY_RATING,ID,MESSAGE,SUBJECT;

    public FacultyFeedback(String FACULTY, String FACULTY_RATING, String ID, String MESSAGE, String SUBJECT) {
        this.FACULTY = FACULTY;
        this.FACULTY_RATING = FACULTY_RATING;
        this.ID = ID;
        this.MESSAGE = MESSAGE;
        this.SUBJECT = SUBJECT;
    }
}
