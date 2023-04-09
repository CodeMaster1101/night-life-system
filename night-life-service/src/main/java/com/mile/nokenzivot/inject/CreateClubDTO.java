package com.mile.nokenzivot.inject;

record CreateClubDTO(
    String name,
    String email,
    String genre,
    int averageCost,
    String address,
    double latitude,
    double longitude) {}
