package com.proj.service;

import com.proj.dto.AuthorReference;
import com.proj.entity.Author;
import com.proj.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Set<Author> fetchAuthors(Set<Long> authorIds) {
        List<Author> authors = authorRepository.findAllById(authorIds);

        if (authors.size() != authorIds.size()) {
            List<Long> foundIds = authors.stream().map(Author::getId).toList();
            List<Long> missingIds = authorIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();

            log.warn("Author IDs not found: {}", missingIds);
        }

        return new HashSet<>(authors);
    }

    public Set<AuthorReference> convertToReference(Set<Author> authors) {
        Set<AuthorReference> authorReferences = new HashSet<>();

        authors.forEach(author -> {
            authorReferences.add(new AuthorReference(
                    author.getId(),
                    author.getFullName(),
                    author.getBirthDate()));
        });

        return authorReferences;
    }
}
