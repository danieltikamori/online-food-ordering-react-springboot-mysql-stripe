package me.amlu.service;

import me.amlu.model.*;
import me.amlu.repository.*;
import me.amlu.request.OrderRequest;
import me.amlu.service.Exceptions.OrderStatusNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    private final RestaurantService restaurantService;

    private final CartService cartService;

    public OrderServiceImp(OrderRepository orderRepository, OrderItemRepository orderItemRepository, AddressRepository addressRepository, UserRepository userRepository, RestaurantService restaurantService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.restaurantService = restaurantService;
        this.cartService = cartService;
    }

    @Override
    public Order createOrder(OrderRequest order, User user) throws Exception {

        Address deliveryAddress = order.getDeliveryAddress();
        Address savedAddress = addressRepository.save(deliveryAddress);

        if(!user.getAddresses().contains(savedAddress)) {
            user.getAddresses().add(savedAddress);
            userRepository.save(user);
        }

        Restaurant restaurant = restaurantService.findRestaurantById(order.getRestaurantId());

        Order createdOrder = new Order();
        createdOrder.setCustomer(user);
        createdOrder.setRestaurant(restaurant);
        createdOrder.setCreatedAt(new Date());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.setDeliveryAddress(savedAddress);

        Cart cart = cartService.findCartByCustomerId(user.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(createdOrder);
            orderItem.setFood(cartItem.getFood());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalAmount(cartItem.getTotalAmount());

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        }

        BigDecimal totalAmount = cartService.calculateTotalAmount(cart);

        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(createdOrder);
        restaurant.getOrders().add(savedOrder);

        return createdOrder;



    }

    @Override
    public Order updateOrder(Long orderId, String orderStatus) throws Exception {

        Order order = findOrderById(orderId);
        if (orderStatus.equals("OUT_FOR_DELIVERY")
                || (orderStatus.equals("DELIVERED"))
                || (orderStatus.equals("COMPLETED"))
                || (orderStatus.equals("PENDING"))
                || (orderStatus.equals("CANCELLED"))) {


            order.setOrderStatus(orderStatus);
            return orderRepository.save(order);
        }
        throw new OrderStatusNotFoundException("Please provide a valid order status.");
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {

        Order order = findOrderById(orderId);

        if(order.getOrderStatus().equals("PENDING")) {
            order.setOrderStatus("CANCELLED");
            orderRepository.save(order);
        }
        else {
            throw new OrderStatusNotFoundException("Order status not found.");
        }

    }

    @Override
    public List<Order> getCustomerOrders(Long customerId) throws Exception {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Order> getRestaurantOrders(Long restaurantId, String orderStatus) throws Exception {
        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);

        if(orderStatus != null) {
            return orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
        }
        throw new OrderStatusNotFoundException("Order status not found.");
    }

    @Override
    public Order findOrderById(Long orderId) throws Exception {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new OrderNotFoundException("Order not found.");
        }
        return orderOptional.get();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));
        order.setDeletedAt(Instant.now());
        orderRepository.save(order);
    }

}
