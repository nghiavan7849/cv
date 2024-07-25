package com.snackviet.controller.user;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.snackviet.model.DanhGia;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.LichSu;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.DanhGiaRepository;
import com.snackviet.repository.HinhAnhDGRepository;
import com.snackviet.repository.LichSuRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.service.CookieService;
import com.snackviet.service.SessionService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class DanhGiaController {
	@Autowired
	SessionService sessionService;
	@Autowired
	HttpSession session;
	@Autowired
	private CookieService cookieService;
	@Autowired
	SanPhamRepository sanPhamRepository;
	@Autowired
	DanhGiaRepository danhGiaRepository;
	@Autowired
	LichSuRepository lichSuRepository;
	@Autowired
	ServletContext context;
	@Autowired
	HinhAnhDGRepository hinhAnhDGRepository;
//	@RequestMapping("/danh-gia")
//	public String danhGia() {
//		return "user/danhgia";
//	}

	@RequestMapping("/danh-gia/{tenSanPham}")
	public String danhGiaTenSanPham(Model model, @PathVariable("tenSanPham") String tenSanPham,
			@RequestParam("soLuong") int soLuong, @RequestParam("tongTien") double tongTien) {
		SanPham sanpham = sanPhamRepository.findByTenSanPhamContaining(tenSanPham);

		session.setAttribute("sanpham", sanpham);
		session.setAttribute("soLuong", soLuong);
		session.setAttribute("tongTien", tongTien);
		model.addAttribute("sanpham", sanpham);
		return "user/danhgia";
	}

	@RequestMapping("/danh-gia")
	public String danhGiaSP(Model model, @RequestParam("masanpham") Integer masanpham,
			@RequestPart(value = "image1", required = false) MultipartFile image1,
			@RequestPart(value = "image2", required = false) MultipartFile image2,
			@RequestPart(value = "image3", required = false) MultipartFile image3,
			@RequestPart(value = "image4", required = false) MultipartFile image4,
			@RequestParam("content") String content, @RequestParam("star") int star) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		SanPham sanpham = sanPhamRepository.findById(masanpham)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
		 Optional<DanhGia> existingReview = danhGiaRepository.findFirstBySanPhamDGAndTaiKhoanDG(sanpham, taiKhoan);
		if (existingReview.isPresent()) {
			model.addAttribute("message", "Bạn đã đánh giá sản phẩm này rồi. Không thể đánh giá lại!!!");
			model.addAttribute("sanpham", sanpham);
			return "user/danhgia";
		} else {
			DanhGia danhGia = new DanhGia();
			danhGia.setSanPhamDG(sanpham);
			danhGia.setTaiKhoanDG(taiKhoan);
			danhGia.setBinhLuan(content);
			danhGia.setNgayDanhGia(new Date());
			danhGia.setSoSao(star);
			danhGia.setTrangThai(true);

			List<HinhAnhDG> listHinhAnhDG = new ArrayList<>();
			try {
				danhGiaRepository.saveAndFlush(danhGia);
				saveImage(image1, danhGia, listHinhAnhDG);
				saveImage(image2, danhGia, listHinhAnhDG);
				saveImage(image3, danhGia, listHinhAnhDG);
				saveImage(image4, danhGia, listHinhAnhDG);
				danhGia.setListHinhAnhDG(listHinhAnhDG);

			} catch (IOException e) {
				e.printStackTrace();
			}

			model.addAttribute("sanpham", sanpham);
			return "user/chitietsanpham";
		}
	}

	private void saveImage(MultipartFile image, DanhGia danhGia, List<HinhAnhDG> listHinhAnhDG) throws IOException {
		if (image != null && !image.isEmpty()) {
			String fileName = image.getOriginalFilename();
			String realPath = context.getRealPath("/image/AnhDanhGia/" + fileName);
			Path path = Path.of(realPath);
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			File file = new File(context.getRealPath("/image/AnhDanhGia/" + fileName));
			image.transferTo(file);

			HinhAnhDG hinhAnhDG = new HinhAnhDG();
			hinhAnhDG.setTenHinhAnh(fileName);
			hinhAnhDG.setDanhGia(danhGia);
			hinhAnhDGRepository.save(hinhAnhDG);
			listHinhAnhDG.add(hinhAnhDG);
		}
	}

	@RequestMapping("/danh-gia/edit/{id}")
	public String editDanhGia(Model model, @PathVariable("id") Integer id) {
		Optional<DanhGia> optionalDanhGia = danhGiaRepository.findById(id);
		if (optionalDanhGia.isPresent()) {
			DanhGia danhGia = optionalDanhGia.get();
			List<HinhAnhDG> anhDG = hinhAnhDGRepository.findByDanhGia(danhGia);
			model.addAttribute("danhGia", danhGia);
			model.addAttribute("anhDG", anhDG);
			return "user/suadanhgia";
		} else {
			return "redirect:/danh-gia";
		}
	}

	@PostMapping("/danh-gia/update")
	public String updateDanhGiaSP(Model model, @RequestParam("idDanhGia") Integer idDanhGia,
			@RequestParam("masanpham") Integer masanpham,
			@RequestPart(value = "image1", required = false) MultipartFile image1,
			@RequestPart(value = "image2", required = false) MultipartFile image2,
			@RequestPart(value = "image3", required = false) MultipartFile image3,
			@RequestPart(value = "image4", required = false) MultipartFile image4,
			@RequestParam("content") String content, @RequestParam("star") int star) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");

		DanhGia danhGia = danhGiaRepository.findById(idDanhGia)
				.orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));

		danhGia.setBinhLuan(content);
		danhGia.setNgayDanhGia(new Date());
		danhGia.setSoSao(star);

		List<HinhAnhDG> listHinhAnhDG = hinhAnhDGRepository.findByDanhGia(danhGia);
		try {
			updateImage(image1, danhGia, listHinhAnhDG, 0);
			updateImage(image2, danhGia, listHinhAnhDG, 1);
			updateImage(image3, danhGia, listHinhAnhDG, 2);
			updateImage(image4, danhGia, listHinhAnhDG, 3);
			danhGiaRepository.saveAndFlush(danhGia);
			danhGia.setListHinhAnhDG(listHinhAnhDG);
		} catch (IOException e) {
			e.printStackTrace();
		}

		SanPham sanpham = sanPhamRepository.findById(masanpham)
				.orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
		model.addAttribute("sanpham", sanpham);
		return "redirect:/chi-tiet-san-pham/" + sanpham.getMaSanPham();
	}

	private void updateImage(MultipartFile image, DanhGia danhGia, List<HinhAnhDG> listHinhAnhDG, int index)
			throws IOException {
		if (image != null && !image.isEmpty()) {
			String fileName = image.getOriginalFilename();
			String realPath = context.getRealPath("/image/AnhDanhGia/" + fileName);
			Path path = Path.of(realPath);
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			File file = new File(context.getRealPath("/image/AnhDanhGia/" + fileName));
			image.transferTo(file);

			HinhAnhDG hinhAnhDG;
			if (listHinhAnhDG.size() > index) {
				hinhAnhDG = listHinhAnhDG.get(index);
				hinhAnhDG.setTenHinhAnh(fileName);
			} else {
				hinhAnhDG = new HinhAnhDG();
				hinhAnhDG.setTenHinhAnh(fileName);
				hinhAnhDG.setDanhGia(danhGia);
				listHinhAnhDG.add(hinhAnhDG);
			}
			hinhAnhDGRepository.save(hinhAnhDG);
		}
	}

}
