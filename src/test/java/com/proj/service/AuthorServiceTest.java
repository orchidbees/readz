package com.proj.service;

import com.proj.dto.http.AuthorReference;
import com.proj.entity.Author;
import com.proj.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({ MockitoExtension.class, OutputCaptureExtension.class })
public class AuthorServiceTest {

    @InjectMocks private AuthorService authorService;
    @Mock private AuthorRepository authorRepository;

    @BeforeEach
    void init() {
        authorService = new AuthorService(authorRepository);
    }

    @Test
    void missing_author_ids_should_be_logged(CapturedOutput output) {
        Set<Long> authorIds = Set.of(1L, 2L, 3L);

        Author author = new Author();
        author.setId(1L);

        when(authorRepository.findAllById(authorIds)).thenReturn(List.of(author));

        authorService.fetchAuthors(authorIds);

        assertThat(output.getOut()).contains("Author IDs not found: [2, 3]");
    }

    @Test
    void author_entity_should_be_mapped_to_author_reference() {
        Author author = new Author();
        author.setId(1L);
        author.setFullName("Carl Sagan");
        author.setBirthDate(LocalDate.parse("1934-11-09"));

        Set<AuthorReference> authorReference = authorService.convertToReference(Set.of(author));

        assertThat(authorReference)
                .singleElement()
                .isEqualTo(new AuthorReference(author.getId(), author.getFullName(), author.getBirthDate()));
    }
}
