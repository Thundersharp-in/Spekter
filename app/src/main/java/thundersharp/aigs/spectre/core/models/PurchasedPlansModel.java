package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class PurchasedPlansModel implements Serializable {

    public PurchasedPlansModel(){}

    public String NAME;
    public Payment_Data PAYMENT;
    public String PLAN_ID;
    public String VALIDITY;
    public String ICON;
    public String RESTRICT;

    public PurchasedPlansModel(String NAME, Payment_Data PAYMENT, String PLAN_ID, String VALIDITY, String ICON, String RESTRICT) {
        this.NAME = NAME;
        this.PAYMENT = PAYMENT;
        this.PLAN_ID = PLAN_ID;
        this.VALIDITY = VALIDITY;
        this.ICON = ICON;
        this.RESTRICT = RESTRICT;
    }
}
