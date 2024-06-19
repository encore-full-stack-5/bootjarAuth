package com.example.bootjarAuth.service;


import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GcsService {
    @Value("${spring.cloud.gcp.bucket}")
    private String bucketName;

    private final Storage storage;

    public String uploadFile(String fileName ,byte[] fileData) {
        // UUID 생성
        String uuid = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(fileName);
        String fullFileName = uuid + fileExtension;

        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, fullFileName).build(),
                fileData
        );

      return blobInfo.getMediaLink();
    }
//파일 확장자 검증
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException("올바르지 못한 확장자입니다");
        }
        return fileName.substring(dotIndex);
    }
}
