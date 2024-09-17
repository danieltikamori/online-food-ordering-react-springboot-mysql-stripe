/*
 * Copyright (c) 2024 Daniel Itiro Tikamori. All rights reserved.
 */

package me.amlu.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PhoneNumber {

  @NotEmpty
  private String value;

  @NotEmpty
  private String locale;
}