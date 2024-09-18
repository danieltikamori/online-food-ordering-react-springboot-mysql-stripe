/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.model;

/**
 * @author Daniel Itiro Tikamori
 * Enum Plural naming canvention: UpperCamelCase, e.g., EntityNameRole (UserRole)
 * Singular naming canvention(constant with single value): UPPER_SNAKE_CASE e.g., ROLE_CUSTOMER
 */

public enum UserRole {

    ROLE_CUSTOMER,
    ROLE_RESTAURANT_OWNER,
    ROLE_RESTAURANT_STAFF,
    ROLE_DELIVERY_PERSON,

    ROLE_ROOT,
    ROLE_ADMIN

}
