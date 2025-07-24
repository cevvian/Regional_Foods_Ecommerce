package edu.ut.sales.sales_analyst.services.cloundinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public ImageUploadService(@Value("${cloudinary.cloud_name}") String cloudName,
                              @Value("${cloudinary.api_key}") String apiKey,
                              @Value("${cloudinary.api_secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public List<String> uploadNewsImages(List<MultipartFile> files, String newsId) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new AppException(ErrorCode.FILE_UPLOAD_NOT_FOUND);
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String publicId = "news_" + newsId + "_" + System.currentTimeMillis();
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "resource_type", "image",
                    "folder", "news",
                    "public_id", publicId,
                    "context", Map.of("news_id", newsId),
                    "transformation", new Transformation()
                            .width(800).height(500).crop("fill").quality("auto")
                            .fetchFormat("webp")
            );

            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            urls.add((String) uploadResult.get("secure_url"));
        }

        if (urls.isEmpty()) {
            throw new AppException(ErrorCode.FILE_UPLOAD_NOT_FOUND);
        }

        return urls;
    }

    public List<String> uploadReviewImages(List<MultipartFile> files, String reviewId) throws IOException {
        if (files == null || files.isEmpty()) {
            throw new AppException(ErrorCode.FILE_UPLOAD_NOT_FOUND);
        }

        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            String publicId = "review_" + reviewId + "_" + System.currentTimeMillis();
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "resource_type", "image",
                    "folder", "reviews",
                    "public_id", publicId,
                    "context", Map.of("review_id", reviewId),
                    "transformation", new Transformation()
                            .width(600).height(600).crop("fill").quality("auto")
                            .fetchFormat("webp")
            );

            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            urls.add((String) uploadResult.get("secure_url"));
        }

        if (urls.isEmpty()) {
            throw new AppException(ErrorCode.FILE_UPLOAD_NOT_FOUND);
        }

        return urls;
    }
}
