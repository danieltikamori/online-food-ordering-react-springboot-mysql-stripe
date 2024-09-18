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

import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.amlu.config.Phone;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {

    @Email
    @NotEmpty
    @Column(nullable = false)
    @Size(max = 255)
    private String email;

    @Phone
    @NotEmpty
    @Column(nullable = false)
    @Size(max = 31)
    private Phonenumber.PhoneNumber phoneNumber;

    @Phone
    @Size(max = 31)
    private Phonenumber.PhoneNumber mobile;

    @Size(max = 255)
    private String website;

    @Size(max = 255)
    private String twitter;

    @Size(max = 255)
    private String instagram;
}
