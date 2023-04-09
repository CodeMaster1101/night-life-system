package com.mile.nokenzivot.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class PartyEventDTO {
  private String name;
  private String description;
  private byte[] thumbnail;
  private String clubName;
}

