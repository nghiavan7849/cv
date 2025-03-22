package com.snackviet.api;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.snackviet.dto.danhgia.CheckListDanhGiaDTO;
import com.snackviet.dto.danhgia.CreateDanhGiaDTO;
import com.snackviet.dto.danhgia.UpdateDanhGiaDTO;
import com.snackviet.model.DanhGia;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.HoaDon;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.DanhGiaRepository;
import com.snackviet.repository.HinhAnhDGRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.PhanHoiDanhGiaRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.DanhGiaService;
import com.snackviet.service.UploadService;

import io.swagger.v3.oas.models.responses.ApiResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
public class ApiDanhGia {
    private static final Logger logger = LoggerFactory.getLogger(ApiDanhGia.class);

    @Autowired
    DanhGiaRepository danhGiaRepository;
    @Autowired
    DanhGiaService danhGiaService;
    @Autowired
    SanPhamRepository sanPhamRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    @Autowired
    HinhAnhDGRepository hinhAnhDGRepository;
    @Autowired
    UploadService uploadService;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    PhanHoiDanhGiaRepository phanHoiDanhGiaRepository;

    @Autowired

    @GetMapping("/api/danh-gia/list")
    public Map<String, Object> getAll() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("danhGia", danhGiaRepository.findAll()));
        response.put("adminResponse",phanHoiDanhGiaRepository.findAll());
        response.put("message", "Lấy dữ liệu đánh giá thành công");
        return response;
    }

    @GetMapping("/api/danh-gia/get-one")
    public Map<String, Object> getByMa(@RequestParam("maDanhGia") Integer maDanhGia) {

        DanhGia danhGia = danhGiaRepository.findById(maDanhGia).get();
        return response("success", danhGia, "Lấy dữ liệu đánh giá thành công");
    }

    @GetMapping("/api/danh-gia/page/{pageSize}")
    public Map<String, Object> getListPage(@PathVariable("pageSize") int pageSize,
            @RequestParam("maSanPham") Integer maSanPham) {
        SanPham sanPham = sanPhamRepository.findById(maSanPham).get();
        Pageable pageable = PageRequest.of(pageSize - 1, 5, Sort.by(Direction.DESC, "maDanhGia"));
        Page<DanhGia> page = danhGiaRepository.findBySanPhamDG(sanPham, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", page.getTotalPages());
        response.put("data", Map.of("danhGia", page.getContent()));
        response.put("message", "Lấy dữ liệu đánh giá thành công");

        return response;
    }

    @GetMapping("/api/danh-gia/list-page")
    public Map<String, Object> getListPagePage(@RequestParam("page") int pageApi,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("maSanPham") Integer maSanPham) {
        SanPham sanPham = sanPhamRepository.findById(maSanPham).get();
        Pageable pageable = PageRequest.of(pageApi - 1, pageSize, Sort.by(Direction.DESC, "maDanhGia"));
        Page<DanhGia> page = danhGiaRepository.findBySanPhamDG(sanPham, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", page.getTotalPages());
        response.put("data", Map.of("danhGia", page.getContent()));
        response.put("message", "Lấy dữ liệu đánh giá thành công");

        return response;
    }

    @GetMapping("/api/danh-gia/page")
    public Map<String, Object> getListPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
            @RequestParam("soSao") int soSao, @RequestParam("maSanPham") Integer maSanPham) {
        SanPham sanPham = sanPhamRepository.findById(maSanPham).get();
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Direction.DESC, "maDanhGia"));
        Page<DanhGia> pageList = danhGiaRepository.findBySoSaoAndSanPhamDG(soSao, sanPham, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", pageList.getTotalPages());
        response.put("data", Map.of("danhGia", pageList.getContent()));
        response.put("message", "Lấy dữ liệu đánh giá thành công");
        return response;
    }

    @PostMapping("/api/danh-gia/create")
    public Map<String, Object> postMethodName(@RequestBody CreateDanhGiaDTO createDanhGiaDTO) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(createDanhGiaDTO.getMaTaiKhoan()).get();
        SanPham sanPham = sanPhamRepository.findById(createDanhGiaDTO.getMaSanPham()).get();
        HoaDon hoaDon = hoaDonRepository.findById(createDanhGiaDTO.getMaHoaDon()).get();
        DanhGia danhGia = new DanhGia();
        danhGia.setHoaDonDG(hoaDon);
        danhGia.setBinhLuan(createDanhGiaDTO.getBinhLuan());
        danhGia.setNgayDanhGia(new Date());
        danhGia.setSanPhamDG(sanPham);
        danhGia.setSoSao(createDanhGiaDTO.getSoSao());
        danhGia.setTaiKhoanDG(taiKhoan);
        danhGia.setTrangThai(true);

        danhGiaRepository.saveAndFlush(danhGia);
        return response("success", danhGia, "Thêm đánh giá thành công");
    }

    @PutMapping("/api/danh-gia/update")
    public Map<String, Object> update(@RequestBody UpdateDanhGiaDTO updateDanhGiaDTO) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(updateDanhGiaDTO.getMaTaiKhoan()).get();
        SanPham sanPham = sanPhamRepository.findById(updateDanhGiaDTO.getMaSanPham()).get();
        DanhGia danhGia = new DanhGia();
        danhGia.setMaDanhGia(updateDanhGiaDTO.getMaDanhGia());
        danhGia.setBinhLuan(updateDanhGiaDTO.getBinhLuan());
        danhGia.setNgayDanhGia(new Date());
        danhGia.setSanPhamDG(sanPham);
        danhGia.setSoSao(updateDanhGiaDTO.getSoSao());
        danhGia.setTaiKhoanDG(taiKhoan);
        danhGia.setTrangThai(true);

        danhGiaRepository.saveAndFlush(danhGia);
        return response("success", danhGia, "Cập nhật đánh giá thành công");
    }

    @GetMapping("/api/hinh-anh-danh-gia/page/{pageSize}")
    public Map<String, Object> getHinhAnhDanhGia(@PathVariable("pageSize") int pageSize,
            @RequestParam("maDanhGia") Integer id) {

        DanhGia danhGia = danhGiaRepository.findById(id).get();
        Pageable pageable = PageRequest.of(pageSize - 1, 5, Sort.by(Direction.DESC, "maHinhAnhDG"));
        Page<HinhAnhDG> page = hinhAnhDGRepository.findByDanhGia(danhGia, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", page.getTotalPages());
        response.put("data", Map.of("hinhAnhDG", page.getContent()));
        response.put("message", "Lấy dữ kiệu hình ảnh của đánh giá thành công");
        return response;
    }

    @PostMapping("/api/hinh-anh-danh-gia/create")
    public ResponseEntity<?> createHinhAnhDG(
            @RequestParam("url") String firebaseUrl, // Nhận URL từ phía client
            @RequestParam("maDanhGia") Integer maDanhGia) {

        // Logging để kiểm tra
        logger.info("Received POST request to /hinh-anh-danh-gia/create");
        // Khởi tạo response map
        Map<String, Object> response = new HashMap<>();
        // Tìm đánh giá dựa trên maDanhGia
        Optional<DanhGia> optionalDanhGia = danhGiaRepository.findById(maDanhGia);
        if (!optionalDanhGia.isPresent()) {
            response.put("status", "error");
            response.put("message", "Không tìm thấy đánh giá");
            return ResponseEntity.badRequest().body(response);
        }
        DanhGia danhGia = optionalDanhGia.get();

        // Tạo đối tượng HinhAnhDG để lưu thông tin vào cơ sở dữ liệu
        HinhAnhDG hinhAnhDG = new HinhAnhDG();
        hinhAnhDG.setDanhGia(danhGia);
        hinhAnhDG.setTenHinhAnh(firebaseUrl); // Lưu URL Firebase thay vì tên file cục bộ
        hinhAnhDGRepository.saveAndFlush(hinhAnhDG); // Lưu vào cơ sở dữ liệu

        // Cấu hình response trả về
        response.put("status", "success");
        response.put("data", hinhAnhDG);
        response.put("message", "Thêm hình ảnh của đánh giá thành công");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/hinh-anh-danh-gia/update")
    public ResponseEntity<?> updateHinhAnhDG(@RequestParam("file") MultipartFile file,
            @RequestParam("url") String firebaseUrl,
            @RequestParam("maDanhGia") Integer maDanhGia,
            @RequestParam("maHinhAnhDG") Integer maHinhAnhDG) {
        // Logging to check request
        logger.info("Received PUT request to /hinh-anh-danh-gia/update");

        // Initialize response map
        Map<String, Object> response = new HashMap<>();

        try {
            // Find the review and image data by ID
            DanhGia danhGia = danhGiaRepository.findById(maDanhGia)
                    .orElseThrow(() -> new RuntimeException("Review not found"));
            HinhAnhDG hinhAnhDG = hinhAnhDGRepository.findById(maHinhAnhDG)
                    .orElseThrow(() -> new RuntimeException("Image not found"));

            // Delete the old image from Firebase
            uploadService.deleteFileFromFirebase(hinhAnhDG.getTenHinhAnh());

            // Update the image details
            hinhAnhDG.setDanhGia(danhGia);
            hinhAnhDG.setTenHinhAnh(firebaseUrl); // Set the new Firebase URL

            // Save the updated image object to the database
            hinhAnhDGRepository.saveAndFlush(hinhAnhDG);

            // Return the updated image details in the response
            response.put("message", "Image updated successfully");
            response.put("image", hinhAnhDG);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error updating review image", e);
            response.put("message", "Failed to update image");
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/api/danh-gia/check")
    public ResponseEntity<?> checkReviewed(@RequestParam("maSanPham") String maSanPham,
            @RequestParam("maTaiKhoan") String maTaiKhoan) {
        boolean isReviewed = danhGiaService.isReviewed(maSanPham, maTaiKhoan);
        Map<String, Object> response = new HashMap<>();
        if (isReviewed) {
            response.put("status", "success");
            response.put("check", true);
            response.put("message","Tài khoản đã đánh giá sản phẩm này.");
            return ResponseEntity.ok().body(response);
        } else {
            response.put("status", "success");
            response.put("check", false);
            response.put("message","Tài khoản chưa đánh giá sản phẩm này.");
            return ResponseEntity.ok().body(response);
        }
    }

    @PostMapping("/api/danh-gia/check-list")
    public ResponseEntity<?> checkReviewedList(@RequestParam("maTaiKhoan") String maTaiKhoan, @RequestBody CheckListDanhGiaDTO checkListDanhGiaDTO) {
        List<Map<String, Object>> list = new ArrayList<>();
        for(Integer id: checkListDanhGiaDTO.getListIds()){
            boolean isReviewed = danhGiaService.isReviewed(String.valueOf(id), maTaiKhoan);
            Map<String, Object> map = new HashMap<>();
            map.put("maSanPham", id);
            map.put("check", isReviewed);
            list.add(map); 
        }

        Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("listCheck", list);
            response.put("message","Lấy thành công");
            return ResponseEntity.ok().body(response);
    }

    public Map<String, Object> response(String status, DanhGia danhGia, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("data", danhGia == null ? null : Map.of("danhGia", danhGia));
        response.put("message", message);
        return response;
    }

    public Map<String, Object> responseHADG(String status, HinhAnhDG hinhAnhDG, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("data", hinhAnhDG == null ? null : Map.of("hinhAnhDG", hinhAnhDG));
        response.put("message", message);
        return response;
    }
}
