package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class Payment_Data implements Serializable {
    public Payment_Data(){}
    public String PAYMENT_ID,PAYMENT_STATUS,PAYMENT_MODE;

    public Payment_Data(String PAYMENT_ID, String PAYMENT_STATUS, String PAYMENT_MODE) {
        this.PAYMENT_ID = PAYMENT_ID;
        this.PAYMENT_STATUS = PAYMENT_STATUS;
        this.PAYMENT_MODE = PAYMENT_MODE;
    }
}
