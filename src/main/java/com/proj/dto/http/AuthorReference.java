package com.proj.dto;

import java.time.LocalDate;

public record AuthorReference(
  Long id,
  String fullName,
  LocalDate dateOfBirth) {}
