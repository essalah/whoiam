package com.elhachmi.portfolio.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.elhachmi.portfolio.exception.StorageException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CloudinaryStorageServiceTest {

    private final MockMultipartFile file = new MockMultipartFile(
            "file", "avatar.png", "image/png", new byte[]{1, 2, 3}
    );

    @Test
    void rejectsMissingConfigurationBeforeCallingCloudinary() {
        Cloudinary cloudinary = mock(Cloudinary.class);
        var service = new CloudinaryStorageService(cloudinary, "empty", "", "");

        StorageException exception = assertThrows(
                StorageException.class,
                () -> service.uploadFile(file, "portfolio")
        );

        assertEquals(
                "Cloudinary is not configured. Set CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, and CLOUDINARY_API_SECRET",
                exception.getMessage()
        );
        verifyNoInteractions(cloudinary);
    }

    @Test
    void wrapsUncheckedCloudinaryFailuresAsStorageErrors() throws IOException {
        Cloudinary cloudinary = mock(Cloudinary.class);
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap()))
                .thenThrow(new IllegalArgumentException("Invalid cloud_name"));
        var service = new CloudinaryStorageService(cloudinary, "cloud", "key", "secret");

        StorageException exception = assertThrows(
                StorageException.class,
                () -> service.uploadFile(file, "portfolio")
        );

        assertEquals("Failed to upload file to Cloudinary", exception.getMessage());
    }

    @Test
    void rejectsCloudinaryResponsesWithoutSecureUrl() throws IOException {
        Cloudinary cloudinary = mock(Cloudinary.class);
        Uploader uploader = mock(Uploader.class);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(Map.of("public_id", "portfolio/avatar"));
        var service = new CloudinaryStorageService(cloudinary, "cloud", "key", "secret");

        StorageException exception = assertThrows(
                StorageException.class,
                () -> service.uploadFile(file, "portfolio")
        );

        assertEquals("Cloudinary upload did not return a secure URL", exception.getMessage());
    }
}
