package com.snackviet.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.model.LoaiSP;
import com.snackviet.repository.LoaiSPRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("adminIndex/quan-ly-loai-san-pham")
public class AdminLoaiSanPhamController {
	@Autowired 
	LoaiSPRepository loaiSPRepository;
	
	boolean editting = false;
	LoaiSP loaiSP;
	String messageSuccess = "";
	
	@GetMapping
	public String get(Model model, @RequestParam("pageNo") Optional<Integer> pageNo,
	        @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
	        @RequestParam(name = "search", defaultValue = "") String search) {
		if(!editting) {
			loaiSP = new LoaiSP();	
		}
		int currentPage = pageNo.orElse(1);
	    Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<LoaiSP> pageLoaiSP = loaiSPRepository.findAll(pageable);
	    
		if(!search.isEmpty()) {
			pageLoaiSP = loaiSPRepository.findByTenLoai(search, pageable);
		}
		
		model.addAttribute("editting",editting);
	    model.addAttribute("loaiSanPham", loaiSP);
		model.addAttribute("listLoaiSanPham", pageLoaiSP.getContent());
	    model.addAttribute("currentPage", currentPage);
	    model.addAttribute("totalItems", pageLoaiSP.getTotalElements());
	    model.addAttribute("totalPages", pageLoaiSP.getTotalPages());
	    model.addAttribute("currentPage", currentPage);
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("search", search);
	    model.addAttribute("messageSuccess",messageSuccess);
	    messageSuccess = "";
		return "admin/quanLyLoaiSanPham";
	}
	@GetMapping("edit/{id}")
	public String edit(Model model,@PathVariable("id")Integer id) {
		editting = true;
		loaiSP = loaiSPRepository.findById(id).get();
		return "redirect:/adminIndex/quan-ly-loai-san-pham";
	}
	
	@PostMapping("cancel")
	public String cancel(Model model) {
		editting = false;
		return "redirect:/adminIndex/quan-ly-loai-san-pham";
	}
	
	
	@PostMapping("insert")
	public String insert(Model model,@Valid @ModelAttribute("loaiSanPham")LoaiSP loaiSP,BindingResult result,
			@RequestParam("pageNo") Optional<Integer> pageNo,
	        @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
	        @RequestParam(name = "search", defaultValue = "") String search) {
		boolean check = false;
		String messageError = ""; 
		List<LoaiSP> loaiSPs = loaiSPRepository.findAll();
		if(result.hasErrors()) {
			check = true;
		} else {
			for (LoaiSP loai : loaiSPs) {
				 if(loaiSP.getTenLoai().equals(loai.getTenLoai())) {
					 messageError = "Tên loại đã tồn tại!!!";
					 check = true;
				 }
			}
		}
		if(check) {
			int currentPage = pageNo.orElse(1);
		    Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
			Page<LoaiSP> pageLoaiSP = loaiSPRepository.findAll(pageable);
		    
			if(!search.isEmpty()) {
				pageLoaiSP = loaiSPRepository.findByTenLoai(search, pageable);
			}
			
			model.addAttribute("editting",editting);
		    model.addAttribute("loaiSanPham", loaiSP);
			model.addAttribute("listLoaiSanPham", pageLoaiSP.getContent());
		    model.addAttribute("currentPage", currentPage);
		    model.addAttribute("totalItems", pageLoaiSP.getTotalElements());
		    model.addAttribute("totalPages", pageLoaiSP.getTotalPages());
		    model.addAttribute("currentPage", currentPage);
		    model.addAttribute("pageSize", pageSize);
		    model.addAttribute("search", search);
		    model.addAttribute("messageError", messageError);
			return "admin/quanLyLoaiSanPham";
		}
		
		loaiSPRepository.saveAndFlush(loaiSP);
		messageSuccess = "Thêm thành công";
		return "redirect:/adminIndex/quan-ly-loai-san-pham";
	}
	@PostMapping("update")
	public String update(Model model,@Valid @ModelAttribute("loaiSanPham")LoaiSP loaiSP,BindingResult result,
			@RequestParam("pageNo") Optional<Integer> pageNo,
	        @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
	        @RequestParam(name = "search", defaultValue = "") String search) {
		boolean check = false;
		String messageError = ""; 
		List<LoaiSP> loaiSPs = loaiSPRepository.findAll();
		if(result.hasErrors()) {
			check = true;
		} else {
			for (LoaiSP loai : loaiSPs) {
				 if(loaiSP.getTenLoai().equals(loai.getTenLoai())) {
					 messageError = "Tên loại đã tồn tại!!!";
					 check = true;
				 }
			}
		}
		if(check) {
			int currentPage = pageNo.orElse(1);
		    Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
			Page<LoaiSP> pageLoaiSP = loaiSPRepository.findAll(pageable);
		    
			if(!search.isEmpty()) {
				pageLoaiSP = loaiSPRepository.findByTenLoai(search, pageable);
			}
			
			model.addAttribute("editting",editting);
		    model.addAttribute("loaiSanPham", loaiSP);
			model.addAttribute("listLoaiSanPham", pageLoaiSP.getContent());
		    model.addAttribute("currentPage", currentPage);
		    model.addAttribute("totalItems", pageLoaiSP.getTotalElements());
		    model.addAttribute("totalPages", pageLoaiSP.getTotalPages());
		    model.addAttribute("currentPage", currentPage);
		    model.addAttribute("pageSize", pageSize);
		    model.addAttribute("search", search);
		    model.addAttribute("messageError", messageError);
			return "admin/quanLyLoaiSanPham";
		}
		
		loaiSP.setMaLoai(this.loaiSP.getMaLoai());
		loaiSPRepository.saveAndFlush(loaiSP);
		messageSuccess = "Cập nhật thành công";
		editting = false;
		return "redirect:/adminIndex/quan-ly-loai-san-pham";
	}
	
}
