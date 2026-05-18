package com.proj.dto.http;

import java.time.LocalDate;

public record AuthorReference(
  Long id,
  String fullName,
  LocalDate dateOfBirth) {}
