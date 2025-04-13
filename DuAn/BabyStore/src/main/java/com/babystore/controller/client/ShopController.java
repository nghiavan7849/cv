package com.babystore.controller.client;

import com.babystore.model.Avaluation;
import com.babystore.model.Category;
import com.babystore.model.Product;
import com.babystore.jparepository.AvaluationRepository;
import com.babystore.jparepository.CategoryRepository;
import com.babystore.jparepository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.babystore.service.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("user")
public class ShopController {
	@Autowired
	SessionService sessionService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    AvaluationRepository avaluationRepository;


    @GetMapping("shop")
    public String showShop(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        // Fill all product
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> findAll = productRepository.findAll(pageable);

        model.addAttribute("products", findAll.getContent());
        model.addAttribute("currentPage", findAll.getNumber());
        model.addAttribute("pageSize", findAll.getSize());
        model.addAttribute("totalPages", findAll.getTotalPages());

        //Fill category
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        //Fill evaluation
        List<Avaluation> evaluations = avaluationRepository.findTop3OrderByAmountStars(5);
        model.addAttribute("evaluations", evaluations);

        return "clients/shop";
    }

    @GetMapping("result")
    public String findByName(@RequestParam("keyword") String keyword, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> findName = productRepository.findByNameContaining(keyword, pageable);

        if (findName.getSize() == 0) {
            return "clients/404";
        } else {

            model.addAttribute("products", findName.getContent());
            model.addAttribute("currentPage", findName.getNumber());
            model.addAttribute("pageSize", findName.getSize());
            model.addAttribute("totalPages", findName.getTotalPages());

            return "clients/s-shop";
        }
    }

    @GetMapping("shop/query")
    public String findTotal(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "9") int size,
                            @RequestParam("total") Integer total) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> findTotalBetween = productRepository.findByTotalBetween(total, pageable);

        model.addAttribute("products", findTotalBetween.getContent());
        model.addAttribute("currentPage", findTotalBetween.getNumber());
        model.addAttribute("pageSize", findTotalBetween.getSize());
        model.addAttribute("totalPages", findTotalBetween.getTotalPages());
        return "clients/s-shop";

    }
}
