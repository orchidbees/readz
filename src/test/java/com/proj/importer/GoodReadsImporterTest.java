package com.proj.importer;

import com.proj.constant.ReadingStatus;
import com.proj.dto.importer.ImportResult;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GoodReadsImporterTest {

    private GoodReadsImporter importer;

    @BeforeEach
    void init() {
        importer = new GoodReadsImporter();
    }

    @Test
    void should_parse_and_format_isbn() {
        GoodReadsRow gr = GoodReadsRow.builder()
                .isbn("=\"0451526341\"")
                .build();

        MultipartFile file = createFileForImport(gr);

        Set<ImportResult> results = importer.extract(file);

        assertThat(results)
                .singleElement()
                .extracting(ImportResult::getIsbn10)
                .isEqualTo("0451526341");
    }

    @Test
    void should_parse_dates() {
        GoodReadsRow gr = GoodReadsRow.builder()
                .dateRead("1970/01/01")
                .build();

        MultipartFile file = createFileForImport(gr);

        Set<ImportResult> results = importer.extract(file);

        assertThat(results)
                .singleElement()
                .extracting(ImportResult::getDateRead)
                .isEqualTo(LocalDate.of(1970, 1, 1));
    }

    @Test
    void should_parse_ints() {
        GoodReadsRow gr = GoodReadsRow.builder()
                .userRating("5")
                .build();

        MultipartFile file = createFileForImport(gr);

        Set<ImportResult> results = importer.extract(file);

        assertThat(results)
                .singleElement()
                .extracting(ImportResult::getUserRating)
                .isEqualTo(5);
    }

    @ParameterizedTest
    @MethodSource("provideReadingStatusMapping")
    void should_parse_exclusive_shelf_as_reading_status(String input, ReadingStatus expected) {
        GoodReadsRow gr = GoodReadsRow.builder()
                .shelf(input)
                .build();

        MultipartFile file = createFileForImport(gr);

        Set<ImportResult> results = importer.extract(file);

        assertThat(results)
                .singleElement()
                .extracting(ImportResult::getReadingStatus)
                .isEqualTo(expected);
    }

    @Test
    void should_parse_year() {
        GoodReadsRow gr = GoodReadsRow.builder()
                .originalPublicationYear("1970")
                .build();

        MultipartFile file = createFileForImport(gr);

        Set<ImportResult> results = importer.extract(file);

        assertThat(results)
                .singleElement()
                .extracting(ImportResult::getOriginalPublicationYear)
                .isEqualTo(Year.of(1970));
    }

    private static Stream<Arguments> provideReadingStatusMapping() {
        return Stream.of(
                Arguments.of("to-read", ReadingStatus.WANT_TO_READ),
                Arguments.of("currently-reading", ReadingStatus.READING),
                Arguments.of("read", ReadingStatus.READ));
    }

    private MultipartFile createFileForImport(GoodReadsRow... rows) {
        String csv = createCsv(rows);
        return new MockMultipartFile("file", csv.getBytes());
    }

    private String createCsv(GoodReadsRow... rows) {
        final String header = "Title,Author,Additional Authors,ISBN,ISBN13,My Rating,Original Publication Year," +
                "Date Read,Date Added,Exclusive Shelf,My Review,Private Notes,Read Count";

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(header);

        for (GoodReadsRow row : rows) {
            joiner.add(row.toString());
        }

        return joiner.toString();
    }

    @Builder
    private static class GoodReadsRow {
        @Builder.Default private String title = "";
        @Builder.Default private String author = "";
        @Builder.Default private String additionalAuthors = "";
        @Builder.Default private String isbn = "";
        @Builder.Default private String isbn13 = "";
        @Builder.Default private String userRating = "";
        @Builder.Default private String originalPublicationYear = "";
        @Builder.Default private String dateAdded = "";
        @Builder.Default private String dateRead = "";
        @Builder.Default private String shelf = "to-read";
        @Builder.Default private String userReview = "";
        @Builder.Default private String userNotes = "";
        @Builder.Default private String readCount = "";

        @Override
        public String toString() {
            List<String> values = List.of(title, author, additionalAuthors, isbn, isbn13, userRating,
                    originalPublicationYear, dateRead, dateAdded, shelf, userReview, userNotes, readCount);

            return String.join(",", values);
        }
    }
}
