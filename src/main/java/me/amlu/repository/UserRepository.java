/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 *
 * This software is proprietary, not intended for public distribution, open source, or commercial use. All rights are reserved. No part of this software may be reproduced, distributed, or transmitted in any form or by any means, electronic or mechanical, including photocopying, recording, or by any information storage or retrieval system, without the prior written permission of the copyright holder.
 *
 * Permission to use, copy, modify, and distribute this software is strictly prohibited without prior written authorization from the copyright holder.
 *
 * Please contact the copyright holder at fuiwzchps@mozmail.com for any inquiries or requests for authorization to use the software.
 */

package me.amlu.repository;

import me.amlu.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :username AND u.deletedAt IS NULL")
    User findByEmail(@Param(value = "username") String username);

    @Query("SELECT u FROM User u WHERE u.fullName = :fullName AND u.deletedAt IS NULL")
    User findByFullName(@Param(value = "fullName") String fullName);

    @NonNull
    @Query("SELECT u FROM User u WHERE u.category_id = :userId AND u.deletedAt IS NULL")
    Optional<User> findById(@NonNull @Param(value = "userId") Long userId);


    void deleteAllByDeletedAtBefore(Instant threshold);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deletedAt IS NULL")
    boolean existsByEmail(@Param(value = "email") String email);

    @Query("SELECT u FROM User u WHERE u.deletedAt < :anonymizationThreshold")
    List<User> findByDeletedAtBefore(Instant anonymizationThreshold);


    void permanentlyDeleteUser(Long userId);
}
