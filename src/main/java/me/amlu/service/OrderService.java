/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

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
