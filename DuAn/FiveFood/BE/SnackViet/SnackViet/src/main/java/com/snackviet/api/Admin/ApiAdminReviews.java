package com.snackviet.api.Admin;

import java.util.Optional;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.beans.Transient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.dto.danhgia.InsertReviewDTO;
import com.snackviet.dto.sanpham.InsertProductDTO;
import com.snackviet.dto.taikhoan.InsertUserDTO;
import com.snackviet.model.DanhGia;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.LoaiSP;
import com.snackviet.model.PhanHoiDanhGia;
import com.snackviet.model.TaiKhoan;
import com.snackviet.model.DanhGia;
import com.snackviet.repository.DanhGiaRepository;
import com.snackviet.repository.HinhAnhDGRepository;
import com.snackviet.repository.HinhAnhSPRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.LoaiSPRepository;
import com.snackviet.repository.PhanHoiDanhGiaRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.MaHoaMKService;

@RestController
@CrossOrigin("*")
public class ApiAdminReviews {
    
    boolean editting = false;
	@Autowired
	DanhGiaRepository danhGiaRepository;
	@Autowired
	HinhAnhDGRepository hinhAnhDGRepository;
	@Autowired
	SanPhamRepository sanPhamRepository;
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	@Autowired
	LoaiSPRepository loaiSPRepository;
    @Autowired
    PhanHoiDanhGiaRepository phanHoiDanhGiaRepository;

    Map<String, String> err = new HashMap<>();

