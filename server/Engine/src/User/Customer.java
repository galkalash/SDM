package User;

import Engine.MapLocation;
import Engine.Order;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User{
    private float AverageItemCosts;
    private float AverageDeliveryCost;


    public Customer(String name) {
        super(name);
        AverageItemCosts = 0;
        AverageDeliveryCost = 0;
    }

    public String getName() {
        return name;
    }


    public int getId() {
        return id;
    }

    public float getAverageItemCosts() {
        return AverageItemCosts;
    }

    public float getAverageDeliveryCost() {
        return AverageDeliveryCost;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order newOrder, float deliveryCost, float averageItems){
        orders.add(newOrder);
        AverageItemCosts *= (orders.size() - 1);
        AverageItemCosts += averageItems;
        AverageItemCosts /= orders.size();

        AverageDeliveryCost *=  (orders.size() - 1);
        AverageDeliveryCost += deliveryCost;
        AverageDeliveryCost /= orders.size();
    }


}
