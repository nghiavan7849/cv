package com.snackviet.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.model.DanhGia;
import com.snackviet.model.DonHang;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.HinhAnhSP;
import com.snackviet.model.HoaDon;
import com.snackviet.model.LichSu;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;
import com.snackviet.model.TrangThaiHoaDon;
import com.snackviet.repository.DonHangRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.LichSuRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.repository.TrangThaiHoaDonRepository;
import com.snackviet.service.CookieService;
import com.snackviet.service.SessionService;

import jakarta.servlet.http.HttpSession;

import java.rmi.server.Operation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LichSuMuaHangController {

    @Autowired
    LichSuRepository lichSuRepository;
    @Autowired
    DonHangRepository donHangRepository;
    @Autowired
    SessionService sessionService;
	@Autowired
	HttpSession session;
	@Autowired
	SanPhamRepository sanPhamRepository;
	@Autowired
	HoaDonRepository hoaDonRepository;
	@Autowired 
	TrangThaiHoaDonRepository trangThaiHoaDonRepository;

    @GetMapping("lich-su-mua-hang")
    public String lichSuMuaHang(Model model,@RequestParam(name = "maHD",defaultValue = "") String maHD,@RequestParam(name = "trangthai",defaultValue = "")String trangThai,
    		@RequestParam("pageNo") Optional<Integer> pageNo, @RequestParam("pageSize") Optional<Integer> pageSize) {
    	TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
    	
    	
        List<DonHang> listDonHangs = null;
        String paramTrangThai = "";
        String tenTrangThai = "";
        if (trangThai.equals("") || trangThai.equals("dadathang")) {
        	listDonHangs =  donHangRepository.hoadonMuaHang(taiKhoan.getMaTaiKhoan(), "Đã đặt hàng");
        	paramTrangThai = "trangthai=dadathang&";
        	tenTrangThai = "Đã đặt hàng";
        } else if (trangThai.equals("daxacnhan")) {
        	listDonHangs =  donHangRepository.hoadonMuaHang(taiKhoan.getMaTaiKhoan(), "Đã xác nhận");
        	paramTrangThai = "trangthai=daxacnhan&";
        	tenTrangThai = "Đã xác nhận";
        } else if (trangThai.equals("dangxuly")) {
        	listDonHangs =  donHangRepository.hoadonMuaHang(taiKhoan.getMaTaiKhoan(), "Đang xử lý");
        	paramTrangThai = "trangthai=dangxuly&";
        	tenTrangThai = "Đang xử lý";
        } else if (trangThai.equals("dangvanchuyen")) {
        	listDonHangs = donHangRepository.hoadonMuaHang(taiKhoan.getMaTaiKhoan(), "Đang vận chuyển");
        	paramTrangThai = "trangthai=dangvanchuyen&";
        	tenTrangThai = "Đang vận chuyển";
        } else if (trangThai.equals("giaothanhcong")) {
        	listDonHangs = donHangRepository.hoadonMuaHang(taiKhoan.getMaTaiKhoan(), "Giao thành công");
        	paramTrangThai = "trangthai=giaothanhcong&";
        	tenTrangThai = "Giao thành công";
        } else if (trangThai.equals("dahuy")) {
        	listDonHangs = donHangRepository.hoadonMuaHang(taiKhoan.getMaTaiKhoan(), "Đã hủy");
        	paramTrangThai = "trangthai=dahuy&";
        	tenTrangThai = "Đã hủy";
        }
        boolean checkMaHD = true;
        List<LichSu> listLichSus = null;
        if(maHD.equals("")){
        	checkMaHD = false;
        } else {
        	listLichSus = lichSuRepository.lichSuMuaHang(taiKhoan.getMaTaiKhoan(), tenTrangThai, Integer.valueOf(maHD));
        }
        model.addAttribute("checkMaHD", checkMaHD);
        model.addAttribute("paramTrangThai", paramTrangThai);
        model.addAttribute("listDonHangs", listDonHangs); 
        model.addAttribute("listLichSus", listLichSus);
        return "user/lichsumuahang";
    }
    @GetMapping("lich-su-mua-hang/da-nhan-duoc-hang")
    public String daNhanDuocHang(Model model, @RequestParam("idHD")Integer idHd) {
    	List<TrangThaiHoaDon> list = trangThaiHoaDonRepository.findAll();
    	HoaDon hoaDon = hoaDonRepository.findById(idHd).get();
    	for (TrangThaiHoaDon tthd : list) {
			if(tthd.getTenTrangThai().equalsIgnoreCase("Giao thành công")) {
				hoaDon.setTrangThaiHoaDon(tthd);
				break;
			}
		}
    	
    	hoaDonRepository.saveAndFlush(hoaDon);
    	return "redirect:/lich-su-mua-hang?trangthai=dangvanchuyen";
    }
}
