/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.service;

import jakarta.validation.Valid;
import lombok.NonNull;
import me.amlu.dto.CartItemDto;
import me.amlu.dto.CartToOrderDto;
import me.amlu.model.*;
import me.amlu.repository.*;
import me.amlu.request.OrderRequest;
import me.amlu.service.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static me.amlu.common.SecurityUtil.getAuthenticatedUser;

@Validated
@Service
public class OrderServiceImp implements OrderService {

    public static final Logger logger = LogManager.getLogger(OrderServiceImp.class);

    private final UserService userService;

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    private final RestaurantService restaurantService;

    private final CartService cartService;

    private final FoodRepository foodRepository;

    private final IngredientsItemsRepository ingredientsItemsRepository;


    public OrderServiceImp(UserService userService, OrderRepository orderRepository, OrderItemRepository orderItemRepository, AddressRepository addressRepository, UserRepository userRepository, RestaurantService restaurantService, CartService cartService, FoodRepository foodRepository, IngredientsItemsRepository ingredientsItemsRepository) {
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.restaurantService = restaurantService;
        this.cartService = cartService;
        this.foodRepository = foodRepository;
        this.ingredientsItemsRepository = ingredientsItemsRepository;
    }

    @Override
    @Transactional // Ensure atomicity
    public Order createOrder(@Valid OrderRequest orderRequest, @Valid User user) throws Exception {

        // 0. Idempotency Check (using idempotency key)
        String idempotencyKey = orderRequest.getIdempotencyKey();
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            idempotencyKey = UUID.randomUUID().toString(); // Generate if not provided
        }

        Optional<Order> existingOrder = orderRepository.findByIdempotencyKey(idempotencyKey);
        if (existingOrder.isPresent()) {
            return existingOrder.get(); // Return the existing order
        }

        // 2. Fetch Cart Data (with potential for Optimistic Locking)
        Optional<CartToOrderDto> cartDtoOptional = cartService.findCartWithItemsAndFoodDetails(user.getUser_id());
        if (cartDtoOptional.isEmpty()) {
            throw new EmptyCartException("Cannot create an order from an empty cart.");
        }
        CartToOrderDto cartDto = cartDtoOptional.get();

        // 3. Handle Address
        Address deliveryAddress = orderRequest.getDeliveryAddress();
        Optional<Address> existingAddress = addressRepository.findMatchingAddress(deliveryAddress);
        Address savedAddress = existingAddress.orElseGet(() -> addressRepository.save(deliveryAddress));
        if (!user.getAddresses().contains(savedAddress)) {
            user.getAddresses().add(savedAddress);
            userRepository.save(user);
        }

        // 4. Create and Save Order
        Order createdOrder = new Order();
        createdOrder.setIdempotencyKey(idempotencyKey);

