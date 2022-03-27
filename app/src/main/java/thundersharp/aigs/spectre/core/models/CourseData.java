package thundersharp.aigs.spectre.core.models;

public class CourseData {
    public CourseData(){}

    public String ID;
    public String TOPIC_NAME;
    public String TOPIC_DESCRIPTION;
    public String VIDEO_ID;

    public CourseData(String ID, String TOPIC_NAME, String TOPIC_DESCRIPTION, String VIDEO_ID) {
        this.ID = ID;
        this.TOPIC_NAME = TOPIC_NAME;
        this.TOPIC_DESCRIPTION = TOPIC_DESCRIPTION;
        this.VIDEO_ID = VIDEO_ID;
    }
}
