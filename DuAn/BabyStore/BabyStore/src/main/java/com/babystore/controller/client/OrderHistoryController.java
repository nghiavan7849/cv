package com.babystore.controller.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.babystore.jparepository.AccountRepository;
import com.babystore.jparepository.OrderHistoryRepository;
import com.babystore.jparepository.OrderRepository;
import com.babystore.jparepository.ShippingStatusRepository;
import com.babystore.model.Account;
import com.babystore.model.Order;
import com.babystore.model.ShippingStatus;

@Controller
@RequestMapping("/user")
public class OrderHistoryController {
	@Autowired 
	AccountRepository accountRepository;
	@Autowired
	OrderHistoryRepository orderHistoryRepository;
	@Autowired
	ShippingStatusRepository shippingStatusRepository;
	@Autowired
	OrderRepository orderRepository;
	
	@GetMapping("/order-history")
	public String orderHistory(Model model, @RequestParam(name = "trangthai", defaultValue = "")String trangThai,
			@RequestParam(name = "maHD", defaultValue = "")String maHD) {
		Account account = accountRepository.findById(1).get();
		List<Object[]> listOrderHistory = null;
		String paramTrangThai = "";
        String tenTrangThai = "";
		if(trangThai.equals("") || trangThai.equals("dadathang")) {
			listOrderHistory = orderHistoryRepository.listLichSuHoaDon(account.getId(), "Đã đặt hàng");
			tenTrangThai = "Đã đặt hàng";
			paramTrangThai = "trangthai=dadathang&";
		} else if (trangThai.equals("daxacnhan")) {
			listOrderHistory = orderHistoryRepository.listLichSuHoaDon(account.getId(), "Đã xác nhận");
			paramTrangThai = "trangthai=daxacnhan&";
        	tenTrangThai = "Đã xác nhận";
		} else if (trangThai.equals("dangxuly")) {
			listOrderHistory = orderHistoryRepository.listLichSuHoaDon(account.getId(), "Đang xử lý");
			paramTrangThai = "trangthai=dangxuly&";
        	tenTrangThai = "Đang xử lý";
		} else if (trangThai.equals("dangvanchuyen")) {
			listOrderHistory = orderHistoryRepository.listLichSuHoaDon(account.getId(), "Đang vận chuyển");
			paramTrangThai = "trangthai=dangvanchuyen&";
        	tenTrangThai = "Đang vận chuyển";
		} else if (trangThai.equals("giaothanhcong")) {
			listOrderHistory = orderHistoryRepository.listLichSuHoaDon(account.getId(), "Giao thành công");
			paramTrangThai = "trangthai=giaothanhcong&";
        	tenTrangThai = "Giao thành công";
		} else if (trangThai.equals("dahuy")) {
			listOrderHistory = orderHistoryRepository.listLichSuHoaDon(account.getId(), "Đã hủy");
			paramTrangThai = "trangthai=dahuy&";
        	tenTrangThai = "Đã hủy";
		}
        boolean checkMaHD = true;
        List<Object[]> listOrderHistoryDetails = null;
        if(maHD.equals("")){
        	checkMaHD = false;
        } else {
        	checkMaHD = true;
        	listOrderHistoryDetails = orderHistoryRepository.listChiTietLichSuHoaDonByMaHD(account.getId(), Integer.valueOf(maHD));
        }
        model.addAttribute("checkMaHD", checkMaHD);
        model.addAttribute("paramTrangThai", paramTrangThai);
		model.addAttribute("listOrderHistoryDetails", listOrderHistoryDetails);
		model.addAttribute("listOrderHistory", listOrderHistory);
		System.out.println(listOrderHistory);
		return "clients/orderhistory";
	}
	@GetMapping("order-history/received")
	public String userClickDaNhanDuocHang(Model model, @RequestParam("idHD")String idHD) {
		List<ShippingStatus> list = shippingStatusRepository.findAll();
		Order order = orderRepository.findById(Integer.valueOf(idHD)).get();
		for (ShippingStatus ss : list) {
			if(ss.getName().equals("Giao thành công")) {
				order.setShippingStatus(ss);
				break;
			}
		}
		orderRepository.saveAndFlush(order);
		return "redirect:/user/order-history?trangthai=dangvanchuyen";
	}
	
}
