package com.mile.nokenzivot.scrape_handler;

record ScrapedEvent(
    String placeName,
    String subject,
    String description,
    byte[] data,
    String date) {}
