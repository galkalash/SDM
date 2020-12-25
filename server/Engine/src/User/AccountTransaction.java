package User;

import java.util.Date;

public class AccountTransaction {
     private final TransactionType transactionType;
     private final Date date;
     private final float amount;
     private final float BalanceBeforeTransaction;
    private final float BalanceAfterTransaction;

    public AccountTransaction(TransactionType transactionType, Date date, float amount,
                              float balanceBeforeTransaction, float balanceAfterTransaction) {
        this.transactionType = transactionType;
        this.date = date;
        this.amount = amount;
        BalanceBeforeTransaction = (float)Math.round(balanceBeforeTransaction*100)/100;
        BalanceAfterTransaction = (float)Math.round(balanceAfterTransaction*100)/100;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Date getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public float getBalanceBeforeTransaction() {
        return (float)Math.round(BalanceBeforeTransaction*100)/100;
    }

    public float getBalanceAfterTransaction() {
        return (float)Math.round(BalanceAfterTransaction*100)/100;
    }
}
