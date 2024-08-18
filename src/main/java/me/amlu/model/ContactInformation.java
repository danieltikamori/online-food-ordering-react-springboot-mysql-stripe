package me.amlu.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {

    @Column(length = 200)
    private String email;

    @Column(length = 200)
    private String mobile;

    @Column(length = 200)
    private String website;

    @Column(length = 200)
    private String twitter;

    @Column(length = 200)
    private String instagram;
}
