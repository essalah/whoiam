package com.elhachmi.portfolio.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.elhachmi.portfolio.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;
    private final boolean configured;

    public CloudinaryStorageService(Cloudinary cloudinary,
                                    @Value("${cloudinary.cloud-name:}") String cloudName,
                                    @Value("${cloudinary.api-key:}") String apiKey,
                                    @Value("${cloudinary.api-secret:}") String apiSecret) {
        this.cloudinary = cloudinary;
        this.configured = isConfigured(cloudName) && isConfigured(apiKey) && isConfigured(apiSecret);
    }

    @Override
    public String uploadFile(MultipartFile file, String folder) {
        if (!configured) {
            throw new StorageException(
                    "Cloudinary is not configured. Set CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY, and CLOUDINARY_API_SECRET"
            );
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "auto"
                    ));
            Object secureUrl = uploadResult.get("secure_url");
            if (secureUrl == null || secureUrl.toString().isBlank()) {
                throw new StorageException("Cloudinary upload did not return a secure URL");
            }
            return secureUrl.toString();
        } catch (StorageException ex) {
            throw ex;
        } catch (IOException | RuntimeException ex) {
            throw new StorageException("Failed to upload file to Cloudinary", ex);
        }
    }

    private boolean isConfigured(String value) {
        return value != null && !value.isBlank() && !"empty".equalsIgnoreCase(value.trim());
    }
}
