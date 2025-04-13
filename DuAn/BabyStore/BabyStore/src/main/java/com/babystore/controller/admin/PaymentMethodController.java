package com.babystore.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PaymentMethodController {
	@RequestMapping("/admin/payment-methods")
	public String paymentMethods() {
		return "admins/management/paymentMethod";
	}
}
