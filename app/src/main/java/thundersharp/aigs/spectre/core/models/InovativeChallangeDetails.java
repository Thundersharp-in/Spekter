package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class InovativeChallangeDetails implements Serializable {

    public InovativeChallangeDetails(){}

    public String ID,LOCATION,HIGHLIGHTS,DESCRIPTION,RULES_MORE, SUBMISSION_DATE,SUBMISSION_LINK,TITTLE,PRIZE_AMT,COVER;

    public InovativeChallangeDetails(String ID, String LOCATION, String HIGHLIGHTS, String DESCRIPTION, String RULES_MORE, String SUBMISSION_DATE, String SUBMISSION_LINK, String TITTLE, String PRIZE_AMT, String COVER) {
        this.ID = ID;
        this.LOCATION = LOCATION;
        this.HIGHLIGHTS = HIGHLIGHTS;
        this.DESCRIPTION = DESCRIPTION;
        this.RULES_MORE = RULES_MORE;
        this.SUBMISSION_DATE = SUBMISSION_DATE;
        this.SUBMISSION_LINK = SUBMISSION_LINK;
        this.TITTLE = TITTLE;
        this.PRIZE_AMT = PRIZE_AMT;
        this.COVER = COVER;
    }
}
