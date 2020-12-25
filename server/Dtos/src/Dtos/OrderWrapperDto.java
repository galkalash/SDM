package Dtos;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="AllOrdersWrapper")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderWrapperDto {
    @XmlElement(name = "Order")
    private List<OrderDto> Orders;

    public List<OrderDto> getOrders() {
        return Orders;
    }


    public void setOrders(List<OrderDto> orders) {
        Orders = orders;
    }
}
