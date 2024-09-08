package me.amlu.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {

    @Column(length = 255)
    @Size(max = 255)
    private String email;

    @Column(length = 255)
    @Size(max = 255)
    private String mobile;

    @Column(length = 255)
    @Size(max = 255)
    private String website;

    @Column(length = 255)
    @Size(max = 255)
    private String twitter;

    @Column(length = 255)
    @Size(max = 255)
    private String instagram;
}
