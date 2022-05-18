package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class SubscriptionDetails implements Serializable {
    public SubscriptionDetails(){}
    public String SUB_ID,SUB_STATUS,SUB_MODE;

    public SubscriptionDetails(String SUB_ID, String SUB_STATUS, String SUB_MODE) {
        this.SUB_ID = SUB_ID;
        this.SUB_STATUS = SUB_STATUS;
        this.SUB_MODE = SUB_MODE;
    }
}
