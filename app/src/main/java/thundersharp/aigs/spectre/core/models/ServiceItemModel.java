package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class ServiceItemModel implements Serializable {

    public String COARSE_BY,
            COARSE_DESCRIPTION,
            COARSE_DURATION,
            COARSE_ID,
            COURSE_ICON,
            COURSE_NAME,HASHTAGS,
            LANGUAGE,
            LEVEL,
            MUST_KNOW_TOPICS,
            PRICE,
            WILL_LEARN;

    public ServiceItemModel() {
    }

    public ServiceItemModel(String COARSE_BY, String COARSE_DESCRIPTION, String COARSE_DURATION, String COARSE_ID, String COURSE_ICON, String COURSE_NAME, String HASHTAGS, String LANGUAGE, String LEVEL, String MUST_KNOW_TOPICS, String PRICE, String WILL_LEARN) {
        this.COARSE_BY = COARSE_BY;
        this.COARSE_DESCRIPTION = COARSE_DESCRIPTION;
        this.COARSE_DURATION = COARSE_DURATION;
        this.COARSE_ID = COARSE_ID;
        this.COURSE_ICON = COURSE_ICON;
        this.COURSE_NAME = COURSE_NAME;
        this.HASHTAGS = HASHTAGS;
        this.LANGUAGE = LANGUAGE;
        this.LEVEL = LEVEL;
        this.MUST_KNOW_TOPICS = MUST_KNOW_TOPICS;
        this.PRICE = PRICE;
        this.WILL_LEARN = WILL_LEARN;
    }
}
