package com.mile.nokenzivot.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TypedCoordinates {
  private String type;
  private double latitude;
  private double longitude;
}
