package me.amlu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AnonymizedData {
    // Define the properties and methods of the class

    private String name;
    private String email;
    private String mobile;
    private String website;
    private String twitter;
    private String instagram;

}