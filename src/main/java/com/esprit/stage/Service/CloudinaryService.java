package com.esprit.stage.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.util.List;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;


    public List<String> uploadImages(MultipartFile[] files) throws IOException {
        List<String> imageUrls = new ArrayList<>();


        for (MultipartFile file : files) {
            Map<String, String> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("url");
            imageUrls.add(imageUrl);
        }

        return imageUrls;
    }


    public void deleteImages(List<String> imageUrls) throws IOException {
        for (String imageUrl : imageUrls) {

            String publicId = extractPublicId(imageUrl);

            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }
    }


    private String extractPublicId(String imageUrl) {


        String[] urlParts = imageUrl.split("/");
        String publicIdWithVersion = urlParts[urlParts.length - 2] + "/" + urlParts[urlParts.length - 1].split("\\.")[0];

        return publicIdWithVersion;
    }
}
