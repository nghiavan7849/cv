package com.snackviet.controller.admin;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.model.DanhGia;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.SanPham;
import com.snackviet.repository.DanhGiaRepository;
import com.snackviet.repository.HinhAnhDGRepository;
import com.snackviet.repository.LoaiSPRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.repository.TaiKhoanRepository;

import jakarta.servlet.ServletContext;

@Controller
public class AdminDanhGiaController {

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
	ServletContext app;

	@RequestMapping("/adminIndex/danh-gia")
	public String products(Model model, @RequestParam("pageNo") Optional<Integer> pageNo,
			@RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
			@RequestParam(name = "search", defaultValue = "") String search,
			@RequestParam(name = "sort", defaultValue = "") String sort,
			@RequestParam(name = "ngayFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayFrom,
			@RequestParam(name = "ngayTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayTo) {

		model.addAttribute("listHinhAnhDG", hinhAnhDGRepository.findAll());

		editting = false;
		model.addAttribute("editting", editting);
		
		// lấy tổng số đánh giá
		model.addAttribute("total", danhGiaRepository.findAll());
		// lấy số sản phẩm đạt 5 sao
		model.addAttribute("fivestar", danhGiaRepository.findBySoSao(5));

		// PHÂN TRANG VÀ TÌM KIẾM
		int currentPage = pageNo.orElse(1);
		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<DanhGia> reviewPage = null;

		if (!search.isEmpty() && !sort.isEmpty()) {
			// Lọc dữ liệu theo cả search và sort
			if (sort.equals("Tên sản phẩm")) {
				reviewPage = danhGiaRepository.findBySanPhamDGTenSanPhamContaining(search, pageable);
			} else if (sort.equals("Số sao")) {
				try {
					reviewPage = danhGiaRepository.findBySoSao(Integer.parseInt(search), pageable);
				} catch (Exception e) {
					// TODO: handle exception
					reviewPage = danhGiaRepository.findAll(pageable);
				}
			} else if (sort.equals("Trạng thái hiển thị")) {
				reviewPage = danhGiaRepository.findByTrangThai(true, pageable);
			} else if (sort.equals("Trạng thái ẩn")) {
				reviewPage = danhGiaRepository.findByTrangThai(false, pageable);
			} else if (sort.equals("Bình luận")) {
				reviewPage = danhGiaRepository.findByBinhLuanContaining(search, pageable);
			}
		} else if (!sort.isEmpty()) {
			if (sort.equals("Trạng thái hiển thị")) {
				reviewPage = danhGiaRepository.findByTrangThai(true, pageable);
			} else if (sort.equals("Trạng thái ẩn")) {
				reviewPage = danhGiaRepository.findByTrangThai(false, pageable);
			} else if (sort.equals("Ngày đánh giá") && ngayFrom != null && ngayTo != null && ngayFrom.before(ngayTo)) {
				reviewPage = danhGiaRepository.findByNgayDanhGiaBetween(ngayFrom, ngayTo, pageable);
			}
			else if (sort.equals("Ngày đánh giá") && ngayFrom != null && ngayTo != null && ngayFrom.after(ngayTo)) {
				return "redirect:/adminIndex/danh-gia?notFound=date";
			}
		} else {
			// Không lọc dữ liệu
			reviewPage = danhGiaRepository.findAll(pageable);
		}

		// Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về
		// admin/quanLyDanhGia
		if (reviewPage == null || reviewPage.isEmpty()) {
			model.addAttribute("reviews", Collections.emptyList());
			model.addAttribute("totalItems", 0);
			model.addAttribute("totalPages", 0);
			model.addAttribute("currentPage", 0);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("search", search);
			model.addAttribute("sort", sort);
			model.addAttribute("ngayFrom", ngayFrom);
			model.addAttribute("ngayTo", ngayTo);
			// Load lại dữ liệu select box đã được chọn
			model.addAttribute("displaySelected", sort);
			return "admin/quanLyDanhGia";
		}
		
		model.addAttribute("reviews", reviewPage.getContent());
		model.addAttribute("totalItems", reviewPage.getTotalElements());
		model.addAttribute("totalPages", reviewPage.getTotalPages());
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("search", search);
		model.addAttribute("sort", sort);
		model.addAttribute("ngayFrom", ngayFrom);
		model.addAttribute("ngayTo", ngayTo);

		// Load lại dữ liệu select box đã được chọn
		model.addAttribute("displaySelected", sort);

		System.out.println(sort);

		return "admin/quanLyDanhGia";

	}

	@RequestMapping("/adminIndex/danh-gia/{maDanhGia}")
	public String editSanPham(Model model, @PathVariable("maDanhGia") int maDanhGia,
			@RequestParam("pageNo") Optional<Integer> pageNo,
			@RequestParam(name = "search", defaultValue = "") String search,
			@RequestParam(name = "sort", defaultValue = "") String sort,
			@RequestParam(name = "ngayFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayFrom,
			@RequestParam(name = "ngayTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayTo) {

		// Bắt lỗi tìm với id nằm ngoài dữ liệu
		Optional<DanhGia> editOptional = danhGiaRepository.findById(maDanhGia);
		if (!editOptional.isPresent()) {
			return "redirect:/adminIndex/danh-gia?notFound=true";
		}
		DanhGia edit = editOptional.get();

		List<HinhAnhDG> listHinhDG = hinhAnhDGRepository.findByDanhGiaMaDanhGia(maDanhGia);
		model.addAttribute("listHinhDG", listHinhDG);

		// PHÂN TRANG SAU KHI EDIT
		int pageSize = 5;

		if (pageSize == 0) {
			pageSize = 5;
		}

		int currentPage = pageNo.orElse(1);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<DanhGia> reviewPage = null;

		// Hiển thị lại bộ lọc
		if (!search.isEmpty() && !sort.isEmpty()) {
			// Lọc dữ liệu theo cả search và sort
			if (sort.equals("Tên sản phẩm")) {
				reviewPage = danhGiaRepository.findBySanPhamDGTenSanPhamContaining(search, pageable);
			} else if (sort.equals("Số sao")) {
				try {
					reviewPage = danhGiaRepository.findBySoSao(Integer.parseInt(search), pageable);
				} catch (Exception e) {
					// TODO: handle exception
					reviewPage = danhGiaRepository.findAll(pageable);
				}
			} else if (sort.equals("Trạng thái hiển thị")) {
				reviewPage = danhGiaRepository.findByTrangThai(true, pageable);
			} else if (sort.equals("Trạng thái ẩn")) {
				reviewPage = danhGiaRepository.findByTrangThai(false, pageable);
			} else if (sort.equals("Bình luận")) {
				reviewPage = danhGiaRepository.findByBinhLuanContaining(search, pageable);
			}
		} else if (!sort.isEmpty()) {
			if (sort.equals("Trạng thái hiển thị")) {
				reviewPage = danhGiaRepository.findByTrangThai(true, pageable);
			} else if (sort.equals("Trạng thái ẩn")) {
				reviewPage = danhGiaRepository.findByTrangThai(false, pageable);
			} else if (sort.equals("Ngày đánh giá") && ngayFrom != null && ngayTo != null && ngayFrom.before(ngayTo)) {
				reviewPage = danhGiaRepository.findByNgayDanhGiaBetween(ngayFrom, ngayTo, pageable);
			}
			else if (sort.equals("Ngày đánh giá") && ngayFrom != null && ngayTo != null && ngayFrom.after(ngayTo)) {
				return "redirect:/adminIndex/danh-gia?notFound=date";
			}

		} else {
			// Không lọc dữ liệu
			reviewPage = danhGiaRepository.findAll(pageable);
		}

		// Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về
		// admin/quanLyDanhGia
		if (reviewPage == null || reviewPage.isEmpty()) {
			model.addAttribute("reviews", Collections.emptyList());
			model.addAttribute("totalItems", 0);
			model.addAttribute("totalPages", 0);
			model.addAttribute("currentPage", 0);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("search", search);
			model.addAttribute("sort", sort);
			model.addAttribute("ngayFrom", ngayFrom);
			model.addAttribute("ngayTo", ngayTo);
			// Load lại dữ liệu select box đã được chọn
			model.addAttribute("displaySelected", sort);
			return "admin/quanLyDanhGia";
		}

		long totalItems = reviewPage.getTotalElements();
		int totalPages = reviewPage.getTotalPages();

		// lấy tổng số đánh giá
		model.addAttribute("total", danhGiaRepository.findAll());
		// lấy số sản phẩm đạt 5 sao
		model.addAttribute("fivestar", danhGiaRepository.findBySoSao(5));
		model.addAttribute("reviews", reviewPage.getContent());
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("search", search);
		model.addAttribute("sort", sort);
		model.addAttribute("displaySelected", sort);
		model.addAttribute("ngayFrom", ngayFrom);
		model.addAttribute("ngayTo", ngayTo);

//		model.addAttribute("sanPham",edit);
//		model.addAttribute("listLoaiSP",loaiSPRepository.findAll());
		model.addAttribute("displayR", edit);

		return "admin/quanLyDanhGia";

	}

}
