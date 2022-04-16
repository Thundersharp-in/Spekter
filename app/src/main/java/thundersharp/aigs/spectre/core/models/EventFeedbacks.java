package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class EventFeedbacks implements Serializable {

    public EventFeedbacks(){}

    public String FACULTY,EVENT_RATING,ID,MESSAGE,DATE,SEMESTER;

    public EventFeedbacks(String FACULTY, String EVENT_RATING, String ID, String MESSAGE, String DATE, String SEMESTER) {
        this.FACULTY = FACULTY;
        this.EVENT_RATING = EVENT_RATING;
        this.ID = ID;
        this.MESSAGE = MESSAGE;
        this.DATE = DATE;
        this.SEMESTER = SEMESTER;
    }
}
