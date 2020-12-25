package Dtos;

import User.AccountTransaction;
import User.TransactionType;

import java.util.Date;

public class AccountTransactionDto {
    private final TransactionType transactionType;
    private final Date date;
    private final float amount;
    private final float balanceBeforeTransaction;
    private final float balanceAfterTransaction;

    public AccountTransactionDto(AccountTransaction transaction) {
        this.transactionType = transaction.getTransactionType();
        this.date = transaction.getDate();
        this.amount = transaction.getAmount();
        this.balanceBeforeTransaction = transaction.getBalanceBeforeTransaction();
        this.balanceAfterTransaction = transaction.getBalanceAfterTransaction();
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Date getDate() {
        return date;
    }

    public float getAmount() {
        return (float)Math.round(amount*100)/100;
    }

    public float getBalanceBeforeTransaction() {
        return balanceBeforeTransaction;
    }

    public float getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }
}
