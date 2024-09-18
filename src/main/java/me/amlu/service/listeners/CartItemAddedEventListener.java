/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.service.listeners;

import me.amlu.events.CartItemAddedEvent;
import me.amlu.model.CartItem;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CartItemAddedEventListener {
   @EventListener
   public void handleCartItemAdded(CartItemAddedEvent event) {

       // Extract the cart item from the event
       CartItem cartItem = event.cartItem();

       // TODO: Logic to handle the event (e.g., update recommendations, send notifications, update Store inventory)

   }
}