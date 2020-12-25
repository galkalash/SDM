package Dtos;

import Engine.ItemInOrder;
import Engine.Order;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@XmlRootElement(name = "Orders")
@XmlSeeAlso({DynamicOrderDto.class,StaticOrderDto.class})
public abstract class OrderDto {
    @XmlElement(name = "serialNumber")
    protected int serialNumber;
    @XmlElement(name = "date")
    protected Date orderDate;
    protected String customerName;
    protected String zoneName;
    protected MapLocationDto customerLocation;

    public int getSerialNumber() {
        return serialNumber;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setCustomer(String customerName) {
        this.customerName = customerName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public MapLocationDto getCustomerLocation() {
        return customerLocation;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}