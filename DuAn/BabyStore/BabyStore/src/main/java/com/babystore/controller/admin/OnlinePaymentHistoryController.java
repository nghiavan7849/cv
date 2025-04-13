package com.babystore.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.babystore.jparepository.TransactionRepository;
import com.babystore.model.Transaction;

@Controller
public class OnlinePaymentHistoryController {
	
	@Autowired
	TransactionRepository transactionRepository;
	
	@RequestMapping("/admin/online-payment-history")
	public String onlinePaymentHistory(Model model) {
		model.addAttribute("listTransactions",transactionRepository.findAll());
		return "admins/management/onlinePaymentHistory";
	}
	
	@RequestMapping("/admin/online-payment-history/transaction-details/{id}")
	public String transactionDetails(Model model,@PathVariable("id") int id) {
		
		Transaction detail = transactionRepository.findById(id).get();
		
		if(detail!=null) {
			model.addAttribute("displayT",detail);
		}
		
		model.addAttribute("listTransactions",transactionRepository.findAll());
		return "admins/management/onlinePaymentHistory";
	}
	
}
