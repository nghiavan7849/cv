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
import com.snackviet.dto.taikhoan.InsertUserDTO;
import com.snackviet.model.HinhAnhSP;
import com.snackviet.model.LoaiSP;
import com.snackviet.model.SanPham;
import com.snackviet.repository.HinhAnhSPRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.LoaiSPRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.service.MaHoaMKService;

@RestController
@CrossOrigin("*")
public class ApiAdminProducts {
    @Autowired
    SanPhamRepository sanPhamRepository;
    // @Autowired
    // HoaDonRepository hoaDonRepository;
    @Autowired
    LoaiSPRepository loaiSPRepository;
    @Autowired
    HinhAnhSPRepository hinhAnhSPRepository;

    Map<String, String> err = new HashMap<>();

    @GetMapping("api/admin/quan-ly-san-pham")
    public Map<String, Object> getDisplay(@RequestParam("pageNo") Optional<Integer> pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "sort", defaultValue = "") String sort) {

        Map<String, Object> cards = new HashMap<>();
        // lấy số sản phẩm ngưng bán
        cards.put("inactive", sanPhamRepository.findByTrangThai(false).size());
        // lấy tổng số sản phẩm
        cards.put("total", sanPhamRepository.count());

        // PHÂN TRANG VÀ TÌM KIẾM
        int currentPage = pageNo.orElse(1);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<SanPham> productPage = null;

        if (!search.isEmpty()) {
            // Lọc dữ liệu theo cả search và sort
            if (sort.equals("Loại")) {
                productPage = sanPhamRepository.findByLoaiSPTenLoaiContainingAndLoaiSPTrangThai(search,true, pageable);
            } else if (sort.equals("Tên sản phẩm")) {
                productPage = sanPhamRepository.findByTenSanPhamContaining(search, pageable);
            } else if (sort.equals("Khối lượng")) {
                productPage = sanPhamRepository.findByTrongLuong(Double.parseDouble(search), pageable);
            } else if (sort.equals("Trạng thái hoạt động")) {
	        	productPage = sanPhamRepository.findByTrangThai(true, pageable);
	        } 
	        else if (sort.equals("Trạng thái ngưng bán")) {
	        	productPage = sanPhamRepository.findByTrangThai(false, pageable);
	        } 
        } else if(!sort.isEmpty()) {
	    	if (sort.equals("Loại") && search.equals("")) {
	    	    productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	    	} else if (sort.equals("Tên sản phẩm") && search.equals("")) {
	    		productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	        } 
	        else if (sort.equals("Khối lượng") && search.equals("")) {
	        	productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	        }
	        else if (sort.equals("Giá thấp tới cao")) {
	        	productPage = sanPhamRepository.findByOrderByGiaAsc(pageable);
	        }
	        else if (sort.equals("Giá cao tới thấp")) {
	        	productPage = sanPhamRepository.findByOrderByGiaDesc(pageable);
	        }
	        else if (sort.equals("Trạng thái hoạt động")) {
	        	productPage = sanPhamRepository.findByTrangThai(true, pageable);
	        } 
	        else if (sort.equals("Trạng thái ngưng bán")) {
	        	productPage = sanPhamRepository.findByTrangThai(false, pageable);
	        } 
	    }
	    else {
	        // Không lọc dữ liệu
	        productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	    }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Success");
        response.put("data", Map.of(
                "cards", cards,
                "editting", false));
        response.put("message", "Lấy dữ liệu SanPham thành công");

        // Thêm giá trị mới vào "data"
        Map<String, Object> data = new HashMap<>((Map<String, Object>) response.get("data"));

        // Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về
        // admin/quanLySanPham
        if (productPage == null || productPage.isEmpty()) {
            data.put("products", Collections.emptyList());
            data.put("pagination", Map.of(
                    "totalItems", 0,
                    "totalPages", 0,
                    "currentPage", 0,
                    "pageSize", pageSize,
                    "search", search,
                    "sort", sort));
            // Load lại dữ liệu select box đã được chọn
            data.put("displaySelected", sort);
        }

        data.put("products", productPage.getContent());
        data.put("pagination", Map.of(
                "totalItems", productPage.getTotalElements(),
                "totalPages", productPage.getTotalPages(),
                "currentPage", currentPage,
                "pageSize", pageSize,
                "search", search,
                "sort", sort
        // ,
        // "ngayFrom",ngayFrom,
        // "ngayTo",ngayTo
        ));

        // Load lại dữ liệu select box đã được chọn
        data.put("displaySelected", sort);

        //Đổ dữ liệu cho select box
	    data.put("listLoaiSP",loaiSPRepository.findAll());

        // Cập nhật lại "data" vào trong response
        response.put("data", data);

        return response;

    }

    @GetMapping("/api/admin/quan-ly-san-pham/{maSanPham}")
    public Map<String, Object> getDetail(Model model, @PathVariable("maSanPham") int maSanPham,
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "sort", defaultValue = "") String sort) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        // set lại trạng thái cho biến editting
        data.put("editting", true);
        response.put("status", "Success");

        // Bắt lỗi tìm với id nằm ngoài dữ liệu
        Optional<SanPham> editOptional = sanPhamRepository.findById(maSanPham);
        SanPham edit = editOptional.get();
        // đổ dữ liệu cho bảng chi tiết
        data.put("displayP", edit);
        data.put("displayProduct", sanPhamRepository.findById(maSanPham).get());

        //Hiển thị list hình ảnh sản phẩm
	    List<HinhAnhSP> listHinhSP = hinhAnhSPRepository.findBySanPhamHAMaSanPham(maSanPham);
		data.put("listHinhSP", listHinhSP);

        response.put("data", data);
        response.put("message", "Lấy dữ liệu sản phẩm được chọn thành công");
        return response;

    }

    
    @PostMapping("api/admin/quan-ly-san-pham/create")
    public ResponseEntity<Map<String, Object>> createProduct(
        @RequestBody InsertProductDTO insertProductDTO){

        Map<String, Object> response = new HashMap<>();

        System.out.println("Đây nhé: "+insertProductDTO.getTenSanPham());;

        if (validation(insertProductDTO.getTenSanPham(),
        insertProductDTO.getGia(),
        insertProductDTO.getTrongLuong(),
        insertProductDTO.getLoaiSP(),
        insertProductDTO.isTrangThai() ? "Còn bán" : "Ngưng bán",
        insertProductDTO.getHinhAnh(),
        insertProductDTO.getListHinhAnhSP(),
        false)) {
            //khởi tạo LoaiSP
            LoaiSP loaiSP = loaiSPRepository.findByTenLoai(insertProductDTO.getLoaiSP());
            if (loaiSP == null) {
                loaiSP.setTenLoai(insertProductDTO.getLoaiSP()); // Lấy thông tin từ DTO
                loaiSPRepository.saveAndFlush(loaiSP);
            }
            
            // Tạo sản phẩm
            SanPham sanPham = new SanPham();

            sanPham.setTenSanPham(insertProductDTO.getTenSanPham());
            sanPham.setGia(insertProductDTO.getGia());
            sanPham.setTrongLuong(insertProductDTO.getTrongLuong());
            sanPham.setLoaiSP(loaiSP);
            sanPham.setTrangThai(insertProductDTO.isTrangThai());
            sanPham.setHinhAnh(insertProductDTO.getHinhAnh());
            sanPham.setNgayThem(insertProductDTO.getNgayThem());
            sanPham.setMoTa(insertProductDTO.getMoTa());

            System.out.println("Đây là giá sản phẩm:"+insertProductDTO.getGia());
            
            // Lưu tài khoản vào database
            sanPhamRepository.saveAndFlush(sanPham);

            //khởi tạo và add data vào List HinhAnhSP
            for(String hinh: insertProductDTO.getListHinhAnhSP()){
                // Tạo một đối tượng hinhAnhSP mới cho mỗi ảnh
			    HinhAnhSP hinhAnhSP = new HinhAnhSP();
			    hinhAnhSP.setSanPhamHA(sanPham); // Đặt lại giá trị SanPhamHA
			    hinhAnhSP.setTenHinhAnh(hinh);  // Đặt tên cho ảnh

			    // Lưu đối tượng hinhAnhSP vào cơ sở dữ liệu
			    hinhAnhSPRepository.saveAndFlush(hinhAnhSP);
            }

            response.put("status", "Success");
            response.put("data", sanPham);
            response.put("message", "Thêm sản phẩm mới thành công");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        }
        response.put("status", "Failed");
        response.put("data", err);
        response.put("message", "Thêm sản phẩm mới thất bại!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("api/admin/quan-ly-san-pham/update/{maSanPham}")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateProduct(
        @RequestBody InsertProductDTO insertProductDTO,
        @PathVariable("maSanPham") int maSanPham){

            Map<String, Object> response = new HashMap<>();
            SanPham update = sanPhamRepository.findById(maSanPham).get();

            // Kiểm tra nếu người dùng không chọn hình ảnh mới thì sử dụng hình ảnh hiện tại
            if (insertProductDTO.getListHinhAnhSP()==null) {
                List<HinhAnhSP> currentImages = update.getListHinhAnhSP();
                List<String> tenHinh = new ArrayList<>();
                for(HinhAnhSP hinh : update.getListHinhAnhSP()){
                    tenHinh.add(hinh.getTenHinhAnh());
                }
                insertProductDTO.setListHinhAnhSP(tenHinh);
                insertProductDTO.setHinhAnh(tenHinh.getFirst());
            } 

            // Tìm kiếm LoaiSP từ tên loại
		    LoaiSP loaiSP = loaiSPRepository.findByTenLoai(insertProductDTO.getLoaiSP());

            if(validation(insertProductDTO.getTenSanPham(),
                          insertProductDTO.getGia(),
                          insertProductDTO.getTrongLuong(), 
                          insertProductDTO.getLoaiSP(), 
                          insertProductDTO.isTrangThai() ? "Còn bán" : "Ngưng bán", 
                          insertProductDTO.getHinhAnh(), 
                          insertProductDTO.getListHinhAnhSP(), 
                          true)){

                update.setTenSanPham(insertProductDTO.getTenSanPham());
                update.setGia(insertProductDTO.getGia());
                update.setTrongLuong(insertProductDTO.getTrongLuong());
                if (loaiSP == null) {
                    loaiSP.setTenLoai(insertProductDTO.getLoaiSP()); // Lấy thông tin từ DTO
                    loaiSPRepository.saveAndFlush(loaiSP);
                }
                update.setLoaiSP(loaiSP);
                update.setTrangThai(insertProductDTO.isTrangThai());
                update.setMoTa(insertProductDTO.getMoTa());
                update.setHinhAnh(insertProductDTO.getHinhAnh());
                sanPhamRepository.saveAndFlush(update);
                
                //bắt đầu xóa hình cũ
                List<HinhAnhSP> hinhAnhSP = update.getListHinhAnhSP();
                
                for(int i = 0;i<hinhAnhSP.size();i++) {
                    if(hinhAnhSP!=null) {
                        hinhAnhSPRepository.deleteByTenHinhAnh(hinhAnhSP.get(i).getTenHinhAnh());
                    }
                }
                
                
                //thêm hình mới
                for(String hinh : insertProductDTO.getListHinhAnhSP()) {
                    // Tạo một đối tượng hinhAnhSP mới cho mỗi ảnh
                    
                    HinhAnhSP updateHinh = new HinhAnhSP();			    
                    updateHinh.setSanPhamHA(update); // Đặt lại giá trị SanPhamHA
                    updateHinh.setTenHinhAnh(hinh);  // Đặt tên cho ảnh

                    // Lưu đối tượng hinhAnhSP vào cơ sở dữ liệu
                    hinhAnhSPRepository.saveAndFlush(updateHinh);
                }

                response.put("status", "Success");
                response.put("data", update);
                response.put("message", "Cập nhật sản phẩm thành công");

                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            response.put("status", "Failed");
            response.put("data", err);
            response.put("message", "Cập nhật sản phẩm thất bại!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }


    public boolean validation(String productName,double price,double weight,String loaiSP,String status,String coverImg,List<String> list,boolean isUpdate) {
		
		boolean hasError = false;
		
		if(productName.equals("")) {
            err.put("errProductName","Vui lòng nhập tên sản phẩm!");
			hasError = true;
		}
		else if(productName.length()<8) {
			err.put("errProductName","Tên sản phẩm phải nhiều hơn 8 ký tự!");
			hasError = true;
		}
		if(price<0) {
            err.put("errPrice","Giá không được nhỏ hơn 0!");
			hasError = true;
		}
		// if(quantity<0) {
        //     err.put("errQuantity","Số lượng không được nhỏ hơn 0!");
		// 	hasError = true;
		// }
		if(weight<0) {
			err.put("errWeight","Trọng lượng không được nhỏ hơn 0!");
			hasError = true;
		}
		if (loaiSP == null) {
			err.put("errType","Loại sản phẩm không được bỏ trống!");
	        hasError = true;
	    }
		if(coverImg.equals("") && !isUpdate) {
			err.put("errImages","Vui lòng chọn hình sản phẩm!");
	        hasError = true;
		}
        if(list == null && !isUpdate){
            err.put("errImages","Vui lòng chọn hình sản phẩm!");
	        hasError = true;
        }
		
		return !hasError;
	}

}
