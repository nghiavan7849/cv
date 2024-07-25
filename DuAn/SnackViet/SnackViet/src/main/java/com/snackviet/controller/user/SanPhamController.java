package com.snackviet.controller.user;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.model.SanPham;
import com.snackviet.service.SanPhamService;

@Controller
@RequestMapping("/san-pham")
public class SanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping
    public String viewProductPage(Model model, 
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "6") int size,
                                  @RequestParam(required = false) String search,
                                  @RequestParam(required = false) Integer category,
                                  @RequestParam(required = false) Double minPrice,
                                  @RequestParam(required = false) Double maxPrice,
                                  @RequestParam(required = false) String sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SanPham> productPage;

        if (search != null && !search.isEmpty()) {
            productPage = sanPhamService.searchSanPhams(search, pageable);
        } else if (category != null) {
            productPage = sanPhamService.getSanPhamsByCategory(category, pageable);
        } else if (minPrice != null && maxPrice != null) {
            productPage = sanPhamService.getSanPhamsByPriceRange(minPrice, maxPrice, pageable);
        } else {
            productPage = sanPhamService.getSortedSanPhams(pageable, sort);
        }

        if (productPage.hasContent()) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, productPage.getTotalPages())
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        } else {
            model.addAttribute("message", "No products available.");
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        return "user/sanpham";
    }
}
