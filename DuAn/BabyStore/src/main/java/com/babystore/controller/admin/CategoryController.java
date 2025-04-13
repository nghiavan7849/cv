package com.babystore.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.babystore.jparepository.CategoryRepository;
import com.babystore.model.Category;

@Controller
public class CategoryController {
	@Autowired
	CategoryRepository categoryRepository;
	String status = "insert";
	Category categoryNew;
	String errorName = "*";
	@GetMapping("/admin/category")
	public String category(Model model) {
		List<Category> listCategory = categoryRepository.findAll();
		status = "insert";
		model.addAttribute("listCategory",listCategory);
		model.addAttribute("category",categoryNew);
		model.addAttribute("errorName",errorName);
		categoryNew = null;
		errorName = "*";
		return "admins/management/category";
	}
	@GetMapping("/admin/category/edit")
	public String editCategory(Model model,@RequestParam("id") Integer id) {
		List<Category> listCategory = categoryRepository.findAll();
		Optional<Category> categorys = categoryRepository.findById(id);
		categoryNew = categorys.get();
		status = "update";
		errorName = "*";
		model.addAttribute("category",categoryNew);
		model.addAttribute("errorName",errorName);
		model.addAttribute("listCategory",listCategory);
		return "admins/management/category";
	}
	@PostMapping("/admin/category/save")
	public String saveCategory(Model model, @RequestParam("name") String name) {
		if(status.equals("insert")) {
			if(checkName(null, name)) {
			Category category = new Category();
			category.setName(name);
			categoryRepository.saveAndFlush(category);
			categoryNew = null;
			}
		}else {
			if(checkName(categoryNew.getId(), name)) {
			categoryNew.setName(name);
			categoryRepository.saveAndFlush(categoryNew);
			categoryNew = null;
			}
		}
		return "redirect:/admin/category";
	}
	
	@GetMapping("/admin/category/delete")
	public String deleteCategory(@RequestParam("id") Integer id) {
		Optional<Category> categorys = categoryRepository.findById(id);
		categoryRepository.delete(categorys.get());
		return "redirect:/admin/category";
	}
	
	@GetMapping("/admin/category/reset")
	public String resetCategory() {
		categoryNew = null;
		errorName = "*";
		return "redirect:/admin/category";
	}
	
	public boolean checkName(Integer id, String name) {
		boolean checkValue = true;
		List<Category> listCategories = categoryRepository.findAll();
		if(status.equals("insert")) {
			for(Category category : listCategories) {
				if(category.getName().equalsIgnoreCase(name)) {
					errorName = "Tên thể loại đã tồn tại";
					checkValue = false;
					break;
				}else {
					errorName = "*";
				}
			}
		}else {
			for(Category category : listCategories) {
				if (category.getId() == id) {
					continue;
				}
				if(category.getName().equalsIgnoreCase(name)) {
					errorName = "Tên thể loại đã tồn tại";
					checkValue = false;
					break;
				}else {
					errorName = "*";
				}
			}
		}
		return checkValue;
	}
}
