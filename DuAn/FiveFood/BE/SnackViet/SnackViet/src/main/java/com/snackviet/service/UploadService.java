package com.snackviet.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class UploadService {
    @Autowired
    ServletContext context;

    public File saveFile(MultipartFile file, String url) {
        // Tạo tên file duy nhất để tránh ghi đè
        String fileName = file.getOriginalFilename();
        // String fileName = System.currentTimeMillis() + "_" +
        // file.getOriginalFilename();

        // Xác định đường dẫn thư mục lưu trữ
        String relativePath = "public/image/" + url;
        String realPath = Paths.get(relativePath).toAbsolutePath().toString();
        Path directoryPath = Paths.get(realPath);
        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(directoryPath)) { 
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create directory for file upload.");
            }
        }

        // Xác định đường dẫn file để lưu
        File saveFile = new File(directoryPath.toFile(), fileName);

        // Lưu file vào hệ thống
        try {
            file.transferTo(saveFile);
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not save file.");
        }
        System.out.println(saveFile.getAbsolutePath());
        return saveFile;
    }

    public boolean deleteFileFromFirebase(String imageUrl) {
        try {
            // Tên bucket
            String bucketName = "fivefood-datn-8a1cf.appspot.com";
            String filePath = "AnhDanhGia/" + imageUrl; // Đường dẫn đến tệp trong Firebase
            // Khởi tạo Storage
            Storage storage = StorageOptions.getDefaultInstance().getService();

            // Get the bucket and delete the file
            Bucket bucket = storage.get(bucketName);
            if (bucket != null) {
                // Delete the file from Firebase Storage
                boolean deleted = bucket.get(filePath).delete(); // Sử dụng đường dẫn đầy đủ để xóa
                if (deleted) {
                    System.out.println("Deleted file from Firebase: " + filePath);
                } else {
                    System.out.println("File not found in Firebase: " + filePath);
                }
                return deleted; // Trả về true nếu xóa thành công
            } else {
                System.out.println("Bucket not found: " + bucketName);
                return false; // Nếu không tìm thấy bucket
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

}
