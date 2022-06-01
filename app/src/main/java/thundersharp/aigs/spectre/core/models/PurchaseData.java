package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class PurchaseData implements Serializable {

    public PurchaseData(){}

    public String COURSE_ID, AMOUNT, NAME;
    public String COURSE_TYPE, RESTRICT;
    public Payment_Data PAYMENT;

    public PurchaseData(String COURSE_ID, String AMOUNT, String NAME, String COURSE_TYPE, String RESTRICT, Payment_Data PAYMENT) {
        this.COURSE_ID = COURSE_ID;
        this.AMOUNT = AMOUNT;
        this.NAME = NAME;
        this.COURSE_TYPE = COURSE_TYPE;
        this.PAYMENT = PAYMENT;
        this.RESTRICT = RESTRICT;
    }
}