        try {
            Restaurant restaurant = restaurantService.findRestaurantById(orderRequest.getRestaurantId());

            createdOrder.setCustomer(user);
            createdOrder.setRestaurant(restaurant);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.setDeliveryAddress(savedAddress);
            createdOrder.setCreatedAt(Instant.now());
            createdOrder.setUpdatedAt(Instant.now());
            createdOrder.setCreatedBy(user);
            createdOrder.setUpdatedBy(user);

            Order savedOrder = orderRepository.save(createdOrder); // Save Order first to get ID

            List<OrderItem> orderItems = new ArrayList<>();
            BigDecimal orderTotal = BigDecimal.ZERO;

            for (CartItemDto cartItem : cartDto.getCartItems()) {

                // Get Food with Optimistic Locking (using @Version)
                Food food = foodRepository.findById(cartItem.getFoodId())
                        .orElseThrow(() -> new FoodNotFoundException("Food not found"));

                // 4. Recalculate Price (to handle potential price changes)
                BigDecimal currentFoodPrice = food.getPrice();
                BigDecimal itemTotal = currentFoodPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                OrderItem orderItem = new OrderItem();

                orderItem.setOrder(createdOrder);
                orderItem.setFood(cartItem.getFood());
                Set<IngredientsItems> ingredients = ingredientsItemsRepository.findByRestaurantId(cartItem.getFoodId());
                orderItem.setIngredients(ingredients);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setCreatedAt(Instant.now());
                orderItem.setUpdatedAt(Instant.now());
                orderItem.setCreatedBy(user);
                orderItem.setUpdatedBy(user);

                orderItems.add(orderItem);
                orderTotal = orderTotal.add(itemTotal); // Update orderTotal
            }

            orderItemRepository.saveAll(orderItems); // Now save the OrderItems
            savedOrder.setTotalAmount(orderTotal); // Set the calculated total amount
            orderRepository.save(savedOrder); // Save again to update total amount

            restaurant.getOrders().add(savedOrder);

            return savedOrder;

        } catch (RestaurantNotFoundException e) {
            throw new RestaurantNotFoundException("Restaurant not found: " + e.getMessage());
        } catch (DataIntegrityViolationException e) {
            throw new OrderAlreadyExistsException("Order already exists: " + e.getMessage(), e);
        }

    }

    @Override
    @Transactional
    public Order updateOrder(@Valid @NonNull Long orderId, @NonNull String orderStatus)
            throws OrderNotFoundException, OrderStatusInvalidException, IllegalStateException {

        // 1. Validate Input (OrderStatus already validated in getRestaurantOrders)
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        // 2. Use Enum for Order Status
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(orderStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OrderStatusInvalidException("Invalid order status: " + orderStatus);
        }

        // 3. Encapsulate Order Status Transition Rules
        if (!order.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid order status transition: " + order.getOrderStatus() + " to " + newStatus);
        }

        // 4. Update Order and Audit Information
        order.setOrderStatus(newStatus);
        order.setUpdatedAt(Instant.now());
        order.setUpdatedBy(getAuthenticatedUser());

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void cancelOrder(@Valid @NonNull Long orderId) throws OrderNotFoundException, OrderCancellationException {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found."));

        // Delegate cancellation logic to the Order entity
        if (!order.cancel()) {
            throw new OrderCancellationException("Order cannot be cancelled in its current state: " + order.getOrderStatus());
        }

        orderRepository.save(order); // Save only if the cancellation logic succeeds
    }

    @Override
    public List<Order> getCustomerOrders(@Valid @NonNull Long customerId) throws CustomerNotFoundException {

        // 1. Validate Customer Existence and Fetch Customer Object
        User customer = (User) userService.findUserById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));

        // 2. Fetch Orders (Efficiently)
        return orderRepository.findByCustomer(customer);
    }

    @Override
    public List<Order> getRestaurantOrders(@Valid Long restaurantId, String orderStatus)
            throws Exception {

        // 1. Validate Input
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        if (orderStatus == null || orderStatus.trim().isEmpty()) {
            throw new OrderStatusInvalidException("Order status cannot be empty.");
        }

        // 2. Use Enum for Order Status
        OrderStatus status;
        try {
            status = OrderStatus.valueOf(orderStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OrderStatusInvalidException("Invalid order status: " + orderStatus);
        }

        // 3. Optimize Database Query
        return orderRepository.findByRestaurantIdAndAndOrderStatus(
                restaurant.getRestaurant_id(), status
        );
    }

    @Override
    public Order findOrderById(@Valid @NonNull Long orderId) throws OrderNotFoundException {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));
    }

    @Override
    @Transactional // Ensure atomicity, especially if there are cascading deletes
    public void deleteOrder(@Valid @NonNull Long orderId) throws OrderNotFoundException {

        // 1. Fetch Order (Consider using getOne/getReference for potential performance gain)
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        // 2. Additional Business Logic
        //    - Check if the order can be deleted (e.g., only pending orders).
        OrderStatus status = order.getOrderStatus();
        if (!status.equals(OrderStatus.PENDING)) {
            throw new OrderDeletionException("Order cannot be deleted in its current state: " + status);
        }
        //    - TODO:Perform any necessary side effects (e.g., refunds, notifications).


        // 3. Choose a Deletion Strategy:
        //    3.1. Soft Delete (if soft delete is required):
        order.setDeletedAt(Instant.now());
        order.setDeletedBy(getAuthenticatedUser());
        orderRepository.save(order);

        //    3.2. Hard Delete (if soft delete is not required):
        // orderRepository.delete(order);

        //    3.3. Custom Repository Method (for optimized bulk deletes):
        // orderRepository.deleteById(orderId);
    }

}
