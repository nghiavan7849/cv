package com.snackviet.api.Admin;

import java.util.Optional;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.storage.Acl.User;
import com.snackviet.dto.taikhoan.InsertUserDTO;
import com.snackviet.model.HoaDon;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.CookieService;
import com.snackviet.service.MaHoaMKService;
import com.snackviet.service.MailService;
import com.snackviet.service.SessionService;

@RestController
@CrossOrigin("*")
public class ApiAdminUser {

    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    MailService mailService;
    @Autowired
    SessionService sessionService;


    Map<String, String> err = new HashMap<>();

    @GetMapping("api/admin/quan-ly-nguoi-dung")
    public Map<String, Object> getDisplay(@RequestParam("pageNo") Optional<Integer> pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "sort", defaultValue = "") String sort) {

        Map<String, Object> cards = new HashMap<>();
        // lấy số người dùng bị khóa
        cards.put("inactive", taiKhoanRepository.findByTrangThai(false).size());
        // lấy tổng số hóa đơn
        cards.put("total", hoaDonRepository.count());

        // PHÂN TRANG VÀ TÌM KIẾM
        int currentPage = pageNo.orElse(1);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<TaiKhoan> userPage = null;

        //lấy thông tin người dùng đăng nhập
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaiKhoan get = taiKhoanRepository.findByTenDangNhap(userDetails.getUsername());
        int maTK = get.getMaTaiKhoan();
        
        

        if (!search.isEmpty()) {
            // Lọc dữ liệu theo cả search và sort
            if (sort.equals("Tên tài khoản")) {
                userPage = taiKhoanRepository.findByTenDangNhapContaining(search, pageable);
            } else if (sort.equals("Số điện thoại")) {
                userPage = taiKhoanRepository.findBySoDienThoaiContaining(search, pageable);
            } else if (sort.equals("Email")) {
                userPage = taiKhoanRepository.findByEmailContaining(search, pageable);
            } else if (sort.equals("Họ và tên")) {
                userPage = taiKhoanRepository.findByHoVaTenContaining(search, pageable);
            }
        } else {
            // Không lọc dữ liệu
            userPage = taiKhoanRepository.findAllByVaiTroNotAndMaTaiKhoanNotOrderByMaTaiKhoanDesc(true, maTK, pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Success");
        response.put("data", Map.of(
                "cards", cards,
                "editting", false));
        response.put("message", "Lấy dữ liệu TaiKhoan thành công");

        // Thêm giá trị mới vào "data"
        Map<String, Object> data = new HashMap<>((Map<String, Object>) response.get("data"));

        // Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về
        // admin/quanLyNguoiDung
        if (userPage == null || userPage.isEmpty()) {
            data.put("users", Collections.emptyList());
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

        data.put("users", userPage.getContent());
        data.put("pagination", Map.of(
                "totalItems", userPage.getTotalElements(),
                "totalPages", userPage.getTotalPages(),
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

        // Cập nhật lại "data" vào trong response
        response.put("data", data);

        return response;

    }

    @GetMapping("/api/admin/quan-ly-nguoi-dung/{maTaiKhoan}")
    public Map<String, Object> getDetail(Model model, @PathVariable("maTaiKhoan") int maTaiKhoan,
            @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "sort", defaultValue = "") String sort) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        // set lại trạng thái cho biến editting
        data.put("editting", true);
        response.put("status", "Success");

        // Bắt lỗi tìm với id nằm ngoài dữ liệu
        Optional<TaiKhoan> editOptional = taiKhoanRepository.findById(maTaiKhoan);
        TaiKhoan edit = editOptional.get();
        // đổ dữ liệu cho bảng chi tiết
        data.put("displayU", edit);
        data.put("displayUser", taiKhoanRepository.findById(maTaiKhoan).get());

        response.put("data", data);
        response.put("message", "Lấy dữ liệu hóa đơn được chọn thành công");
        return response;

    }

    @PostMapping("api/admin/quan-ly-nguoi-dung/create")
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestBody InsertUserDTO insertUserDTO) {

        Map<String, Object> response = new HashMap<>();

        System.out.println("Đây là PASS: " + insertUserDTO.getMatKhau());

        if (validation(insertUserDTO.getHoVaTen(),
                insertUserDTO.getTenDangNhap(),
                insertUserDTO.getEmail(),
                insertUserDTO.isVaiTro(),
                insertUserDTO.isTrangThai(),
                insertUserDTO.getSoDienThoai(),
                insertUserDTO.isGioiTinh())) {

            // Kiểm tra dữ liệu unique
            if (taiKhoanRepository.findByTenDangNhap(insertUserDTO.getTenDangNhap()) != null) {
                response.put("status", "Failed");
                response.put("message", "Tên đăng nhập đã tồn tại");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else if (taiKhoanRepository.findByEmail(insertUserDTO.getEmail()) != null) {
                response.put("status", "Failed");
                response.put("message", "Email đã tồn tại");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            } else if (taiKhoanRepository.findBySoDienThoaiContaining(insertUserDTO.getSoDienThoai()) != null) {
                response.put("status", "Failed");
                response.put("message", "Số điện thoại đã tồn tại");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Tạo tài khoản người dùng
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setHoVaTen(insertUserDTO.getHoVaTen());
            taiKhoan.setTenDangNhap(insertUserDTO.getTenDangNhap());
            // gửi mật khẩu tới mail người dùng
            taiKhoan.setMatKhau(MaHoaMKService.maHoaMatKhauMD5(insertUserDTO.getMatKhau()));
            mailService.sendNewAdmin(insertUserDTO.getEmail(), insertUserDTO.getMatKhau());

            taiKhoan.setEmail(insertUserDTO.getEmail());
            taiKhoan.setVaiTro(insertUserDTO.isVaiTro());
            taiKhoan.setTrangThai(insertUserDTO.isTrangThai());
            taiKhoan.setSoDienThoai(insertUserDTO.getSoDienThoai());
            taiKhoan.setGioiTinh(insertUserDTO.isGioiTinh());

            // Lưu tài khoản vào database
            taiKhoanRepository.saveAndFlush(taiKhoan);

            response.put("status", "Success");
            response.put("data", taiKhoan);
            response.put("message", "Thêm người dùng mới thành công");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        response.put("status", "Failed");
        response.put("data", err);
        response.put("message", "Thêm người dùng mới thất bại!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("api/admin/quan-ly-nguoi-dung/update/{maTaiKhoan}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @RequestBody InsertUserDTO insertUserDTO,
            @PathVariable("maTaiKhoan") int maTaiKhoan ) {

        Map<String, Object> response = new HashMap<>();

        // Lấy tài khoản người dùng
        TaiKhoan taiKhoan = taiKhoanRepository.findById(maTaiKhoan).get();

        if (validation(insertUserDTO.getHoVaTen(),
                insertUserDTO.getTenDangNhap(),
                insertUserDTO.getEmail(),
                insertUserDTO.isVaiTro(),
                insertUserDTO.isTrangThai(),
                insertUserDTO.getSoDienThoai(),
                insertUserDTO.isGioiTinh())) {

            // Kiểm tra dữ liệu unique
            // if (taiKhoanRepository.findByTenDangNhap(insertUserDTO.getTenDangNhap()) != null) {
            //     response.put("status", "Failed");
            //     response.put("message", "Tên đăng nhập đã tồn tại");
            //     return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            // } else if (taiKhoanRepository.findByEmail(insertUserDTO.getEmail()) != null) {
            //     response.put("status", "Failed");
            //     response.put("message", "Email đã tồn tại");
            //     return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            // } else if (taiKhoanRepository.findBySoDienThoaiContaining(insertUserDTO.getSoDienThoai()) != null) {
            //     response.put("status", "Failed");
            //     response.put("message", "Số điện thoại đã tồn tại");
            //     return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            // }


            taiKhoan.setHoVaTen(insertUserDTO.getHoVaTen());
            taiKhoan.setVaiTro(insertUserDTO.isVaiTro());
            taiKhoan.setTrangThai(insertUserDTO.isTrangThai());
            taiKhoan.setGioiTinh(insertUserDTO.isGioiTinh());

            // Lưu tài khoản vào database
            taiKhoanRepository.saveAndFlush(taiKhoan);

            response.put("status", "Success");
            response.put("data", taiKhoan);
            response.put("message", "Cập nhật người dùng thành công");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        response.put("status", "Failed");
        response.put("data", err);
        response.put("message", "Cập nhật người dùng thất bại!");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public boolean validation(String fullname, String username, String email, boolean role, boolean status,
            String phone, boolean gender) {

        boolean hasError = false;
        String patternSDT = "^(0[3-9])\\d{8}$";

        if (fullname.equals("")) {
            err.put("errName", "Vui lòng nhập họ và tên!");
            hasError = true;
        }
        if (username.equals("")) {
            err.put("errUsername", "Vui lòng nhập tên đăng nhập!");
            hasError = true;
        }
        if (email.equals("")) {
            err.put("errEmail", "Vui lòng nhập email!");
            hasError = true;
        }
        if (phone.equals("")) {
            err.put("errPhone", "Vui lòng nhập số điện thoại!");
            hasError = true;
        } else if (phone.trim().length() < 10) {
            err.put("errPhone", "Số điện thoại phải có 10 số!");
            hasError = true;
        } else if (!phone.matches(patternSDT)) {
            err.put("errPhone", "Số điện thoại không đúng định dạng!");
            hasError = true;
        }
        // if(image.equals("")) {
        // err.put("errImg", "Vui lòng chọn ảnh đại diện!");
        // hasError = true;
        // }

        return !hasError;
    }

}
