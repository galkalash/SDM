package User;

import Engine.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    protected int id;
    protected String name;
    protected static int idCounter = 100;
    protected float creditBalance;
    protected List<AccountTransaction>  userTransactions;
    protected List<Order> orders;

    public User(String name) {
        this.id = idCounter;
        this.name = name;
        this.creditBalance = 0;
        idCounter++;
        userTransactions = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getCreditBalance() {
        return (float)Math.round(creditBalance*100)/100;
    }

    public List<AccountTransaction> getUserTransactions() {
        return userTransactions;
    }

    public void addMoney(float amount, Date date){
        userTransactions.add(new AccountTransaction(TransactionType.Charging, date, amount,creditBalance,creditBalance + amount));
        creditBalance += amount;
    }

    public void setCreditBalance(float creditBalance) {
        this.creditBalance = creditBalance;
    }

    public void addReceivingPaymentTransaction(Date receivingDate, float amount , float balanceBefore, float balanceAfter){
        userTransactions.add(new AccountTransaction(TransactionType.ReceivingPayment,receivingDate, amount ,balanceBefore,balanceAfter));
    }
    public void addSendingPaymentTransaction(Date sendingDate, float amount , float balanceBefore, float balanceAfter){
        userTransactions.add(new AccountTransaction(TransactionType.SendingPayment,sendingDate, amount ,balanceBefore,balanceAfter));
    }

    public List<Order> getOrders() {
        return orders;
    }
}
