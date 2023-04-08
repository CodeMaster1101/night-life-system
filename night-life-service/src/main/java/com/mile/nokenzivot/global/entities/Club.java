package com.mile.nokenzivot.global.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "club", indexes = {
    @Index(name = "idx_club_longitude_latitude", columnList = "longitude,latitude")
})
public class Club {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String name;
  private String email;
  private String genre;
  private int averageCost;
  private double rating;
  private String address;
  private Double latitude;
  private Double longitude;
  @OneToMany(mappedBy = "club")
  private Set<PartyEvent> events;

}
