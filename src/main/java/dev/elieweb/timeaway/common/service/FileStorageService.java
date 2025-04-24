package dev.elieweb.timeaway.common.service;

import dev.elieweb.timeaway.common.config.FileStorageConfig;
import dev.elieweb.timeaway.common.exception.BadRequestException;
import dev.elieweb.timeaway.common.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileStorageConfig fileStorageConfig;
    private final FileValidationService fileValidationService;

    public String storeFile(MultipartFile file) {
        // Validate file
        fileValidationService.validateFile(file);

        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getFileExtension(originalFileName);
            String fileName = UUID.randomUUID().toString() + fileExtension;

            Path targetLocation = Paths.get(fileStorageConfig.getUploadDir()).resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file. Please try again!", ex);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            throw new BadRequestException("Invalid file format");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = Paths.get(fileStorageConfig.getUploadDir()).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file. Please try again!", ex);
        }
    }
} 