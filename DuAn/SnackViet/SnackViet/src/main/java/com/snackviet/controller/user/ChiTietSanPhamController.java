package com.snackviet.controller.user;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.model.DanhGia;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.HinhAnhSP;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.DanhGiaRepository;
import com.snackviet.repository.HinhAnhDGRepository;
import com.snackviet.repository.HinhAnhSPRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.service.HoaDonService;
import com.snackviet.service.SessionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChiTietSanPhamController {
    @Autowired
    SanPhamRepository sanPhamRepository;
    @Autowired
    HinhAnhSPRepository hinhAnhSPRepository;
    @Autowired
    HinhAnhDGRepository hinhAnhDGRepository;
    @Autowired
    DanhGiaRepository danhGiaRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    HttpSession session;
	@Autowired
	SessionService sessionService;
	@Autowired
	HoaDonService hoaDonService;

    @RequestMapping("chi-tiet-san-pham")
    public String detailProduct(Model model, @RequestParam("pageNo") Optional<Integer> pageNo,
                                @RequestParam("pageSize") Optional<Integer> pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "maSanPham");
        Pageable pageable = PageRequest.of(pageNo.orElse(0), pageSize.orElse(4), sort);
        Page<SanPham> sanphamPage = sanPhamRepository.findAll(pageable);
        List<SanPham> sanPhams = sanphamPage.getContent();
        List<SanPham> shuffledsanPhams = sanPhams.stream().collect(Collectors.toList());
        Collections.shuffle(shuffledsanPhams);

        model.addAttribute("sanPhams", shuffledsanPhams);
        model.addAttribute("pageSize", pageSize.orElse(4));
        return "user/chitietsanpham";
    }

    @RequestMapping("chi-tiet-san-pham/{id}")
    public String detailProducts(Model model, @PathVariable("id") Integer id,
                                 @RequestParam(name = "soSao", defaultValue = "") Optional<Integer> soSao,
                                 @RequestParam("pageNo") Optional<Integer> pageNo, @RequestParam("pageSize") Optional<Integer> pageSize,
                                 @RequestParam("pageSizeDG") Optional<Integer> pageSizeDG) {
        SanPham sanPham = sanPhamRepository.findById(id).orElse(null);
        List<HinhAnhSP> anhSPs = hinhAnhSPRepository.findBySanPhamHA(sanPham);

        // Fetch reviews for the product
        List<DanhGia> danhGias = danhGiaRepository.findBySanPhamDG(sanPham);
     // Fetch images for each review
        for (DanhGia danhGia : danhGias) {
            List<HinhAnhDG> hinhAnhDGs = hinhAnhDGRepository.findByDanhGia(danhGia);
            danhGia.setListHinhAnhDG(hinhAnhDGs);
        }
        
        Sort sortDG = Sort.by(Sort.Direction.ASC, "ngayDanhGia");
        Pageable pageableDG = PageRequest.of(pageNo.orElse(0), pageSizeDG.orElse(2), sortDG);
        Page<DanhGia> danhGiaPage;
        if (soSao.isPresent()) {
            danhGiaPage = danhGiaRepository.findBySoSaoAndSanPhamDG(soSao.get(), sanPham, pageableDG);
        } else {
            danhGiaPage = danhGiaRepository.findBySanPhamDG(sanPham, pageableDG);
        }
        
        List<Integer> totalPages = new ArrayList<>();
        for (int i = 0; i < danhGiaPage.getTotalPages(); i++) {
            totalPages.add(i + 1);
        }

        List<DanhGia> danhgia = danhGiaPage.getContent();
        List<SanPham> sanPhams = sanPhamRepository.findAll(PageRequest.of(pageNo.orElse(0), pageSize.orElse(4))).getContent();
        List<SanPham> shuffledsanPhams = new ArrayList<>(sanPhams);
        Collections.shuffle(shuffledsanPhams);
        
        TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
        model.addAttribute("taiKhoan", taiKhoan);
        
        session.setAttribute("sanpham", sanPham);
        session.setAttribute("anhSPs", anhSPs);
        session.setAttribute("danhGias", danhgia);
        
     // Calculate average rating
        double averageRating = calculateAverageRating(danhGias);
        DecimalFormat df = new DecimalFormat("#.#");
        String formattedRating = df.format(averageRating);
        model.addAttribute("averageRating", formattedRating);
        session.setAttribute("averageRating", formattedRating);
        
        long count1Star = danhGias.stream().filter(dg -> dg.getSoSao() == 1).count();
        long count2Stars = danhGias.stream().filter(dg -> dg.getSoSao() == 2).count();
        long count3Stars = danhGias.stream().filter(dg -> dg.getSoSao() == 3).count();
        long count4Stars = danhGias.stream().filter(dg -> dg.getSoSao() == 4).count();
        long count5Stars = danhGias.stream().filter(dg -> dg.getSoSao() == 5).count();

        model.addAttribute("count1Star", count1Star);
        model.addAttribute("count2Stars", count2Stars);
        model.addAttribute("count3Stars", count3Stars);
        model.addAttribute("count4Stars", count4Stars);
        model.addAttribute("count5Stars", count5Stars);
        model.addAttribute("totalAllStars", count5Stars+count4Stars+count3Stars+count2Stars+count1Star);
        
        session.setAttribute("count1Star", count1Star);
        session.setAttribute("count2Stars", count2Stars);
        session.setAttribute("count3Stars", count3Stars);
        session.setAttribute("count4Stars", count4Stars);
        session.setAttribute("count5Stars", count5Stars);
        
//        Tính tổng sản phẩm đã bán
        int tongSoLuong = hoaDonService.tinhTongSanPhamDaBan(id);
        model.addAttribute("tongSoLuong", tongSoLuong);
        
        model.addAttribute("danhGias", danhgia);
        model.addAttribute("anhSPs", anhSPs);
        model.addAttribute("sanpham", sanPham);
        model.addAttribute("sanPhams", shuffledsanPhams);
        model.addAttribute("pageSize", pageSize.orElse(4));
        model.addAttribute("pageSizeDG", pageSizeDG.orElse(2));
        model.addAttribute("soSao", soSao.orElse(0));

        return "user/chitietsanpham";
    }
    
    private double calculateAverageRating(List<DanhGia> danhGias) {
        if (danhGias.isEmpty()) return 0;
        double sum = 0;
        for (DanhGia danhGia : danhGias) {
            sum += danhGia.getSoSao();
        }
        return sum / danhGias.size();
    }
    
    @RequestMapping("chi-tiet-san-pham/danhgia-more")
    public String detailDanhGia(Model model,
                                @RequestParam(name = "soSao", defaultValue = "") Optional<Integer> soSao,
                                @RequestParam("pageNo") Optional<Integer> pageNo,
                                @RequestParam("pageSize") Optional<Integer> pageSize,
                                @RequestParam("pageSizeDG") Optional<Integer> pageSizeDG,
                                @RequestParam("sanPhamId") Integer sanPhamId) {
        Pageable pageable = PageRequest.of(pageNo.orElse(0), pageSize.orElse(4));
        List<SanPham> sanPhams = sanPhamRepository.findAll(pageable).getContent();
        Sort sortDG = Sort.by(Sort.Direction.ASC, "ngayDanhGia");
        Pageable pageableDG = PageRequest.of(pageNo.orElse(0), pageSizeDG.orElse(2),sortDG);
        Page<DanhGia> danhGiaPage;
        SanPham sanPham = sanPhamRepository.findById(sanPhamId).orElse(null);

        if (soSao.isPresent()) {
            danhGiaPage = danhGiaRepository.findBySoSaoAndSanPhamDG(soSao.get(), sanPham, pageableDG);
        } else {
            danhGiaPage = danhGiaRepository.findBySanPhamDG(sanPham, pageableDG);
            model.addAttribute("pageSizeDG", pageSizeDG.orElse(2));
        }

        List<DanhGia> danhGias = danhGiaPage.getContent();
        List<SanPham> shuffledsanPhams = new ArrayList<>(sanPhams);
        Collections.shuffle(shuffledsanPhams);

        for (DanhGia danhGia : danhGias) {
            List<HinhAnhDG> hinhAnhDGs = hinhAnhDGRepository.findByDanhGia(danhGia);
            danhGia.setListHinhAnhDG(hinhAnhDGs);
        }

        model.addAttribute("danhGias", danhGias);
        model.addAttribute("sanPhams", shuffledsanPhams);
        model.addAttribute("pageSize", pageSize.orElse(4));
        model.addAttribute("pageSizeDG", pageSizeDG.orElse(2));
        model.addAttribute("soSao", soSao.orElse(0));
        model.addAttribute("sanPham", sanPham);

        return "user/chitietsanpham";
    }

}
