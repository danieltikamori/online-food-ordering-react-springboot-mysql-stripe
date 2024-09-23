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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD) // Apply this annotation to methods
@Retention(RetentionPolicy.RUNTIME) // Make it available at runtime
public @interface DataRetentionPolicyDays {

//retentionDaysBeforeAnonymization: Defines the number of days before anonymization is performed (defaults to 0 if not specified).
    int retentionDaysBeforeAnonymization() default 91; // Default anonymization period

//retentionDaysBeforeDatabaseRemotion: Defines the number of days before data is deleted from the database (defaults to 0 if not specified).
    int retentionDaysBeforeDatabaseRemotion() default 181; // Default database removal period
}