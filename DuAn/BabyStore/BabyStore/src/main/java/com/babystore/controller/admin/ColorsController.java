package com.babystore.controller.admin;

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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.babystore.jparepository.AccountRepository;
import com.babystore.jparepository.AddressRepository;
import com.babystore.jparepository.ColorRepository;
import com.babystore.model.Account;
import com.babystore.model.Address;
import com.babystore.model.Color;
import com.babystore.service.SessionService;

@Controller
public class ColorsController {
	@Autowired
	ColorRepository colorRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	SessionService sessionService;

	Color color = new Color();
	String messageSoDienThoai = "";
	String messageProvince = "";
	String messageDistrict = "";
	String messageWard = "";
	String messageDiaChiCuThe = "";
	boolean checkValid = false;
	boolean checkBtn = false;

	@GetMapping("admin/color")
	public String color(Model model, @RequestParam("pageNo") Optional<Integer> pageNo,
			@RequestParam("pageSize") Optional<Integer> pageSize) {
		Sort sort = Sort.by(Sort.Direction.DESC, "id");
		Pageable pageable = PageRequest.of(pageNo.orElse(0), pageSize.orElse(4), sort);
		Page<Color> colorPage = colorRepository.findAll(pageable);
		List<Color> colors = colorPage.getContent();
		
		if (color.getId() !=null) {
			model.addAttribute("color", color);
		}
		
		model.addAttribute("colors",colors);
		model.addAttribute("checkBtn", checkBtn);
		return "admins/management/colors";
	}
	@PostMapping("admin/color/create")
	public String createColor(Model model, @RequestParam("name") String name) {
		Color color = new Color();
		color.setName(name);
		colorRepository.saveAndFlush(color);
		return "redirect:/admin/color";
	}
	@GetMapping("admin/color/edit/{id}")
	public String editColor(Model model, @PathVariable("id") Integer id) {
		color = colorRepository.findById(id).get();
		checkBtn = true;
		return "redirect:/admin/color";
	}
	@PostMapping("admin/color/update")
	public String updateColor(Model model, @RequestParam("id") Integer id, @RequestParam("name") String name) {
		Color color = colorRepository.findById(id).get();
		color.setName(name);
		colorRepository.saveAndFlush(color);
		return "redirect:/admin/color";
	}
	@GetMapping("admin/color/remove/{id}")
	public String removeColor(Model model, @PathVariable("id") Integer id) {
		Color color = colorRepository.findById(id).get();
		colorRepository.delete(color);
		this.color = new Color();
		return "redirect:/admin/color";
	}
	@GetMapping("admin/color/lam-moi")
	public String resetColor(Model model) {
		color = new Color();
		checkBtn = false;
		return "redirect:/admin/color";
	}
}
