package com.babystore.controller.client;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.babystore.model.Product;
import com.babystore.model.ProductDetail;
import com.babystore.jparepository.ProductDetailRepository;
import com.babystore.jparepository.ProductRepository;
@Controller
public class ShopDetailController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductDetailRepository productDetailRepository;
    
    @GetMapping( "/user/shop-detail")
    public String showShopDetail(Model model,@RequestParam("id") Integer id){
        Optional<Product> product = productRepository.findById(id);
        Integer idCategory = product.get().getCategory().getId();
        List<ProductDetail> listProductDetails = productDetailRepository.findByIdProduct(id);
        List<Product> listProductCategory = productRepository.findAllByCategory(idCategory);
        model.addAttribute("product", product.get());
        model.addAttribute("listProductDetail", listProductDetails);
        model.addAttribute("listProductCategory", listProductCategory);
        return "clients/shop-detail";
    }
}