    @GetMapping("api/admin/quan-ly-danh-gia")
    public Map<String, Object> getDisplay(@RequestParam("pageNo") Optional<Integer> pageNo,
			@RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
			@RequestParam(name = "search", defaultValue = "") String search,
			@RequestParam(name = "sort", defaultValue = "") String sort,
			@RequestParam(name = "ngayFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayFrom,
			@RequestParam(name = "ngayTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayTo) {

        Map<String, Object> cards = new HashMap<>();
        // lấy tổng số đánh giá
        cards.put("total", danhGiaRepository.findAll().size());
        // lấy tổng số đánh giá 5 sao
        cards.put("fivestar", danhGiaRepository.findBySoSao(5).size());

        // PHÂN TRANG VÀ TÌM KIẾM
		int currentPage = pageNo.orElse(1);
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<DanhGia> reviewPage = null;

        if (!search.isEmpty()) {
            if (sort.equals("Tên sản phẩm")) {
                reviewPage = danhGiaRepository.findBySanPhamDGTenSanPhamContaining(search, pageable);
            } else if (sort.equals("Số sao")) {
                try {
                    int soSao = Integer.parseInt(search);
                    reviewPage = danhGiaRepository.findBySoSao(soSao, pageable);
                } catch (NumberFormatException e) {
                    // Nếu không thể chuyển đổi search thành số, trả về tất cả
                    reviewPage = danhGiaRepository.findAll(pageable);
                }
            } else if (sort.equals("Bình luận")) {
                reviewPage = danhGiaRepository.findByBinhLuanContaining(search, pageable);
            } else {
                reviewPage = danhGiaRepository.findAll(pageable); // Hoặc có thể sắp xếp theo mặc định
            }
        } else if (!sort.isEmpty()) {
            if (sort.equals("Trạng thái hiển thị")) {
                reviewPage = danhGiaRepository.findByTrangThai(true, pageable);
            } else if (sort.equals("Trạng thái ẩn")) {
                reviewPage = danhGiaRepository.findByTrangThai(false, pageable);
            } else if (sort.equals("Ngày đánh giá") && ngayFrom != null && ngayTo != null && ngayFrom.before(ngayTo)) {
                reviewPage = danhGiaRepository.findByNgayDanhGiaBetween(ngayFrom, ngayTo, pageable);
            } else {
                reviewPage = danhGiaRepository.findAll(pageable); // Hoặc có thể sắp xếp theo mặc định
            }
        } else {
            // Không lọc dữ liệu
            reviewPage = danhGiaRepository.findAll(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Success");
        response.put("data", Map.of(
                "cards", cards,
                "editting", false));
        response.put("message", "Lấy dữ liệu DanhGia thành công");

        // Thêm giá trị mới vào "data"
        Map<String, Object> data = new HashMap<>((Map<String, Object>) response.get("data"));

        System.out.println("Đây là review Page: "+reviewPage);

        // Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về
        // admin/quanLySanPham
        if (reviewPage == null || reviewPage.isEmpty()) {
            data.put("reviews", Collections.emptyList());
            data.put("pagination", Map.of(
                    "totalItems", 0,
                    "totalPages", 0,
                    "currentPage", 0,
                    "pageSize", pageSize,
                    "search", search,
                    "sort", sort
                    // "ngayFrom", ngayFrom,
			        // "ngayTo", ngayTo
                    ));
            // Load lại dữ liệu select box đã được chọn
            data.put("displaySelected", sort);
        }

        data.put("reviews", reviewPage.getContent());
        data.put("pagination", Map.of(
                "totalItems", reviewPage.getTotalElements(),
                "totalPages", reviewPage.getTotalPages(),
                "currentPage", currentPage,
                "pageSize", pageSize,
                "search", search,
                "sort", sort
                // "ngayFrom", ngayFrom,
			    // "ngayTo", ngayTo
        ));

        // Load lại dữ liệu select box đã được chọn
        data.put("displaySelected", sort);

        //lấy hình ảnh đánh giá
        data.put("listHinhAnhDG", hinhAnhDGRepository.findAll());

        //lấy tất cả review không phân trang
        data.put("allReviews",danhGiaRepository.findAll());

        // Cập nhật lại "data" vào trong response
        response.put("data", data);

        return response;

    }

    @GetMapping("/api/admin/quan-ly-danh-gia/{maDanhGia}")
    public Map<String, Object> getDetail(@PathVariable("maDanhGia") int maDanhGia,
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "sort", defaultValue = "") String sort) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        // set lại trạng thái cho biến editting
        data.put("editting", true);
        response.put("status", "Success");

        
        Optional<DanhGia> editOptional = danhGiaRepository.findById(maDanhGia);
        DanhGia edit = editOptional.get();
        // đổ dữ liệu cho bảng chi tiết
        data.put("displayR", edit);
        data.put("displayReview", danhGiaRepository.findById(maDanhGia).get());

        //Hiển thị list hình ảnh sản phẩm
	    List<HinhAnhDG> listHinhDG = hinhAnhDGRepository.findByDanhGiaMaDanhGia(maDanhGia);
		data.put("listHinhDG", listHinhDG);

        //Hiển thị phản hồi đánh giá
        PhanHoiDanhGia phanHoiDanhGia = phanHoiDanhGiaRepository.findByMaDanhGiaMaDanhGia(edit.getMaDanhGia());
        data.put("phanHoi",phanHoiDanhGia);

        response.put("data", data);
        response.put("message", "Lấy dữ liệu đánh giá được chọn thành công");
        return response;

    }

    
    @PostMapping("api/admin/quan-ly-danh-gia/create")
    public ResponseEntity<Map<String, Object>> createProduct(
        @RequestBody InsertReviewDTO insertReviewDTO){

        Map<String, Object> response = new HashMap<>();

        if (validation(insertReviewDTO.getNoiDungPhanHoi(),
        insertReviewDTO.getMaDanhGia(),
        insertReviewDTO.getMaTaiKhoan())) {
            //khởi tạo DanhGia và TaiKhoan
            DanhGia getDG = danhGiaRepository.findById(insertReviewDTO.getMaDanhGia()).get();
            TaiKhoan getTK = taiKhoanRepository.findById(insertReviewDTO.getMaTaiKhoan()).get();
            PhanHoiDanhGia phanHoi = new PhanHoiDanhGia();
            phanHoi.setDaPhanHoi(true);
            phanHoi.setMaDanhGia(getDG);
            phanHoi.setMaTaiKhoan(getTK);
            phanHoi.setNoiDungPhanHoi(insertReviewDTO.getNoiDungPhanHoi());

            phanHoiDanhGiaRepository.saveAndFlush(phanHoi);

            response.put("status", "Success");
            response.put("data", phanHoi);
            response.put("message", "Phản hồi đánh giá thành công");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        }
        response.put("status", "Failed");
        response.put("data", err);
        response.put("message", "Phản hồi đánh giá thất bại!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("api/admin/quan-ly-danh-gia/update/{maDanhGia}")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateReview(
        @RequestBody InsertReviewDTO insertReviewDTO,
        @PathVariable("maDanhGia") int maDanhGia){

            Map<String, Object> response = new HashMap<>();
            DanhGia update = danhGiaRepository.findById(maDanhGia).get();

            if(validation(insertReviewDTO.getNoiDungPhanHoi(),
                          insertReviewDTO.getMaDanhGia(),
                          insertReviewDTO.getMaTaiKhoan()
            )){

                //khởi tạo DanhGia và TaiKhoan
                TaiKhoan getTK = taiKhoanRepository.findById(insertReviewDTO.getMaTaiKhoan()).get();
                PhanHoiDanhGia phanHoi = phanHoiDanhGiaRepository.findById(insertReviewDTO.getMaPhanHoiDanhGia()).get();
                phanHoi.setDaPhanHoi(true);
                phanHoi.setMaDanhGia(update);
                phanHoi.setMaTaiKhoan(getTK);
                phanHoi.setNoiDungPhanHoi(insertReviewDTO.getNoiDungPhanHoi());

                phanHoiDanhGiaRepository.saveAndFlush(phanHoi);

                response.put("status", "Success");
                response.put("data", update);
                response.put("message", "Cập nhật phản hồi đánh giá thành công");

                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            response.put("status", "Failed");
            response.put("data", err);
            response.put("message", "Cập nhật phản hồi đánh giá thất bại!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }


    public boolean validation(String noiDungPhanHoi,int maDanhGia,int maTaiKhoan) {
		
		boolean hasError = false;
		
		if(noiDungPhanHoi.equals("")) {
            err.put("errReply","Vui lòng nhập vào phản hồi!");
			hasError = true;
		}
		if(maDanhGia==-1) {
            err.put("errReview","Mã đánh giá không được rỗng!");
			hasError = true;
		}
		if(maTaiKhoan==-1) {
            err.put("errAccount","Mã tài khoản không được rỗng!");
			hasError = true;
		}
		
		return !hasError;
	}

}
