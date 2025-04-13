package com.babystore.controller.client;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.babystore.config.VNPayConfig;
import com.babystore.jparepository.AccountRepository;
import com.babystore.jparepository.AddressRepository;
import com.babystore.jparepository.CartDetailRepository;
import com.babystore.jparepository.OrderDetailRepository;
import com.babystore.jparepository.OrderRepository;
import com.babystore.jparepository.ProductDetailRepository;
import com.babystore.jparepository.ShippingStatusRepository;
import com.babystore.jparepository.TransactionRepository;
import com.babystore.model.Account;
import com.babystore.model.Address;
import com.babystore.model.CartDetail;
import com.babystore.model.Order;
import com.babystore.model.OrderDetail;
import com.babystore.model.ProductDetail;
import com.babystore.model.ShippingStatus;
import com.babystore.model.Transaction;
import com.babystore.service.SessionService;


import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user")
public class CartController {
	@Autowired
	CartDetailRepository cartDetailRepository;
	@Autowired
	SessionService sessionService;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ProductDetailRepository productDetailRepository;
	@Autowired 
	AddressRepository addressRepository;
	@Autowired 
	ShippingStatusRepository shippingStatusRepository;
	@Autowired
	OrderDetailRepository orderDetailRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	HttpServletRequest req;
	boolean isBoughtFromCart = false;

	@GetMapping("cart")
	public String getCart(Model model) {
		List<CartDetail> cartDetailts = new ArrayList<>();
//		Account account = sessionService.getAttribute("account");
		Account account = accountRepository.findById(1).get();
		cartDetailts = cartDetailRepository.findAllByAccount(account);
		model.addAttribute("listCarts", cartDetailts);
		return "clients/shoppingCart";
	}
	
	@GetMapping("shop/add-cart/{id}/{quantity}")
	public String addCart(Model model, @PathVariable("id")Integer idSP ,@PathVariable("quantity")Integer quantity) {
		Account account = accountRepository.findById(1).get();
		ProductDetail productDetail = productDetailRepository.findById(idSP).get();
		CartDetail cart = cartDetailRepository.findFirstByAccountAndProductDetail(account, productDetail);
		if(cart == null) {
			CartDetail cartDetail = new CartDetail();
			cartDetail.setAccount(account);
			cartDetail.setProductDetail(productDetail);
			cartDetail.setQuantity(quantity);
			cartDetail.setPrice(productDetail.getPrice());
			cartDetailRepository.saveAndFlush(cartDetail);
		} else {
			cart.setQuantity(cart.getQuantity() + quantity);
			cartDetailRepository.saveAndFlush(cart);
		}
		String cartUrlBack= sessionService.getAttribute("cartUrlBack");
		// return "redirect:/"+cartUrlBack;
		return "redirect:/user/cart";
	}
	
	@PostMapping("cart/get-cart-pay")
	public String getCartPay(Model model, @RequestParam("idCart")String[] idCart) {
		List<CartDetail> listCartDetails = new ArrayList<CartDetail>();
		double total = 0;
		double totalWeight = 0;
		Account account = accountRepository.findById(1).get();
 		for (String string : idCart) {
 			CartDetail cartDetail = cartDetailRepository.findById(Integer.valueOf(string)).get();
			listCartDetails.add(cartDetail);
			total += cartDetail.getPrice() * cartDetail.getQuantity();
			totalWeight += cartDetail.getProductDetail().getWeight() * cartDetail.getQuantity();
 		}
		List<Address> lisAddresses = addressRepository.findByAccount(account); 
		for (Address add : lisAddresses) {
			if(add.isStatus()) {
				model.addAttribute("address", add);
				break;
			}
		}
		model.addAttribute("totalWeight", totalWeight);
 		model.addAttribute("total", total);
 		model.addAttribute("listCartDetails", listCartDetails);
 		//cho list CTGH vào session
 		sessionService.setAttribute("listCTGH", listCartDetails);

		return "clients/pay";
	}
	

