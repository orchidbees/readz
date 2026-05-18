package com.proj.dto.importer;

import com.proj.constant.ReadingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Year;
import java.util.Set;

@Getter
@Builder
public class ImportResult {

    private String title;
    private Set<String> authors;
    private String isbn10;
    private String isbn13;
    private Year originalPublicationYear;
    private Integer userRating;
    private LocalDate dateRead;
    private LocalDate dateAdded;
    private ReadingStatus readingStatus;
    private String review;
    private String privateNotes;
    private Integer timesRead;
}
