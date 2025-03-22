package com.snackviet.api.Admin;

import java.util.Optional;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
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

import com.snackviet.dto.sanpham.InsertProductDTO;
import com.snackviet.dto.sanpham.InsertProductTypeDTO;
import com.snackviet.dto.taikhoan.InsertUserDTO;
import com.snackviet.model.HinhAnhSP;
import com.snackviet.model.LoaiSP;
import com.snackviet.model.LoaiSP;
import com.snackviet.repository.HinhAnhSPRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.LoaiSPRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.service.MaHoaMKService;

@RestController
@CrossOrigin("*")
public class ApiAdminProductType {
    @Autowired 
	LoaiSPRepository loaiSPRepository;
	
	boolean editting = false;
    Map<String, String> err = new HashMap<>();

    @GetMapping("api/admin/quan-ly-loai-san-pham")
    public Map<String, Object> getDisplay(@RequestParam("pageNo") Optional<Integer> pageNo,
    @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
    @RequestParam(name = "search", defaultValue = "") String search) {
  
    
        int currentPage = pageNo.orElse(1);
	    Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<LoaiSP> productTypePage = loaiSPRepository.findAll(pageable);
	    
		if(!search.isEmpty()) {
			productTypePage = loaiSPRepository.findByTenLoai(search, pageable);
		}

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Success");
        response.put("data", Map.of(
                "editting", false));
        response.put("message", "Lấy dữ liệu LoaiSP thành công");

        // Thêm giá trị mới vào "data"
        Map<String, Object> data = new HashMap<>((Map<String, Object>) response.get("data"));

        // Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về
        // admin/quanLySanPham
        if (productTypePage == null || productTypePage.isEmpty()) {
            data.put("types", Collections.emptyList());
            data.put("pagination", Map.of(
                    "totalItems", 0,
                    "totalPages", 0,
                    "currentPage", 0,
                    "pageSize", pageSize,
                    "search", search));
        }

        List<LoaiSP> types = productTypePage.getContent();
    
        // Cập nhật kích thước của danh sách sản phẩm cho từng loại
        for (LoaiSP type : types) {
            type.setTotalProducts(type.getListLoaiSP().size()); // Giả sử bạn có phương thức setTotalProducts trong LoaiSP
        }

        data.put("types", types);
        
        // data.put("totalProducts",);
        data.put("pagination", Map.of(
                "totalItems", productTypePage.getTotalElements(),
                "totalPages", productTypePage.getTotalPages(),
                "currentPage", currentPage,
                "pageSize", pageSize,
                "search", search
        ));

        // Cập nhật lại "data" vào trong response
        response.put("data", data);

        return response;

    }

    @GetMapping("/api/admin/quan-ly-loai-san-pham/{maLoai}")
    public Map<String, Object> getDetail(@PathVariable("maLoai") int maLoai,
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "sort", defaultValue = "") String sort) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        // set lại trạng thái cho biến editting
        data.put("editting", true);
        response.put("status", "Success");

        // Bắt lỗi tìm với id nằm ngoài dữ liệu
        Optional<LoaiSP> editOptional = loaiSPRepository.findById(maLoai);
        LoaiSP edit = editOptional.get();
        // đổ dữ liệu cho bảng chi tiết
        data.put("displayT", edit);
        data.put("displayType", loaiSPRepository.findById(maLoai).get());


        response.put("data", data);
        response.put("message", "Lấy dữ liệu loại sản phẩm được chọn thành công");
        return response;

    }

    
    @PostMapping("api/admin/quan-ly-loai-san-pham/create")
    public ResponseEntity<Map<String, Object>> createProductType(
        @RequestBody InsertProductTypeDTO insertProductTypeDTO){

        Map<String, Object> response = new HashMap<>();

        if (validation(insertProductTypeDTO.getTenLoai())) {

            // Kiểm tra dữ liệu unique
            if (loaiSPRepository.findByTenLoai(insertProductTypeDTO.getTenLoai()) != null) {
                response.put("status", "Failed");
                response.put("message", "Tên loại đã tồn tại");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            
            // Tạo loại sản phẩm
            LoaiSP loaiSP = new LoaiSP();

            loaiSP.setTenLoai(insertProductTypeDTO.getTenLoai());
            loaiSP.setTrangThai(insertProductTypeDTO.isTrangThai());
            
            // Lưu loại sản phẩm vào database
            loaiSPRepository.saveAndFlush(loaiSP);

            response.put("status", "Success");
            response.put("data", loaiSP);
            response.put("message", "Thêm loại sản phẩm mới thành công");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        }
        response.put("status", "Failed");
        response.put("data", err);
        response.put("message", "Thêm loại sản phẩm mới thất bại!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("api/admin/quan-ly-loai-san-pham/update/{maLoai}")
    public ResponseEntity<Map<String, Object>> updateProductType(
        @RequestBody InsertProductTypeDTO insertProductTypeDTO,
        @PathVariable("maLoai") int maLoai){

            Map<String, Object> response = new HashMap<>();
            LoaiSP update = loaiSPRepository.findById(maLoai).get();

            if(validation(insertProductTypeDTO.getTenLoai())){

                update.setTenLoai(insertProductTypeDTO.getTenLoai());  
                update.setTrangThai(insertProductTypeDTO.isTrangThai());

                loaiSPRepository.saveAndFlush(update);
                
                response.put("status", "Success");
                response.put("data", update);
                response.put("message", "Cập nhật loại sản phẩm thành công");

                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            response.put("status", "Failed");
            response.put("data", err);
            response.put("message", "Cập nhật loại sản phẩm thất bại!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }


    public boolean validation(String typeName) {
        boolean hasError = false;
		
		if(typeName.equals("")) {
            err.put("errTypeName","Vui lòng nhập tên loại sản phẩm!");
			hasError = true;
		}
		
		return !hasError;
	}

}
