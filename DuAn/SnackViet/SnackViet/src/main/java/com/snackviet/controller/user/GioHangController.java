package com.snackviet.controller.user;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.configuration.VNPayConfig;
import com.snackviet.model.ChiTietGioHang;
import com.snackviet.model.ChiTietHoaDon;
import com.snackviet.model.DiaChi;
import com.snackviet.model.HoaDon;
import com.snackviet.model.PaymentResDTO;
import com.snackviet.model.SanPham;
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

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class GioHangController {
	@Autowired
	SanPhamRepository sanPhamRepository;
	@Autowired
	ChiTietGioHangRepository chiTietGioHangRepository;
	@Autowired
	ChiTietHoaDonRepository chiTietHoaDonRepository;
	@Autowired
	DiaChiRepository diaChiRepository;
	@Autowired
	HoaDonRepository hoaDonRepository;
	@Autowired
	TrangThaiHoaDonRepository trangThaiHoaDonRepository;
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	@Autowired
	SessionService sessionService;
	@Autowired
	HttpServletRequest request;
	
	boolean isBoughtFromCart = false;

	@PostMapping("chi-tiet-san-pham/them-gio-hang/{id}")
	public String themVaoGioHang(Model model, @PathVariable("id") Integer id, @RequestParam("soLuong") int soLuong) {
		SanPham sanPham = sanPhamRepository.findById(id).get();
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		if (taiKhoan == null) {
			return "redirect:/dang-nhap";
		}
		ChiTietGioHang chiTietGioHang = chiTietGioHangRepository.findBySanPhamGHAndTaiKhoanGH(sanPham, taiKhoan);
		if (chiTietGioHang == null) {
			chiTietGioHang = new ChiTietGioHang();
			chiTietGioHang.setSanPhamGH(sanPham);
			chiTietGioHang.setTaiKhoanGH(taiKhoan);
			chiTietGioHang.setSoLuong(soLuong);
		} else {
			chiTietGioHang.setSoLuong(chiTietGioHang.getSoLuong() + soLuong);
		}
		chiTietGioHangRepository.saveAndFlush(chiTietGioHang);
		return "redirect:/chi-tiet-san-pham/" + id;
	}

	@RequestMapping("gio-hang")
	public String gioHang(Model model) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		List<ChiTietGioHang> listCTGH = chiTietGioHangRepository.findByTaiKhoanGH(taiKhoan,
				Sort.by(Direction.DESC, "maChiTietGioHang"));
		double totalGioHang = 0;
		for (ChiTietGioHang chiTietGioHang : listCTGH) {
			totalGioHang += chiTietGioHang.getSanPhamGH().getGia() * chiTietGioHang.getSoLuong();
		}
		
		List<DiaChi> listdiaChis = diaChiRepository.findByTaiKhoanDC(taiKhoan);
		System.out.println(listdiaChis);
		model.addAttribute("listdiaChis", listdiaChis.isEmpty()?"empty":"no empty");
		model.addAttribute("listCTGH", listCTGH);
		model.addAttribute("totalGioHang", totalGioHang);
		return "user/giohang";
	}

	@GetMapping("gio-hang/remove/{id}")
	public String removeSP(Model model, @PathVariable("id") Integer id) {
		ChiTietGioHang chiTietGioHang = chiTietGioHangRepository.findById(id).get();
		chiTietGioHangRepository.delete(chiTietGioHang);
		return "redirect:/gio-hang";
	}

	@PostMapping("gio-hang/thay-doi-so-luong/{id}")
	public String thayDoiSL() {
		return "";
	}

	@GetMapping("gio-hang/tang-so-luong/{id}")
	public String tangSL(Model model, @PathVariable("id") Integer id) {
		ChiTietGioHang chiTietGioHang = chiTietGioHangRepository.findById(id).get();
		chiTietGioHang.setSoLuong(chiTietGioHang.getSoLuong() + 1);

		chiTietGioHangRepository.saveAndFlush(chiTietGioHang);
		return "redirect:/gio-hang";
	}

	@GetMapping("gio-hang/giam-so-luong/{id}")
	public String giamSL(Model model, @PathVariable("id") Integer id) {
		ChiTietGioHang chiTietGioHang = chiTietGioHangRepository.findById(id).get();
		if (chiTietGioHang.getSoLuong() > 1) {
			chiTietGioHang.setSoLuong(chiTietGioHang.getSoLuong() - 1);
			chiTietGioHangRepository.saveAndFlush(chiTietGioHang);
		}
		return "redirect:/gio-hang";
	}

	@GetMapping("gio-hang/xoa-tat-ca")
	public String xoaTatCa(Model model) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		List<ChiTietGioHang> lisChiTietGioHangs = chiTietGioHangRepository.findByTaiKhoanGH(taiKhoan);
		for (ChiTietGioHang chiTietGioHang : lisChiTietGioHangs) {
			chiTietGioHangRepository.delete(chiTietGioHang);
		}
		return "redirect:/gio-hang";
	}

	// Khi đặt hàng bằng mua hàng trong giỏ hàng
	@GetMapping("gio-hang/dat-hang")
	public String datHang(Model model, @RequestParam(name = "idGH", defaultValue = "") String id) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		List<ChiTietGioHang> list = new ArrayList<ChiTietGioHang>();
		if (!id.isEmpty()) {
			String[] ids = id.split(",");
			for (String string : ids) {
				ChiTietGioHang chiTietGioHang = chiTietGioHangRepository.findById(Integer.valueOf(string)).get();
				list.add(chiTietGioHang);
			}
		}
		
		//cho list CTGH vào session
		sessionService.setAttribute("listCTGH", list);

		double total = 0;
		double totalWeight = 0;
		for (ChiTietGioHang ctgh : list) {
			total += ctgh.getSoLuong() * ctgh.getSanPhamGH().getGia();
			totalWeight += ctgh.getSanPhamGH().getTrongLuong() * ctgh.getSoLuong();
		}

		List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
		DiaChi diaChi = new DiaChi();
		for (DiaChi dc : listDiaChi) {
			if (dc.isTrangThai()) {
				diaChi = dc;
				break;
			}
		}

		model.addAttribute("totalWeight", totalWeight);
		model.addAttribute("total", total);
		model.addAttribute("listChiTietGioHang", list);
		model.addAttribute("diaChi", diaChi);

		return "user/thongtinthanhtoan";
	}

	@GetMapping("gio-hang/dat-hang/xac-nhan")
	public String datHang(Model model, @RequestParam(name = "idGH", defaultValue = "") String id,
			@RequestParam("phivanchuyen") String phiVanChuyen,
			@RequestParam(name = "note", defaultValue = "") String note,
			@RequestParam("phuongthucthanhtoan") boolean phuongThucThanhToan) {

		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		List<ChiTietGioHang> list = new ArrayList<ChiTietGioHang>();
		if (!id.isEmpty()) {
			String[] ids = id.split(",");
			for (String string : ids) {
				ChiTietGioHang chiTietGioHang = chiTietGioHangRepository
						.findByMaChiTietGioHangAndTaiKhoanGH(Integer.valueOf(string), taiKhoan);
				list.add(chiTietGioHang);
			}
		}

		double total = 0;
		double totalWeight = 0;
		for (ChiTietGioHang ctgh : list) {
			total += ctgh.getSoLuong() * ctgh.getSanPhamGH().getGia();
			totalWeight += ctgh.getSanPhamGH().getTrongLuong() * ctgh.getSoLuong();
		}

		// Đưa thông tin sản phẩm và phí vận chuyển vào session
	    sessionService.setAttribute("total", total);
	    sessionService.setAttribute("phiVanChuyen", phiVanChuyen);
	    sessionService.setAttribute("note", note);
	    //đưa vào một biến boolean để phân giữa trường hợp mua từ giỏ hàng / mua ngay
	  	isBoughtFromCart = true;
	    
	    if(phuongThucThanhToan) {
	    	List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
			DiaChi diaChi = new DiaChi();
			for (DiaChi dc : listDiaChi) {
				if (dc.isTrangThai()) {
					diaChi = dc;
					break;
				}
			}
			TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
			List<TrangThaiHoaDon> listTTHD = trangThaiHoaDonRepository.findAll();
			System.out.println(list);
			for (TrangThaiHoaDon tthd : listTTHD) {
				if (tthd.getTenTrangThai().equals("Đã đặt hàng")) {
					trangThaiHoaDon = tthd;
				}
			}

			HoaDon hoaDon = new HoaDon();
			hoaDon.setDiaChi(diaChi.getDiaChi());
			hoaDon.setTongTien(total);
			hoaDon.setPhiVanChuyen(Double.valueOf(phiVanChuyen));
			hoaDon.setTaiKhoanHD(taiKhoan);
			hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
			hoaDon.setGhiChu(note);
			hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
			hoaDonRepository.saveAndFlush(hoaDon);

			for (ChiTietGioHang ctgh : list) {
				ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
				chiTietHoaDon.setHoaDonCT(hoaDon);
				chiTietHoaDon.setSanPhamCT(ctgh.getSanPhamGH());
				chiTietHoaDon.setSoLuong(ctgh.getSoLuong());
				chiTietHoaDonRepository.saveAndFlush(chiTietHoaDon);
				chiTietGioHangRepository.delete(ctgh);

			}

			return "redirect:/gio-hang/dat-hang/thanh-cong?id=" + hoaDon.getMaHoaDon();
	    }
	    else {
	    	String orderType = "other";
	        long amount = (long) ((total + Double.valueOf(phiVanChuyen)) * 100); // Chuyển đổi thành tiền tệ của VNPay (VD: VNĐ)

	        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
	        String vnp_IpAddr = "127.0.0.1";

	        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

	        Map<String, String> vnp_Params = new HashMap<>();
	        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
	        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
	        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
	        vnp_Params.put("vnp_Amount", String.valueOf(amount));
	        vnp_Params.put("vnp_CurrCode", "VND");
	        vnp_Params.put("vnp_BankCode", "NCB");

	        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
	        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
	        vnp_Params.put("vnp_Locale", "vn");
	        vnp_Params.put("vnp_OrderType", orderType);
	        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
	        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

	        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	        String vnp_CreateDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

	        cld.add(Calendar.MINUTE, 15);
	        String vnp_ExpireDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

	        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
	        Collections.sort(fieldNames);
	        StringBuilder hashData = new StringBuilder();
	        StringBuilder query = new StringBuilder();
	        Iterator<String> itr = fieldNames.iterator();
	        while (itr.hasNext()) {
	            String fieldName = itr.next();
	            String fieldValue = vnp_Params.get(fieldName);
	            if ((fieldValue != null) && (fieldValue.length() > 0)) {
	                hashData.append(fieldName);
	                hashData.append('=');
	                try {
	                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
	                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
	                    query.append('=');
	                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
	                } catch (Exception e) {
	                    System.out.println(e);
	                }
	                if (itr.hasNext()) {
	                    query.append('&');
	                    hashData.append('&');
	                }
	            }
	        }
	        String queryUrl = query.toString();
	        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
	        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
	        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

	        return "redirect:" + paymentUrl;
	    }
		
		
	}

	// Khi mua hàng ở ngoài giỏ hàng
	@GetMapping("gio-hang/mua-hang/dat-hang")
	public String datHangKhiNhanMuaHang(Model model, @RequestParam(name = "idSP", defaultValue = "") String id) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		double total = 0;
		double totalWeight = 0;
		List<ChiTietGioHang> list = new ArrayList<ChiTietGioHang>();
		ChiTietGioHang chiTietGioHang = new ChiTietGioHang();
		SanPham sp = sanPhamRepository.findById(Integer.valueOf(id)).get();

		total = sp.getGia();
		totalWeight = sp.getTrongLuong();

		List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
		DiaChi diaChi = new DiaChi();
		for (DiaChi dc : listDiaChi) {
			if (dc.isTrangThai()) {
				diaChi = dc;
				break;
			}
		}
		chiTietGioHang.setSanPhamGH(sp);
		chiTietGioHang.setTaiKhoanGH(taiKhoan);
		chiTietGioHang.setSoLuong(1);
