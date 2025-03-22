package com.snackviet.api;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.snackviet.dto.taikhoan.ChangePasswordDTO;
import com.snackviet.dto.taikhoan.EditProfileDTO;
import com.snackviet.dto.taikhoan.ForgotPasswordDTO;
import com.snackviet.dto.taikhoan.RegisterDTO;
import com.snackviet.dto.taikhoan.TokenRequest;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.JWTService;
import com.snackviet.service.MaHoaMKService;
import com.snackviet.service.MailService;
import com.snackviet.service.OptCodeService;
import com.snackviet.service.PasswordEncoderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
public class ApiTaiKhoan {
    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    @Autowired
    MailService mailService;
    @Autowired
    OptCodeService optCodeService;
    @Autowired
	JWTService jwtService;
	@Autowired
	AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoderService passwordEncoderService;

    // @GetMapping("/api/tai-khoan/list")
    // public Map<String, Object> getAllTaiKhoan() {
    //     List<TaiKhoan> list = taiKhoanRepository.findAll();

    //     Map<String, Object> response = new HashMap<>();
    //     response.put("status", "success");
    //     response.put("data", Map.of("taikhoans", list));
    //     response.put("message", "Retrieve account data successfully");
    //     return response;
    // }

    // api/taikhoans?tenDangNhap=?&matKhau=?
    @GetMapping("/api/get-tai-khoan")
    public Map<String, Object> getTaiKhoanByTenDangNhapMatKhau(@RequestParam("tenDangNhap") String tenDangNhap,
            @RequestParam("matKhau") String matKhau) {
        TaiKhoan tk = taiKhoanRepository.findByTenDangNhap(tenDangNhap);
        if (tk == null) {
            return response("error", tk, "Tên đăng nhập hoặc mật khẩu không đúng!!!");
        } else if (!tk.isTrangThai()) {
            return response("error", tk, "Tai khoản của bạn đã bị khóa!!!");
        } else {
            System.out.println(passwordEncoderService.encodePassword(matKhau));
            System.out.println("hi"+matKhau.equals(tk.getMatKhau()) );
            System.out.println(passwordEncoderService.matches(matKhau, tk.getMatKhau()));
            if ( matKhau.equals(tk.getMatKhau()) || passwordEncoderService.matches(matKhau, tk.getMatKhau())) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(tk.getTenDangNhap(),
                        matKhau));
                if (authentication.isAuthenticated()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("data", Map.of("taiKhoan", tk));
                    String token = jwtService.GenerateToken(tk.getTenDangNhap(), map);
                    System.out.println("Generated Token: " + token); // Debugging log
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("token",token);
                    response.put("message","Đăng nhập thành công");
                    return response;
                } else {
                    System.out.println("do lỗi nè");
                    throw new UsernameNotFoundException("Invalid user request..!!");

                }
            } else {
                return response("error", new TaiKhoan(), "Tên đăng nhập hoặc mật khẩu không đúng!!!");
            }
        }

    }

    @PostMapping("/api/tai-khoan/create")
    public Map<String, Object> postTaiKhoan(@RequestBody RegisterDTO registerDTO) {
        if (taiKhoanRepository.findByTenDangNhap(registerDTO.getTenDangNhap()) != null) {
            return response("error", new TaiKhoan(), "Tên đăng nhập đã tồn tại!!!");
        } else if (taiKhoanRepository.findByEmail(registerDTO.getEmail()) != null) {
            return response("error", new TaiKhoan(), "Email đã tồn tại!!!");
        } else if (taiKhoanRepository.findBySoDienThoaiContaining(registerDTO.getSoDienThoai()) != null) {
            return response("error", new TaiKhoan(), "Số điện thoại đã tồn tại!!!");
        } else {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setEmail(registerDTO.getEmail());
            taiKhoan.setHoVaTen(registerDTO.getHoVaTen());
            taiKhoan.setTenDangNhap(registerDTO.getTenDangNhap());
            taiKhoan.setMatKhau(MaHoaMKService.maHoaMatKhauMD5(registerDTO.getMatKhau()));
            taiKhoan.setSoDienThoai(registerDTO.getSoDienThoai());
            taiKhoanRepository.saveAndFlush(taiKhoan);
            return response("success", taiKhoan, "Đăng ký tài khoản thành công");
        }
    }

    @PutMapping("/api/tai-khoan/edit-profile")
    public Map<String, Object> updateTaiKhoan(@RequestBody EditProfileDTO editProfileDTO) {
        TaiKhoan tkByTenDangNhap = taiKhoanRepository.findByTenDangNhap(editProfileDTO.getTenDangNhap());
        if (!editProfileDTO.getEmail().equals(tkByTenDangNhap.getEmail())
                && taiKhoanRepository.findByEmail(editProfileDTO.getEmail()) != null) {
            return response("error", new TaiKhoan(), "Email đã tồn tại!!!");
        } else if (!editProfileDTO.getSoDienThoai().equals(tkByTenDangNhap.getSoDienThoai())
                && taiKhoanRepository.findBySoDienThoaiContaining(editProfileDTO.getSoDienThoai()) != null) {
            return response("error", new TaiKhoan(), "Số điệnt thoại đã tồn tại!!!");
        } else {
            tkByTenDangNhap.setHoVaTen(editProfileDTO.getHoVaTen());
            tkByTenDangNhap.setEmail(editProfileDTO.getEmail());
            tkByTenDangNhap.setSoDienThoai(editProfileDTO.getSoDienThoai());
            tkByTenDangNhap.setGioiTinh(editProfileDTO.getGioiTinh());
            System.out.println(tkByTenDangNhap.isGioiTinh());
            System.out.println(editProfileDTO.getGioiTinh());
            tkByTenDangNhap.setHinhAnh(editProfileDTO.getHinhAnh());
            taiKhoanRepository.saveAndFlush(tkByTenDangNhap);
            return response("success", tkByTenDangNhap, "Cập nhật tài khoản thành công");
        }
    }

    @PutMapping("/api/tai-khoan/change-password")
    public Map<String, Object> putMethodName(@RequestBody ChangePasswordDTO changePasswordDTO) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(changePasswordDTO.getTenDangNhap());
        if (!passwordEncoderService.matches(changePasswordDTO.getMatKhau(), taiKhoan.getMatKhau())) {
            return response("error", new TaiKhoan(), "Mật khẩu cũ không chính xác!!!");
        } else {
            System.out.println(changePasswordDTO.getChangeMatKhau());
            taiKhoan.setMatKhau(MaHoaMKService.maHoaMatKhauMD5(changePasswordDTO.getChangeMatKhau()));
            taiKhoanRepository.saveAndFlush(taiKhoan);
            return response("success", taiKhoan, "Đổi mật khẩu thành công");
        }

    }

    @PostMapping("/api/tai-khoan/forgot-password/send-otp")
    public Map<String, Object> sendOtp(@RequestParam("email") String email) {
        Map<String, Object> response = new HashMap<>();
        int code = optCodeService.generateCode();
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email);
        if (taiKhoan == null) {
            return response("error", taiKhoan, "Email không tồn tại!!!");
        } else {
            if (mailService.sendOtp(email, "Forgot Password", "Your verification code OTP is " + code)) {
                response.put("status", "success");
                response.put("data", Map.of("email", email, "code", MaHoaMKService.maHoaMatKhauMD5(String.valueOf(code))));
                response.put("message", "Đã gửi mã xác nhận");
                return response;
            } else {
                return response("error", new TaiKhoan(), "Gửi mã xác nhận không thành công!!!");
            }

        }
    }

    @PostMapping("/api/tai-khoan/forgot-password/change")
    public Map<String, Object> sendOtp(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        Integer code = optCodeService.getCode();
        if (code == null) {
            return response("errorr", null, "Mã xác nhận đã hết hạn");
        } else if (!code.equals(forgotPasswordDTO.getMaXacNhan())) {
            return response("error", null, "Mã xác nhận không đúng!!!");
        } else {
            TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(forgotPasswordDTO.getEmail());
            taiKhoan.setMatKhau(MaHoaMKService.maHoaMatKhauMD5(forgotPasswordDTO.getMatKhau()));
            taiKhoanRepository.saveAndFlush(taiKhoan);
            return response("success", taiKhoan, "Đổi mật khẩu thành công");
        }
    }

    @GetMapping("/api/lien-he")
    public Map<String, Object> guiLienHe(@RequestParam("hoVaTen") String hoVaTen, @RequestParam("email") String email, @RequestParam("tinNhan")String tinNhan) {
        Map<String, Object> response = new HashMap<>();
        if (mailService.sendOtp(email,"Khách hàng: " +hoVaTen + " liên hệ", "Nội dung: " + tinNhan + "\nEmail: " + email)) {
            response.put("status", "success");
            response.put("message", "Gửi thông tin liên hệ thành công");
            return response;
        } else {
            response.put("status", "error");
            response.put("message", "Gửi thông tin liên hệ thất bại");
            return response;
        }
    }
        


    public Map<String, Object> response(String status, TaiKhoan taiKhoan, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("data", taiKhoan == null ? null : Map.of("taiKhoan", taiKhoan));
        response.put("message", message);
        return response;
    }

    @PostMapping("api/tai-khoan/auth/google")
    public Map<String, Object> googleLogin(@RequestBody TokenRequest tokenRequest){
        String tokenId = tokenRequest.getTokenId();
        try {
            // Thiết lập GoogleIdTokenVerifier
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("68991665224-48e0o2dcf45bo7hgllage70huupt308l.apps.googleusercontent.com"))
                    .build();

            // Xác thực token
            GoogleIdToken idToken = verifier.verify(tokenId);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Lấy thông tin người dùng từ payload
                String userId = payload.getSubject();
                String email = payload.getEmail();
                boolean emailVerified = payload.getEmailVerified();
                String name = (String) payload.get("name");
                String avatar = (String) payload.get("picture");

                TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email);
                if(taiKhoan == null) {
                    int atIndex = email.indexOf('@');
                    String emailDaCat = atIndex != -1? email.substring(0, atIndex):email;
                    Random random = new Random();
                    int newCode = 100000 + random.nextInt(900000);
                    String randomString = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    
                    TaiKhoan tk = new TaiKhoan();
                    tk.setEmail(email);
                    tk.setHoVaTen(name);
                    tk.setHinhAnh(avatar);
                    tk.setTenDangNhap(email);
                    // tk.setTenDangNhap(emailDaCat + newCode);
                    tk.setMatKhau(MaHoaMKService.maHoaMatKhauMD5(randomString));
                    tk.setVaiTro(false);
                    taiKhoanRepository.saveAndFlush(tk);

                    Map<String, Object> map = new HashMap<>();
                    map.put("data", Map.of("taiKhoan", tk));
                    String token = jwtService.GenerateToken(tk.getTenDangNhap(), map);
                    System.out.println("Generated Token: " + token); // Debugging log
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("token",token);
                    response.put("message","Đăng nhập thành công");
                    return response;
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("data", Map.of("taiKhoan", taiKhoan));
                    String token = jwtService.GenerateToken(taiKhoan.getTenDangNhap(), map);
                    System.out.println("Generated Token: " + token); // Debugging log
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("token",token);
                    response.put("message","Đăng nhập thành công");
                    return response;
                }              
                // // Tiếp tục xử lý, như tạo session hoặc JWT token cho người dùng
                // Map<String, Object> response = new HashMap<>();
                // response.put("status", "success");
                // response.put("data",  Map.of("userId", userId, "email", email, "name", name, "emailVerified", emailVerified, "payload", payload,"emailDaCat", emailDaCat+newCode, "randomString", randomString));
                // response.put("message", "message");
                // return response;
            } else {
                return null;
            }

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("data",  null);
            response.put("message", e.getMessage());
            return response;
            
        }
    }
}
