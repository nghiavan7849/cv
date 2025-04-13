package com.babystore.controller.admin;

import java.util.ArrayList;
import java.util.List;

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

import com.babystore.jparepository.OrderDetailRepository;
import com.babystore.jparepository.OrderRepository;
import com.babystore.jparepository.ShippingStatusRepository;
import com.babystore.model.Order;
import com.babystore.model.OrderDetail;
import com.babystore.model.ShippingStatus;

@Controller
public class OrderController {
	@Autowired
	OrderRepository orderRepository;
	@Autowired 
	OrderDetailRepository orderDetailRepository;
	@Autowired
	ShippingStatusRepository shippingStatusRepository;
	
	List<OrderDetail> listOrderDetails = new ArrayList<OrderDetail>();
	private String statusOrder = "";
	private boolean editing = true;
	private Order orderEdit = new Order();
	private String messageUpdate = "";
	@RequestMapping("/admin/order")
	public String order(Model model, @RequestParam(name = "pageNo" , defaultValue = "1")int pageNo,
			@RequestParam(name = "search", defaultValue = "")String search,
			@RequestParam(name = "sort", defaultValue = "")String sort
			) {
		int size = 5;
		Pageable pageable = PageRequest.of(pageNo - 1, size,Sort.by(Direction.DESC, "id"));
		Page<Order> pageOrder = null;
		if(search.equals("")) {
			pageOrder = orderRepository.findAll(pageable);
		} else if(sort.equals("id")) {
			pageOrder = orderRepository.findById(Integer.valueOf(search),pageable);
		} else if(sort.equals("orderdate")) {
			pageOrder = orderRepository.findByOrderDate(search,pageable);
		} else if(sort.equals("status")) {
			pageOrder = orderRepository.findByStatusOrder(search,pageable);
		} else if(sort.equals("fullname")) {
			pageOrder = orderRepository.findByFullNameAccount(search,pageable);
		} else {
			//address
			pageOrder = orderRepository.findByNameAddress(search,pageable);
		}
		
		List<ShippingStatus> listStatusOrder = new ArrayList<ShippingStatus>();
		if(statusOrder.equals("")) {
			editing = false;
			listStatusOrder = shippingStatusRepository.findAll();
		} else {
			if(statusOrder.equals("Đã đặt hàng")) {
				editing = true;
				listStatusOrder = shippingStatusRepository.findByNameIn(new String[] {"Đã đặt hàng", "Đã xác nhận", "Đã hủy"});
			} else if(statusOrder.equals("Đã xác nhận")) {
				editing = true;
				listStatusOrder = shippingStatusRepository.findByNameIn(new String[] {"Đã xác nhận", "Đang xử lý"});
			} else if(statusOrder.equals("Đang xử lý")) {
				editing = true;
				listStatusOrder = shippingStatusRepository.findByNameIn(new String[] {"Đang xử lý", "Đang vận chuyển"});
			} else if(statusOrder.equals("Đang vận chuyển")) {
				editing = true;
				listStatusOrder = shippingStatusRepository.findByNameIn(new String[] {"Đang vận chuyển", "Giao thành công"});
			} else if(statusOrder.equals("Giao thành công")) {
				editing = false;
				listStatusOrder = shippingStatusRepository.findByNameIn(new String[] {"Giao thành công"});
			} else {
				editing = false;
				listStatusOrder = shippingStatusRepository.findByNameIn(new String[] {"Đã hủy"});
			}
			
		}
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages",pageOrder.getTotalPages());
		model.addAttribute("sort", sort);
		model.addAttribute("search", search);
		model.addAttribute("listOrder", pageOrder.getContent());
		model.addAttribute("listOrderDetails", listOrderDetails);
		model.addAttribute("listStatusOrder", listStatusOrder);
		model.addAttribute("editing", editing);
		model.addAttribute("messageUpdate", messageUpdate);
		listOrderDetails = new ArrayList<OrderDetail>();
		statusOrder  = "";
		messageUpdate = "";
		return "admins/management/order";
	}
	@GetMapping("/admin/order/order-details/{id}")
	public String editOrder(Model model, @PathVariable("id")Integer idOrder) {
		orderEdit = orderRepository.findById(idOrder).get();
		statusOrder = orderEdit.getShippingStatus().getName();
		listOrderDetails = orderDetailRepository.findByOrder(orderEdit);
		return "redirect:/admin/order";
	}
	
	@PostMapping("/admin/order/update")
	public String updateStatusOrder(@RequestParam("status")Integer idStatus) {
		if(orderEdit.getShippingStatus().getId() != idStatus) {
			ShippingStatus shippingStatus = shippingStatusRepository.findById(idStatus).get();
			orderEdit.setShippingStatus(shippingStatus);
			orderRepository.saveAndFlush(orderEdit);
		}
		messageUpdate = "Update invoice status successfully";
		orderEdit = new Order();
		return "redirect:/admin/order";
	}
	
}
