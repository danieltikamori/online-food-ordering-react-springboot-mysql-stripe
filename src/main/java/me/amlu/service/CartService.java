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

import me.amlu.model.Cart;
import me.amlu.model.CartItem;
import me.amlu.request.AddCartItemRequest;

import java.math.BigDecimal;

public interface CartService {

    CartItem addItemToCart(AddCartItemRequest request, String token) throws Exception;

    CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;

    Cart removeCartItem(Long cartItemId, String token) throws Exception;

    BigDecimal calculateTotalAmount(Cart cart) throws Exception;

    Cart findCartById(Long cartId) throws Exception;

    Cart findCartByCustomerId(Long customerId) throws Exception;
    

    Cart clearCart(Long customerId) throws Exception;



}
