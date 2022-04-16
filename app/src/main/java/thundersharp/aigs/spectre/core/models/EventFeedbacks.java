package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class EventFeedbacks implements Serializable {

    public EventFeedbacks(){}

    public String EVENT,EVENT_RATING,ID,MESSAGE,DATE,SEMESTER;

    public EventFeedbacks(String EVENT, String EVENT_RATING, String ID, String MESSAGE, String DATE, String SEMESTER) {
        this.EVENT = EVENT;
        this.EVENT_RATING = EVENT_RATING;
        this.ID = ID;
        this.MESSAGE = MESSAGE;
        this.DATE = DATE;
        this.SEMESTER = SEMESTER;
    }
}
