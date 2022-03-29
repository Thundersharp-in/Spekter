package thundersharp.aigs.newsletter.core.model;

import java.io.Serializable;

public class NewsLetters implements Serializable {

    public String AUTHOR;
    public String TITLE;
    public String DESCRIPTION;
    public String URL;
    public String URL_TO_IMAGE;
    public String PUBLISHED_AT;
    public String SOURCE_NAME;
    public String SHORT_DESCRIPTION;

    public NewsLetters(){}

    public NewsLetters(String AUTHOR, String TITLE, String DESCRIPTION, String URL, String URL_TO_IMAGE, String PUBLISHED_AT, String SOURCE_NAME, String SHORT_DESCRIPTION) {
        this.AUTHOR = AUTHOR;
        this.TITLE = TITLE;
        this.DESCRIPTION = DESCRIPTION;
        this.URL = URL;
        this.URL_TO_IMAGE = URL_TO_IMAGE;
        this.PUBLISHED_AT = PUBLISHED_AT;
        this.SOURCE_NAME = SOURCE_NAME;
        this.SHORT_DESCRIPTION = SHORT_DESCRIPTION;
    }

    @Override
    public String toString() {
        return "NewsLetters{" +
                "AUTHOR='" + AUTHOR + '\'' +
                ", TITLE='" + TITLE + '\'' +
                ", DESCRIPTION='" + DESCRIPTION + '\'' +
                ", URL='" + URL + '\'' +
                ", URL_TO_IMAGE='" + URL_TO_IMAGE + '\'' +
                ", PUBLISHED_AT='" + PUBLISHED_AT + '\'' +
                ", SOURCE_NAME='" + SOURCE_NAME + '\'' +
                ", SHORT_DESCRIPTION='" + SHORT_DESCRIPTION + '\'' +
                '}';
    }
}
