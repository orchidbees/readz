package com.proj.importer;

import com.proj.dto.importer.ImportResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface BookImporter {

    Set<ImportResult> importBooks(MultipartFile file);
}
