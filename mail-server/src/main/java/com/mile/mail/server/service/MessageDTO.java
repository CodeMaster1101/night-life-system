package com.mile.mail.server.service;

record MessageDTO(String sender, String subject, String description, byte[] data) {}
