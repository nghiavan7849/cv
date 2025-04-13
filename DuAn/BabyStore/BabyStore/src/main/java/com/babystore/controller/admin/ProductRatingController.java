package com.babystore.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductRatingController {
	@RequestMapping("/admin/product-rating")
	public String productRating() {
		return "admins/management/productRating";
	}
}
