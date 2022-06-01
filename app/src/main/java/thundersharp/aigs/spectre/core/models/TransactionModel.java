package thundersharp.aigs.spectre.core.models;

public class TransactionModel {

    public TransactionModel(){}

    public String TRANSACTION_FOR;
    public String TRANSACTION_ID;
    public String TRANSACTION_STATUS;
    public String ID;

    public TransactionModel(String TRANSACTION_FOR, String TRANSACTION_ID, String TRANSACTION_STATUS, String ID) {
        this.TRANSACTION_FOR = TRANSACTION_FOR;
        this.TRANSACTION_ID = TRANSACTION_ID;
        this.TRANSACTION_STATUS = TRANSACTION_STATUS;
        this.ID = ID;
    }
}
