package com.proj.importer;

import com.proj.constant.ReadingStatus;
import com.proj.dto.importer.ImportResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class GoodReadsImporter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public Set<ImportResult> extract(MultipartFile file) {
        Set<ImportResult> importedBooks = new HashSet<>();
        int rows = 0;

        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            Iterable<CSVRecord> records = getRecords(reader);

            for (CSVRecord record : records) {
                rows++;
                importedBooks.add(createImport(record));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to decode user import", e);
        }

        log.info("Successfully read {}/{} records", importedBooks.size(), rows);
        return importedBooks;
    }

    private Iterable<CSVRecord> getRecords(Reader reader) {
        try {
            return CSVFormat.RFC4180.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .get()
                    .parse(reader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to build CSV formatter for user import", e);
        }
    }

    private ImportResult createImport(CSVRecord record) {
        return ImportResult.builder()
                .title(record.get("Title"))
                .authors(getAuthors(record))
                .isbn10(parseIsbn(record.get("ISBN")))
                .isbn13(parseIsbn(record.get("ISBN13")))
                .userRating(parseInt(record.get("My Rating")))
                .originalPublicationYear(parseYear(record.get( "Original Publication Year")))
                .dateRead(parseDate(record.get("Date Read")))
                .dateAdded(parseDate(record.get("Date Added")))
                .readingStatus(parseExclusiveShelf(record.get("Exclusive Shelf")))
                .review(record.get("My Review"))
                .privateNotes(record.get("Private Notes"))
                .timesRead(parseInt(record.get("Read Count")))
                .build();
    }

    private Set<String> getAuthors(CSVRecord record) {
        Set<String> authors = new HashSet<>();
        authors.add(record.get("Author"));
        authors.addAll(List.of(record.get("Additional Authors").split(",")));

        return authors;
    }

    private String parseIsbn(String isbn) {
        if (isbn == null) return null;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < isbn.length(); i++) {
            Character c = isbn.charAt(i);

            if (Character.isDigit(c) || Character.isAlphabetic(c)) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    private Integer parseInt(String value) {
        return value == null || value.isBlank() ? null : Integer.parseInt(value);
    }

    private LocalDate parseDate(String date) {
        return date == null || date.isBlank() ? null : LocalDate.parse(date, formatter);
    }

    private Year parseYear(String year) {
        return year == null || year.isBlank() ? null : Year.parse(year);
    }

    private ReadingStatus parseExclusiveShelf(String shelf) {
        return switch (shelf.toLowerCase()) {
            case ("to-read") -> ReadingStatus.WANT_TO_READ;
            case ("currently-reading") -> ReadingStatus.READING;
            case ("read") -> ReadingStatus.READ;
            default -> throw new RuntimeException("Unexpected shelf seen in GoodReads import: " + shelf);
        };
    }

}
