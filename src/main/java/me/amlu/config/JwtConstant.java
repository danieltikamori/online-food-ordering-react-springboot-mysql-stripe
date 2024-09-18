/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.config;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtConstant {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int KEY_LENGTH = 64;

    public static final String JWT_SECRET = generateSecretKey();
    public static final String JWT_HEADER = "Authorization";

    private JwtConstant() {
    }

    private static String generateSecretKey() {
        byte[] randomBytes = new byte[KEY_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }
}