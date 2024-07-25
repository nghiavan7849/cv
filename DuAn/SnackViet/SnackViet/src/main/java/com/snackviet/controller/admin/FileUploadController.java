package com.snackviet.controller.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;

@RestController
public class FileUploadController {
	
	@Autowired
	ServletContext app;

    @PostMapping("/image/AnhSanPham/")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String fileName = file.getOriginalFilename();
//                Path uploadPath = Paths.get("image/AnhSanPham/" + fileName);
                
				String realPath = app.getRealPath("/image/AnhSanPham/" + fileName);
				Path path = Path.of(realPath);
//				if (!Files.exists(path.getParent())) {
//					Files.createDirectories(path);
//					
//				}
//				Files.copy(hinhAnh.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//				imageName = filename;

                // Tạo thư mục nếu chưa tồn tại
                if (!Files.exists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                }

                // Sao chép nội dung tệp vào thư mục lưu trữ
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                // Trả về URL của tệp đã tải lên
                String fileUrl = "/image/AnhSanPham/" + fileName;
                fileUrls.add(fileUrl);
            } catch (IOException e) {
                // Xử lý lỗi khi không thể tải lên tệp
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
            }
        }

        // Trả về danh sách URL của các tệp đã tải lên
        return ResponseEntity.ok().body(Collections.singletonMap("urls", fileUrls));
    }
}