package com.snackviet.api;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.configuration.VNPayConfig;
import com.snackviet.model.ChiTietHoaDon;
import com.snackviet.model.DiaChi;
import com.snackviet.model.HoaDon;
import com.snackviet.model.TaiKhoan;
import com.snackviet.model.TrangThaiHoaDon;
import com.snackviet.repository.ChiTietGioHangRepository;
import com.snackviet.repository.ChiTietHoaDonRepository;
import com.snackviet.repository.DiaChiRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.repository.TrangThaiHoaDonRepository;
import com.snackviet.service.SessionService;
import com.snackviet.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RestController
public class ApiVnPayOnline {
@Autowired
    SessionService sessionService;
    @Autowired
    DiaChiRepository diaChiRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    @Autowired
    TrangThaiHoaDonRepository trangThaiHoaDonRepository;
    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Autowired
    ChiTietGioHangRepository chiTietGioHangRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    SanPhamRepository sanPhamRepository;

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.url}")
    private String vnp_Url;

    @Value("${vnpay.returnUrl}")
    private String vnp_Returnurl;
    @Autowired
    HttpServletRequest request;

    @PostMapping("/api/vnpay/create-payment")
    public Map<String, String> createPayment(@RequestBody Map<String, Object> orderData) throws Exception {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        String vnp_IpAddr = "127.0.0.1";
        long amount = (long) ((int) orderData.get("tongTien") * 100);

        SortedMap<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", String.valueOf( (int) orderData.get("orderId") + 1));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + orderData.get("orderId"));
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_ReturnUrl", String.valueOf(orderData.get("urlReturn")));
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = now.format(formatter);
        ZonedDateTime expireTime = now.plusMinutes(15);
        String vnp_ExpireDate = expireTime.format(formatter);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Generate payment URL
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            if (query.length() > 0) {
                query.append("&");
            }
            query.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        String secureHash = VNPayService.hmacSHA512(vnp_HashSecret, query.toString());
        query.append("&vnp_SecureHash=").append(secureHash);

        String paymentUrl = vnp_Url + "?" + query.toString();

        Map<String, String> response = new HashMap<>();
        response.put("paymentUrl", paymentUrl);

        System.out.println("PaymentUrl: " + paymentUrl);

        return response;
    }

    Map<String,Object> dataCTHD = new HashMap<>();
    
    @PostMapping("/api/vnpay/save-session-data")
    public ResponseEntity<?> saveSession(@RequestBody Map<String, Object> sessionData){
        try {
            double phiVanChuyen = Double.parseDouble(sessionData.get("phiVanChuyen").toString());
            TaiKhoan tk  = taiKhoanRepository.findById((int) sessionData.get(("maTaiKhoan"))).get();

            // Lưu vào session
            sessionService.setAttribute("phiVanChuyen", phiVanChuyen);
            sessionService.setAttribute("sessionService", tk);

           dataCTHD.put("soLuong", sessionData.get("dataCTHD"));
            
           dataCTHD = (Map<String, Object>) sessionData.get("dataCTHD");

           System.out.println("Đây là mã sản phẩm: "+dataCTHD.get("maSanPham"));
            return ResponseEntity.ok("Session data saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving session data");
        }
    }

    int count = 0;

    @PostMapping("/api/vnpay/save-order")
    public ResponseEntity<?> saveOrder(@RequestBody Map<String, Object> requestParams) {
        count++;
        // Lấy thông tin trả về từ VNPAY
        String vnp_ResponseCode = (String) requestParams.get("vnp_ResponseCode");
        String vnp_Amount = (String) requestParams.get("vnp_Amount");
        String vnp_TxnRef = (String) requestParams.get("vnp_TxnRef");
        String vnp_SecureHash = (String) requestParams.get("vnp_SecureHash");

        Integer maTaiKhoan = (Integer) requestParams.get("maTaiKhoan");
        long shipCost = Long.parseLong(requestParams.get("phiVanChuyen").toString());

        sessionService.setAttribute("phiVanChuyen", shipCost);


        // Các tham số khác nếu cần

        // Kiểm tra tính hợp lệ của vnp_SecureHash và vnp_ResponseCode
        if ("00".equals(vnp_ResponseCode)) {

            

            // Thanh toán thành công
            double total = Double.valueOf(vnp_Amount) / 100; // Chuyển đổi số tiền về đơn vị tiền tệ
            TaiKhoan taiKhoan = taiKhoanRepository.findById(maTaiKhoan).get();

            // Lấy các thông tin cần thiết từ session
            String note = sessionService.getAttribute("note");

            long phiVanChuyen = (long) sessionService.getAttribute("phiVanChuyen");

            boolean phuongThucThanhToan = false; // Chắc chắn đây là thanh toán online

            // Lấy địa chỉ và trạng thái hóa đơn
            List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
            DiaChi diaChi = listDiaChi.stream().filter(DiaChi::isTrangThai).findFirst().orElse(null);
            // UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // TaiKhoan get = taiKhoanRepository.findByTenDangNhap(userDetails.getUsername());
            // DiaChi dc = diaChiRepository.findByTaiKhoanDCAndTrangThai(taiKhoan, true);

            TrangThaiHoaDon trangThaiHoaDon = trangThaiHoaDonRepository.findAll()
                    .stream().filter(tthd -> tthd.getTenTrangThai().equals("Đã đặt hàng")).findFirst().orElse(null);

            // Tạo và lưu hóa đơn
            HoaDon hoaDon = new HoaDon();
            hoaDon.setDiaChiNhan(diaChi.getDiaChi()+", "+diaChi.getDiaChiChiTiet());
            hoaDon.setDcHoTen(diaChi.getHoVaTen());
            hoaDon.setDcSoDienThoai(diaChi.getSoDienThoai());
            hoaDon.setTongTien(total);
            hoaDon.setPhiVanChuyen(phiVanChuyen);
            hoaDon.setTaiKhoanHD(taiKhoan);
            hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
            hoaDon.setTrangThai(!phuongThucThanhToan);
            hoaDon.setGhiChu(note==null ? "Không có" : note);
            hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
            hoaDonRepository.saveAndFlush(hoaDon);
            
            // Lấy danh sách mã sản phẩm và số lượng
            List<Integer> maSanPhamList = (List<Integer>) dataCTHD.get("maSanPham");
            List<Integer> soLuongList = (List<Integer>) dataCTHD.get("soLuong");

            // Kiểm tra nếu hai danh sách có kích thước không khớp
            if (maSanPhamList == null || soLuongList == null || maSanPhamList.size() != soLuongList.size()) {
                throw new RuntimeException("Danh sách mã sản phẩm và số lượng không khớp!");
            }

            // Lặp qua từng cặp mã sản phẩm và số lượng
            for (int i = 0; i < maSanPhamList.size(); i++) {
                Integer maSanPham = maSanPhamList.get(i);
                Integer soLuong = soLuongList.get(i);

                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
                chiTietHoaDon.setHoaDonCT(hoaDon);

                // Tìm sản phẩm theo mã sản phẩm
                chiTietHoaDon.setSanPhamCT(sanPhamRepository.findById(maSanPham).orElseThrow(() ->
                    new RuntimeException("Không tìm thấy sản phẩm với mã: " + maSanPham)
                ));
                System.out.println("Đây là mã sản phẩm: " + maSanPham);

                // Gán số lượng tương ứng
                chiTietHoaDon.setSoLuong(soLuong);

                // Lưu bản ghi vào cơ sở dữ liệu
                chiTietHoaDonRepository.saveAndFlush(chiTietHoaDon);
            }

            //Thực hiện xóa khỏi giỏ hàng
            List<Integer> maCTGHList = (List<Integer>) requestParams.get("maCTGH");
            for(int i = 0; i < maCTGHList.size(); i++){
                chiTietGioHangRepository.deleteById(maCTGHList.get(i));
            }
              
            return ResponseEntity.ok(hoaDon);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed");
        }
    }

    @GetMapping("/api/vnpay/get-ma-hd")
    public Map<String,Object> getMaHD(){
        Map<String,Object> response = new HashMap<>();
        response.put("status","Success");
        response.put("maHD", hoaDonRepository.findMaxMaHoaDon());
        response.put("message","Lấy mã hóa đơn thành công!");
        return response;
    }
}
