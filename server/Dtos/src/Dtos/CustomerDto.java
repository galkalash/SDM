package Dtos;

import Engine.*;

import java.util.ArrayList;
import java.util.List;
import User.*;

public class CustomerDto extends UserDto{

    private float AverageItemCosts;
    private float AverageDeliveryCost;

    public CustomerDto(Customer customer){
        super(customer);
        orders = new ArrayList<>();
        this.AverageItemCosts = customer.getAverageItemCosts();
        this.AverageDeliveryCost = customer.getAverageDeliveryCost();
        for(Order order: customer.getOrders()){
            if(order instanceof StaticOrder){
                orders.add(new StaticOrderDto((StaticOrder) order));
            }
            else if(order instanceof DynamicOrder){
                orders.add(new DynamicOrderDto((DynamicOrder) order));
            }
        }
    }

    public float getAverageItemCosts() {
        return AverageItemCosts;
    }

    public float getAverageDeliveryCost() {
        return AverageDeliveryCost;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

}
