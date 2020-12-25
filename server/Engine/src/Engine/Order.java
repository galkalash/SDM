package Engine;

import User.Customer;

import java.util.*;

public abstract class Order {
    protected static int baseSerialNumber = 100;
    protected int serialNumber;
    protected Date orderDate;
    protected String customerName;
    protected String zoneName;
    protected MapLocation customerLocation;


    public int getSerialNumber() {
        return serialNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public static int getBaseSerialNumber() {
        return baseSerialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public static void setBaseSerialNumber(int baseSerialNumber) {
        Order.baseSerialNumber = baseSerialNumber;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public MapLocation getCustomerLocation() {
        return customerLocation;
    }
}
