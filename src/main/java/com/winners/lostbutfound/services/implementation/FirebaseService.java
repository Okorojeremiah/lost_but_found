package com.winners.lostbutfound.services.implementation;



import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
@Service
public class FirebaseService  {


    @Value("${firebase.project.id}")
    private String projectId;

    @Value("${firebase.storage.bucket}")
    private String storageBucket;

    public String uploadFile(MultipartFile file) throws IOException {
        StorageOptions options = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(GoogleCredentials.fromStream(new FileInputStream("src/main/resources/firebase-admin.json")))
                .build();

        Storage storage = options.getService();

        String fileName = "uploads/" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(storageBucket, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

        storage.create(blobInfo, file.getBytes());

        storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        return String.format("https://storage.googleapis.com/%s/%s", storageBucket, fileName);
    }
}
