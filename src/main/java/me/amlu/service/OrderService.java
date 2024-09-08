package me.amlu.service;

import lombok.NonNull;
import me.amlu.model.Order;
import me.amlu.model.User;
import me.amlu.request.OrderRequest;

import java.util.List;

public interface OrderService {

    Order createOrder(@NonNull OrderRequest order, User user) throws Exception;

    Order updateOrder(Long orderId, String orderStatus) throws Exception;

    void cancelOrder(Long orderId) throws Exception;

    List<Order> getCustomerOrders(Long customerId) throws Exception;

    List<Order> getRestaurantOrders(Long restaurantId, String orderStatus) throws Exception;

    Order findOrderById(Long orderId) throws Exception;

    void deleteOrder(Long orderId) throws Exception;

}
