package dev.elieweb.timeaway.common.service;

import dev.elieweb.timeaway.common.config.FileUploadConfig;
import dev.elieweb.timeaway.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileValidationService {
    private final FileUploadConfig fileUploadConfig;

    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File cannot be empty");
        }

        // Validate file size
        if (file.getSize() > fileUploadConfig.getMaxFileSize()) {
            throw new BadRequestException(String.format(
                "File size exceeds maximum limit of %d MB",
                fileUploadConfig.getMaxFileSize() / (1024 * 1024)
            ));
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !fileUploadConfig.getAllowedContentTypes().contains(contentType)) {
            throw new BadRequestException(
                "Invalid file type. Allowed types are: PDF, DOC, DOCX, JPEG, and PNG"
            );
        }
    }
} 