package thundersharp.aigs.spectre.core.models;

public class ServiceTopicModel {

    public String COARSE_BY,
            COARSE_DESCRIPTION,
            COARSE_ID,
            COURSE_ICON,
            PRICE,
            COURSE_NAME;

    public ServiceTopicModel() {
    }

    public ServiceTopicModel(String COARSE_BY, String COARSE_DESCRIPTION, String COARSE_ID, String COURSE_ICON, String PRICE, String COURSE_NAME) {
        this.COARSE_BY = COARSE_BY;
        this.COARSE_DESCRIPTION = COARSE_DESCRIPTION;
        this.COARSE_ID = COARSE_ID;
        this.COURSE_ICON = COURSE_ICON;
        this.PRICE = PRICE;
        this.COURSE_NAME = COURSE_NAME;
    }
}