//		chiTietGioHangRepository.saveAndFlush(chiTietGioHang);
		list.add(chiTietGioHang);
		model.addAttribute("totalWeight", totalWeight);
		model.addAttribute("total", total);
		model.addAttribute("listChiTietGioHang", list);
		model.addAttribute("diaChi", diaChi);
		return "user/thongtinthanhtoan";
	}

	@GetMapping("gio-hang/mua-hang/dat-hang/xac-nhan")
	public String datHangKhiMuaHang(Model model, @RequestParam(name = "idSP", defaultValue = "") String idSP,
			@RequestParam("phivanchuyen") String phiVanChuyen,
			@RequestParam(name = "note", defaultValue = "") String note,
			@RequestParam("phuongthucthanhtoan") boolean phuongThucThanhToan) {

		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		SanPham sanPham = sanPhamRepository.findById(Integer.valueOf(idSP)).get();
		//đưa sanPham vào session để khi trả về từ cổng thanh toán VN Pay có thể đổ lại phần đặt hàng
		sessionService.setAttribute("sanPham", sanPham);
		//đưa phí vận chuyển vào session
		sessionService.setAttribute("phiVanChuyen", phiVanChuyen);
		//đưa note vào session
		sessionService.setAttribute("note", note);
		//đưa vào một biến boolean để phân giữa trường hợp mua từ giỏ hàng / mua ngay
		isBoughtFromCart = false;
		
		
		double total = sanPham.getGia();

		if(phuongThucThanhToan) {
			
			double totalWeight = sanPham.getTrongLuong();

			List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
			DiaChi diaChi = new DiaChi();
			for (DiaChi dc : listDiaChi) {
				if (dc.isTrangThai()) {
					diaChi = dc;
					break;
				}
			}
			TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
			List<TrangThaiHoaDon> listTTHD = trangThaiHoaDonRepository.findAll();
			for (TrangThaiHoaDon tthd : listTTHD) {
				if (tthd.getTenTrangThai().equals("Đã đặt hàng")) {
					trangThaiHoaDon = tthd;
				}
			}
			// Thêm hóa đơn	
			HoaDon hoaDon = new HoaDon();
			hoaDon.setDiaChi(diaChi.getDiaChi());
			hoaDon.setTongTien(total);
			hoaDon.setPhiVanChuyen(Double.valueOf(phiVanChuyen));
			hoaDon.setTaiKhoanHD(taiKhoan);
			hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
			hoaDon.setGhiChu(note);
			hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
			hoaDonRepository.saveAndFlush(hoaDon);

			//Thêm chi tiết hóa đơn
			ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
			chiTietHoaDon.setHoaDonCT(hoaDon);
			chiTietHoaDon.setSanPhamCT(sanPham);
			chiTietHoaDon.setSoLuong(1);
			chiTietHoaDonRepository.saveAndFlush(chiTietHoaDon);

			return "redirect:/gio-hang/dat-hang/thanh-cong?id=" + hoaDon.getMaHoaDon();
		}
		else {
			String orderType = "other";
	        long amount = (long) ((total+Double.valueOf(phiVanChuyen)) * 100); // Chuyển đổi thành tiền tệ của VNPay (VD: VNĐ)

	        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
	        String vnp_IpAddr = "127.0.0.1";

	        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
	        
	        Map<String, String> vnp_Params = new HashMap<>();
	        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
	        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
	        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
	        vnp_Params.put("vnp_Amount", String.valueOf(amount));
	        vnp_Params.put("vnp_CurrCode", "VND");
	        //Chạy trên môi trường test nên bankCode mặc định là NCB
	        vnp_Params.put("vnp_BankCode", "NCB");
	        
//	        if (bankCode != null && !bankCode.isEmpty()) {
//	            
//	        }
	        
	        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
	        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
	        //VnPay chỉ mới hỗ trợ trong nước nên locale là vn
	        vnp_Params.put("vnp_Locale", "vn");
	        vnp_Params.put("vnp_OrderType", orderType);

//	        String locate = req.getParameter("language");
//	        if (locate != null && !locate.isEmpty()) {
//	            vnp_Params.put("vnp_Locale", locate);
//	        } else {
//	            vnp_Params.put("vnp_Locale", "vn");
//	        }
	        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
	        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

	        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	        String vnp_CreateDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
	        
	        cld.add(Calendar.MINUTE, 15);
	        String vnp_ExpireDate = formatter.format(cld.getTime());
	        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
	        
	        List fieldNames = new ArrayList(vnp_Params.keySet());
	        Collections.sort(fieldNames);
	        StringBuilder hashData = new StringBuilder();
	        StringBuilder query = new StringBuilder();
	        Iterator itr = fieldNames.iterator();
	        while (itr.hasNext()) {
	            String fieldName = (String) itr.next();
	            String fieldValue = (String) vnp_Params.get(fieldName);
	            if ((fieldValue != null) && (fieldValue.length() > 0)) {
	                //Build hash data
	                hashData.append(fieldName);
	                hashData.append('=');
	                try {
	                	hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
	                    //Build query
	                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
	                    query.append('=');
	                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
					} catch (Exception e) {
						System.out.println(e);
					}
	                if (itr.hasNext()) {
	                    query.append('&');
	                    hashData.append('&');
	                }
	            }
	        }
	        String queryUrl = query.toString();
	        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
	        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
	        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
	        
	        PaymentResDTO paymentResDTO = new PaymentResDTO();
	        paymentResDTO.setStatus("Ok");
	        paymentResDTO.setMessage("Successfully");
	        paymentResDTO.setURL(paymentUrl);
	        
	        
	        return "redirect:" + paymentUrl;
		}
	}

	@GetMapping("gio-hang/dat-hang/thanh-cong")
	public String datHangThanhCong(Model model, @RequestParam("id") Integer id) {
		HoaDon hoaDon = hoaDonRepository.findById(id).get();

		List<ChiTietHoaDon> lisChiTietHoaDons = chiTietHoaDonRepository.findByHoaDonCT(hoaDon);

		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
		DiaChi diaChi = new DiaChi();
		for (DiaChi dc : listDiaChi) {
			if (dc.isTrangThai()) {
				diaChi = dc;
				break;
			}
		}
		System.out.println(diaChi);
		model.addAttribute("lisChiTietHoaDons", lisChiTietHoaDons);
		model.addAttribute("hoaDon", hoaDon);
		model.addAttribute("diaChi", diaChi);
		return "user/thanhtoan";
	}
	
	
	@GetMapping("/vnpay_return")
    public String vnPayReturn(
            @RequestParam Map<String, String> requestParams,
            HttpServletRequest request) {
        
        // Lấy thông tin trả về từ VNPAY
        String vnp_ResponseCode = requestParams.get("vnp_ResponseCode");
        String vnp_Amount = requestParams.get("vnp_Amount");
        String vnp_TxnRef = requestParams.get("vnp_TxnRef");
        String vnp_SecureHash = requestParams.get("vnp_SecureHash");
        // Các tham số khác nếu cần

        // Kiểm tra tính hợp lệ của vnp_SecureHash và vnp_ResponseCode
        if ("00".equals(vnp_ResponseCode)) {
            // Thanh toán thành công
            // Thực hiện logic lưu hóa đơn và chi tiết hóa đơn
            double total = Double.valueOf(vnp_Amount) / 100; // Chuyển đổi số tiền về đơn vị tiền tệ

            // Giả sử các thông tin khác cần thiết đã được lưu trữ trong session hoặc có thể lấy từ request
            // Ví dụ: tài khoản người dùng, sản phẩm, địa chỉ giao hàng, phí vận chuyển, ghi chú, phương thức thanh toán
            TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
            
            if(isBoughtFromCart) {
            	List<ChiTietGioHang> listCTGH = sessionService.getAttribute("listCTGH");
            	String note = sessionService.getAttribute("note");// Lấy từ session hoặc request
                double phiVanChuyen =  Double.parseDouble(sessionService.getAttribute("phiVanChuyen"));// Lấy từ session hoặc request
                boolean phuongThucThanhToan = false; // Chắc chắn đây là thanh toán online
                
                List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
                DiaChi diaChi = new DiaChi();
                for (DiaChi dc : listDiaChi) {
                    if (dc.isTrangThai()) {
                        diaChi = dc;
                        break;
                    }
                }
                TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
                List<TrangThaiHoaDon> listTTHD = trangThaiHoaDonRepository.findAll();
                for (TrangThaiHoaDon tthd : listTTHD) {
                    if (tthd.getTenTrangThai().equals("Đã đặt hàng")) {
                        trangThaiHoaDon = tthd;
                    }
                }
                //do từ vn pay trả về đã tính luôn phí vận chuyển, nên khi trả về phải trừ phí vận chuyển ra để tổng tiền ko cộng vào nữa
                double realAmount = total - phiVanChuyen;
                // Thêm hóa đơn	
                HoaDon hoaDon = new HoaDon();
                hoaDon.setDiaChi(diaChi.getDiaChi());
                hoaDon.setTongTien(realAmount);
                hoaDon.setPhiVanChuyen(Double.valueOf(phiVanChuyen));
                hoaDon.setTaiKhoanHD(taiKhoan);
                hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
                hoaDon.setGhiChu(note);
                hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
                hoaDonRepository.saveAndFlush(hoaDon);
                
                
                //Duyệt qua list chi tiết giỏ hàng lấy từ session và thêm vào chi tiết hóa đơn
                //Thêm xong lưu lại và xóa giỏ hàng
                for(ChiTietGioHang get : listCTGH) {
                	//Thêm chi tiết hóa đơn
                    ChiTietHoaDon chiTietHoaDon  = new ChiTietHoaDon();
                	chiTietHoaDon.setHoaDonCT(hoaDon);
                	chiTietHoaDon.setSanPhamCT(get.getSanPhamGH());
                	chiTietHoaDon.setSoLuong(get.getSoLuong());
                	chiTietHoaDonRepository.saveAndFlush(chiTietHoaDon);
                	chiTietGioHangRepository.delete(get);
                }

                //trước khi chuyển hướng về xóa đi các session

                // Chuyển hướng về trang thành công với mã hóa đơn
                return "redirect:/gio-hang/dat-hang/thanh-cong?id=" + hoaDon.getMaHoaDon();
                
            }
            else {
            	// Lấy từ session hoặc request
                SanPham sanPham = sessionService.getAttribute("sanPham");
                String note = sessionService.getAttribute("note");// Lấy từ session hoặc request
                double phiVanChuyen =  Double.parseDouble(sessionService.getAttribute("phiVanChuyen"));// Lấy từ session hoặc request
                boolean phuongThucThanhToan = false; // Chắc chắn đây là thanh toán online

//                double totalWeight = sanPham.getTrongLuong();
                List<DiaChi> listDiaChi = diaChiRepository.findByTaiKhoanDC(taiKhoan);
                DiaChi diaChi = new DiaChi();
                for (DiaChi dc : listDiaChi) {
                    if (dc.isTrangThai()) {
                        diaChi = dc;
                        break;
                    }
                }
                TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
                List<TrangThaiHoaDon> listTTHD = trangThaiHoaDonRepository.findAll();
                for (TrangThaiHoaDon tthd : listTTHD) {
                    if (tthd.getTenTrangThai().equals("Đã đặt hàng")) {
                        trangThaiHoaDon = tthd;
                    }
                }
                
                //do từ vn pay trả về đã tính luôn phí vận chuyển, nên khi trả về phải trừ phí vận chuyển ra để tổng tiền ko cộng vào nữa
                double realAmount = total - phiVanChuyen;
                
                // Thêm hóa đơn	
                HoaDon hoaDon = new HoaDon();
                hoaDon.setDiaChi(diaChi.getDiaChi());
                hoaDon.setTongTien(realAmount);
                hoaDon.setPhiVanChuyen(Double.valueOf(phiVanChuyen));
                hoaDon.setTaiKhoanHD(taiKhoan);
                hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
                hoaDon.setGhiChu(note);
                hoaDon.setPhuongThucThanhToan(phuongThucThanhToan);
                hoaDonRepository.saveAndFlush(hoaDon);

                //Thêm chi tiết hóa đơn
                ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
                chiTietHoaDon.setHoaDonCT(hoaDon);
                chiTietHoaDon.setSanPhamCT(sanPham);
                chiTietHoaDon.setSoLuong(1);
                chiTietHoaDonRepository.saveAndFlush(chiTietHoaDon);
                
                //trước khi chuyển hướng về xóa đi các session

                // Chuyển hướng về trang thành công với mã hóa đơn
                return "redirect:/gio-hang/dat-hang/thanh-cong?id=" + hoaDon.getMaHoaDon();
            }
            
            
        } else {
            // Xử lý khi thanh toán không thành công
            return "redirect:/gio-hang/dat-hang/that-bai";
        }
    }
}


