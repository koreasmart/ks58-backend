package sst.community.service;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class CommunityImageService {

    public void deleteImage(String imageUrl) {

        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        // /uploads/community/test.png
        String filePath = imageUrl.replace("/uploads/", "");

        File file = new File("uploads/" + filePath);

        if (file.exists()) {
            file.delete();
        }
    }
}