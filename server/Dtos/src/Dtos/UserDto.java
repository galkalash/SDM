package Dtos;

import Engine.DynamicOrder;
import Engine.Order;
import Engine.StaticOrder;
import User.*;
import User.UserType;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    protected final int id;
    protected final String name;
    protected float creditBalance;
    protected final List<AccountTransactionDto> userTransactions;
    protected List<OrderDto> orders;

    public UserDto(User user) {
        id = user.getId();
        name = user.getName();
        creditBalance = user.getCreditBalance();
        userTransactions = new ArrayList<>();
        orders = new ArrayList<>();
        for(AccountTransaction transaction : user.getUserTransactions()){
            userTransactions.add(new AccountTransactionDto(transaction));
        }

        for(Order order : user.getOrders()){
            if(order instanceof StaticOrder){
                orders.add(new StaticOrderDto((StaticOrder) order));
            }
            else{
                orders.add(new DynamicOrderDto((DynamicOrder) order));
            }
        }
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

    public List<AccountTransactionDto> getUserTransactions() {
        return userTransactions;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }
}
