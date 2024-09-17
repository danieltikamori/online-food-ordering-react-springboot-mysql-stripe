/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.dto;

import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import me.amlu.model.ContactInformation;

@Data
public class ContactInformationDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private Phonenumber.PhoneNumber phoneNumber;

    public static ContactInformationDto fromEntity(@Valid @NotEmpty ContactInformation contactInformation) {
        ContactInformationDto dto = new ContactInformationDto();
        dto.setEmail(contactInformation.getEmail());
        dto.setPhoneNumber(contactInformation.getPhoneNumber());
        return dto;
    }
}
