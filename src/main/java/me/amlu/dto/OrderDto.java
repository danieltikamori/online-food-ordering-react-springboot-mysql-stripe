/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.dto;

import jakarta.validation.constraints.NotEmpty;
import me.amlu.model.OrderStatus;
import me.amlu.model.User;

import java.time.Instant;

public class OrderDto {

    @NotEmpty
    private Long order_id;

    @NotEmpty
    private Long restaurant_id;

    @NotEmpty
    private UserToOrderDto customer;

    @NotEmpty
    private AddressDto customerAddress;

//    @NotEmpty
//    private PaymentMethod paymentMethod;
//
//    @NotEmpty
//    private PaymentStatus paymentStatus;

    @NotEmpty
    private OrderStatus orderStatus;

    @NotEmpty
    private String idempotencyKey;

    @NotEmpty
    private Instant updatedAt;

    @NotEmpty
    private UserToOrderDto updatedBy;

}
