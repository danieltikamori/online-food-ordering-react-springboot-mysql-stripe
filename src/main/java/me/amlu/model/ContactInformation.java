/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
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
