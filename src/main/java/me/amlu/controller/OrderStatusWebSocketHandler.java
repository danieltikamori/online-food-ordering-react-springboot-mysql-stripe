/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderStatusWebSocketHandler extends TextWebSocketHandler {

//    TODO: Finish implementation

    private final ConcurrentHashMap<Long, List<WebSocketSession>> orderSessions = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON

    public void sendOrderStatusUpdate(Long orderId, String newStatus) throws IOException {
        List<WebSocketSession> sessions = orderSessions.get(orderId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    // Create a message object (or use a simple string)
                    Map<String, Object> message = new HashMap<>();
                    message.put("orderId", orderId);
                    message.put("newStatus", newStatus);
                    // Send the message as JSON
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
                }
            }
        }
    }

    private Long getOrderIdFromSession(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery(); // Get the entire query string
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] parts = pair.split("=", 2);
                if (parts.length == 2 && "orderId".equals(parts[0])) {
                    try {
                        return Long.parseLong(parts[1]);
                    } catch (NumberFormatException e) {
                        // Handle invalid orderId format
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // ... (Methods for handling WebSocket connections/disconnections)

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        // 1. Get the orderId from the session (e.g., from connection parameters)
        Long orderId = getOrderIdFromSession(session); // You'll need to implement this
        // 2. Add the session to the map
        orderSessions.computeIfAbsent(orderId, k -> new ArrayList<>()).add(session);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        // 1. Get the orderId
        Long orderId = getOrderIdFromSession(session);
        // 2. Remove the session from the map
        orderSessions.computeIfPresent(orderId, (k, sessions) -> {
            sessions.remove(session);
            return sessions.isEmpty() ? null : sessions; // Remove empty lists
        });
    }

}