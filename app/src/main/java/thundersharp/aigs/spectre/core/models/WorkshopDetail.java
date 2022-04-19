package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class WorkshopDetail implements Serializable {

    public WorkshopDetail(){}

    public String ID,LOCATION,HIGHLIGHTS,DETAILS,MORE,PRICE,REG_LINK;

    public WorkshopDetail(String ID, String LOCATION, String HIGHLIGHTS, String DETAILS, String MORE, String PRICE, String REG_LINK) {
        this.ID = ID;
        this.LOCATION = LOCATION;
        this.HIGHLIGHTS = HIGHLIGHTS;
        this.DETAILS = DETAILS;
        this.MORE = MORE;
        this.PRICE = PRICE;
        this.REG_LINK = REG_LINK;
    }
}
