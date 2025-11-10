package com.example.backend_services.util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

public class GCPStorageService {

    private static final String BUCKET_NAME = "webscraper-logs-bucket"; // replace with your GCP bucket name

    public String uploadFile(String filePath) {
        System.out.println("🚀 Uploading file: " + filePath);
        try (InputStream keyStream = getClass().getClassLoader().getResourceAsStream("gcp-key.json")) {

            if (keyStream == null) {
                throw new RuntimeException("❌ gcp-key.json not found in resources folder!");
            }

            Storage storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(keyStream))
                    .build()
                    .getService();

            String objectName = "logs_" + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

            BlobId blobId = BlobId.of(BUCKET_NAME, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("text/csv")
                    .build();

            storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));

            String publicUrl = "https://storage.googleapis.com/" + BUCKET_NAME + "/" + objectName;
            System.out.println("✅ Uploaded successfully: " + publicUrl);
            return publicUrl;

        } catch (StorageException se) {
            System.err.println("❌ GCP Storage error: " + se.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("❌ Upload failed: " + e.getMessage());
            return null;
        }
    }
}
