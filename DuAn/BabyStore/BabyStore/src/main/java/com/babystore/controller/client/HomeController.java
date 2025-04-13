package com.babystore.controller.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.babystore.jparepository.CategoryRepository;
import com.babystore.jparepository.ProductRepository;
import com.babystore.model.Product;
import com.babystore.service.SessionService;

@Controller(value = "homeControllerOfClients")
public class HomeController {
	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ProductRepository productRepository;
	@Autowired
	SessionService sessionService;

	@GetMapping("/user/home-page")
	public String showHomepage(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "8") int size) {

		List<Product> findLastestProducts = productRepository.findTop10ByOrderByIdDesc();

		Pageable pageable = PageRequest.of(page, size);
		Page<Product> findAll = productRepository.findAll(pageable);
		model.addAttribute("findAll", findAll.getContent());
		model.addAttribute("currentPage", findAll.getNumber());
		model.addAttribute("pageSize", findAll.getSize());
		model.addAttribute("totalPages", findAll.getTotalPages());

		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("findLastestProducts", findLastestProducts);
		sessionService.setAttribute("cartUrlBack", "user/home-page");
		return "clients/index";
	}
}
