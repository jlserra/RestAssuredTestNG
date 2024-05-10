package pojo.pojoClasses.e2eSpecbuilder;

import pojo.pojoClasses.e2eSpecbuilder.requestCreateOrder.OrderDetails;

import java.util.List;

public class RequestCreateOrder {

    private List<OrderDetails> orders;

    public List<OrderDetails> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetails> orders) {
        this.orders = orders;
    }

}