	@PostMapping("cart/get-cart-pay/order")
	public String orderProduct(Model model,@RequestParam("idCart")String[] idCart,@RequestParam("paymentMethod")String paymentMethod,@RequestParam("phiVanChuyen")String phiVanChuyen ) {
		Account account = accountRepository.findById(1).get();
		List<Address> lisAddresses = addressRepository.findByAccount(account); 
		Address address = new Address();
		for (Address add : lisAddresses) {
			if(add.isStatus()) {
				address = add;
				break;
			}
		}
		double total = 0;
		double totalWeight = 0;
		List<CartDetail> listCartDetails = new ArrayList<CartDetail>();
 		for (String string : idCart) {
 			CartDetail cartDetail = cartDetailRepository.findById(Integer.valueOf(string)).get();
			listCartDetails.add(cartDetail);
			total += cartDetail.getPrice() * cartDetail.getQuantity();
 		}
 		// Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Định dạng ngày tháng theo định dạng "dd-MM-yyyy"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        
        
     // Đưa thông tin sản phẩm và phí vận chuyển vào session
	    sessionService.setAttribute("total", total);
	    sessionService.setAttribute("phiVanChuyen", phiVanChuyen);
	    //đưa vào một biến boolean để phân giữa trường hợp mua từ giỏ hàng / mua ngay
	  	isBoughtFromCart = true;
        
        //khởi tạo đơn hàng
        Order order = new Order();
        
        //THANH TOÁN KHI NHẬN HÀNG
        if(paymentMethod.equals("cod")) {
        	// lưu order vào database
    		order.setAccount(account);
    		order.setAddress(address);
    		List<ShippingStatus> list = shippingStatusRepository.findAll();
    		for (ShippingStatus shippingStatus : list) {
    			if(shippingStatus.getName().equals("Đã đặt hàng")) {
    				order.setShippingStatus(shippingStatus);
    				break;
    			}
    		}
    		order.setShippingFee(Double.valueOf(phiVanChuyen));
    		order.setTotal(total);
    		order.setOrderDate(formattedDate);
    		
    		
			order.setPaymentMethod(true);
			order.setPaymentStatus(false);
			orderRepository.saveAndFlush(order);
			
			for(CartDetail cd: listCartDetails) {
    			OrderDetail orderDetail = new OrderDetail();
    			orderDetail.setOrder(order);
    			orderDetail.setPrice(cd.getPrice());
    			orderDetail.setQuantity(cd.getQuantity());
    			orderDetail.setProductDetail(cd.getProductDetail());
    			orderDetailRepository.saveAndFlush(orderDetail);
    			cartDetailRepository.delete(cd);
    		}
			
			return "redirect:/user/cart/pay-success/"+ order.getId(); 
		} 
        //THANH TOÁN VN PAY
        else {
			
			String orderType = "other";
	        long amount = (long) ((total + Double.valueOf(phiVanChuyen)) * 100); // Chuyển đổi thành tiền tệ của VNPay (VD: VNĐ)

	        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
	        String vnp_IpAddr = "127.0.0.1";

	        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

	        Map<String, String> vnp_Params = new HashMap<>();
	        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
	        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
	        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
	        vnp_Params.put("vnp_Amount", String.valueOf(amount));
	        vnp_Params.put("vnp_CurrCode", "VND");
	        vnp_Params.put("vnp_BankCode", "NCB");

	        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
	        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
	        vnp_Params.put("vnp_Locale", "vn");
	        vnp_Params.put("vnp_OrderType", orderType);
	        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
	        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

	        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
	        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	        String vnp_CreateDate = format.format(cld.getTime());
	        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

	        cld.add(Calendar.MINUTE, 15);
	        String vnp_ExpireDate = format.format(cld.getTime());
	        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

	        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
	        Collections.sort(fieldNames);
	        StringBuilder hashData = new StringBuilder();
	        StringBuilder query = new StringBuilder();
	        Iterator<String> itr = fieldNames.iterator();
	        while (itr.hasNext()) {
	            String fieldName = itr.next();
	            String fieldValue = vnp_Params.get(fieldName);
	            if ((fieldValue != null) && (fieldValue.length() > 0)) {
	                hashData.append(fieldName);
	                hashData.append('=');
	                try {
	                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
	                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
	                    query.append('=');
	                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
	                } catch (Exception e) {
	                    System.out.println(e);
	                }
	                if (itr.hasNext()) {
	                    query.append('&');
	                    hashData.append('&');
	                }
	            }
	        }
	        String queryUrl = query.toString();
	        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
	        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
	        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

	        return "redirect:" + paymentUrl;
			

		}

	}
	
	
	@GetMapping("/vnpay_return")
    public String vnPayReturn(
            @RequestParam Map<String, String> requestParams,
            HttpServletRequest request) {
        
        // Lấy thông tin trả về từ VNPAY
        String vnp_ResponseCode = requestParams.get("vnp_ResponseCode");
        String vnp_Amount = requestParams.get("vnp_Amount");
        String vnp_TxnRef = requestParams.get("vnp_TxnRef");
        String vnp_SecureHash = requestParams.get("vnp_SecureHash");
        // Các tham số khác nếu cần

        // Kiểm tra tính hợp lệ của vnp_SecureHash và vnp_ResponseCode
        if ("00".equals(vnp_ResponseCode)) {
            // Thanh toán thành công
            // Thực hiện logic lưu hóa đơn và chi tiết hóa đơn
            double total = Double.valueOf(vnp_Amount) / 100; // Chuyển đổi số tiền về đơn vị tiền tệ

            // Giả sử các thông tin khác cần thiết đã được lưu trữ trong session hoặc có thể lấy từ request
            // Ví dụ: tài khoản người dùng, sản phẩm, địa chỉ giao hàng, phí vận chuyển, ghi chú, phương thức thanh toán
            
//            TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
            Account account = accountRepository.findById(1).get();
            
            if(isBoughtFromCart) {
            	List<CartDetail> listCTGH = sessionService.getAttribute("listCTGH");
            	
//            	String note = sessionService.getAttribute("note");// Lấy từ session hoặc request
            	
                double phiVanChuyen =  Double.parseDouble(sessionService.getAttribute("phiVanChuyen"));// Lấy từ session hoặc request
                boolean phuongThucThanhToan = false; // Chắc chắn đây là thanh toán online
                
                List<Address> listDiaChi = addressRepository.findByAccount(account);
                Address diaChi = new Address();
                for (Address dc : listDiaChi) {
                    if (dc.isStatus()) {
                        diaChi = dc;
                        break;
                    }
                }
                ShippingStatus trangThaiHoaDon = new ShippingStatus();
                List<ShippingStatus> listTTHD = shippingStatusRepository.findAll();
                for (ShippingStatus tthd : listTTHD) {
                    if (tthd.getName().equals("Đã đặt hàng")) {
                        trangThaiHoaDon = tthd;
                    }
                }
                //do từ vn pay trả về đã tính luôn phí vận chuyển, nên khi trả về phải trừ phí vận chuyển ra để tổng tiền ko cộng vào nữa
                double realAmount = total - phiVanChuyen;
                // Thêm hóa đơn	
                Order hoaDon = new Order();
                hoaDon.setAddress(diaChi);
                hoaDon.setTotal(realAmount);
                hoaDon.setShippingFee(Double.valueOf(phiVanChuyen));
                hoaDon.setAccount(account);
                hoaDon.setShippingStatus(trangThaiHoaDon);
                
                // Lấy ngày hiện tại
                LocalDate currentDate = LocalDate.now();
                // Định dạng ngày tháng theo định dạng "dd-MM-yyyy"
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = currentDate.format(formatter);
                hoaDon.setOrderDate(formattedDate);
                
    			hoaDon.setPaymentMethod(false);
    			hoaDon.setPaymentStatus(true);
                
                orderRepository.saveAndFlush(hoaDon);
                
                
                //Duyệt qua list chi tiết giỏ hàng lấy từ session và thêm vào chi tiết hóa đơn
                //Thêm xong lưu lại và xóa giỏ hàng
                for(CartDetail get : listCTGH) {
                	//Thêm chi tiết hóa đơn
                    OrderDetail chiTietHoaDon  = new OrderDetail();
                	chiTietHoaDon.setOrder(hoaDon);
                	chiTietHoaDon.setProductDetail(get.getProductDetail());
                	chiTietHoaDon.setQuantity(get.getQuantity());
                	orderDetailRepository.saveAndFlush(chiTietHoaDon);
                	cartDetailRepository.delete(get);
                }
                
                //LƯU THÔNG TIN GIAO DỊCH ONLINE VÀO TRANSACTIONS
                Transaction transaction = new Transaction();
                transaction.setAmountMoney(total);
                transaction.setAccount(account);
                transaction.setOrder(hoaDon);
                transaction.setTransactionDate(new Date());
                transaction.setTransactionStatus(true);
                transaction.setAccountNumber("9704198526191432198");
                transaction.setFullName(account.getFullName());
                transaction.setTransactionCode(requestParams.get("vnp_TransactionNo"));
                
                transactionRepository.saveAndFlush(transaction);

                //trước khi chuyển hướng về xóa đi các session

                // Chuyển hướng về trang thành công với mã hóa đơn
                return "redirect:/user/cart/pay-success/"+ hoaDon.getId(); 
                
            }
            else {
            	//DÀNH CHO TRƯỜNG HỢP THANH TOÁN ONLINE VỚI HÌNH THỨC MUA NGAY
            	return null;
            }
            
            
        } else {
            // Xử lý khi thanh toán không thành công
        	return "redirect:/user/cart/pay-failed";
        }

    }
	
	
	@GetMapping("cart/pay-success/{id}")
	public String paySuccessCart(Model model,@PathVariable("id")Integer idHD) {
		Order order = orderRepository.findById(idHD).get();
		model.addAttribute("order", order);		
		return "clients/paysuccess";
	}
	
	@GetMapping("shoppingcart/delete/{idCartDetail}")
	public String deleteCart(@PathVariable("idCartDetail") Integer idCartDetail) {
		cartDetailRepository.deleteById(idCartDetail);
		return "redirect:/user/cart";
	}

	@PostMapping("shoppingcart/deleteall")
	public String deleteAllCart(@RequestParam("idProduct") Integer[] cartId) {
		for (Integer cart : cartId) {
			cartDetailRepository.deleteById(cart);
		}
		return "redirect:/user/cart";
	}
}
