package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class EventFeedback implements Serializable {

    public EventFeedback(){}

    public String FACULTY,FACULTY_RATING,ID,MESSAGE,SUBJECT,SEMESTER;

    public EventFeedback(String FACULTY, String FACULTY_RATING, String ID, String MESSAGE, String SUBJECT, String SEMESTER) {
        this.FACULTY = FACULTY;
        this.FACULTY_RATING = FACULTY_RATING;
        this.ID = ID;
        this.MESSAGE = MESSAGE;
        this.SUBJECT = SUBJECT;
        this.SEMESTER = SEMESTER;
    }
}
