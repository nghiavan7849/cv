package com.snackviet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.model.ChiTietGioHang;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.ChiTietGioHangRepository;
import com.snackviet.service.MailService;
import com.snackviet.service.SanPhamService;
import com.snackviet.service.SessionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Controller
public class IndexController {

    @Autowired
    SessionService sessionService;

    @Autowired
    MailService mailService;

    @Autowired
    SanPhamService sanPhamService;

    @Autowired
    ChiTietGioHangRepository chiTietGioHangRepository;
    
    String message = "";

//    @RequestMapping("/home-index")
//    public String index() {
//    	
//        return "index";
//    }

    @RequestMapping("dang-xuat")
    public String dangXuat() {
        sessionService.removeAttribute("sessionTaiKhoan");
        return "redirect:/home-index";
    }

    @GetMapping("/home-index")
    public String index(
            @RequestParam(value = "showFullDescription", required = false, defaultValue = "false") boolean showFullDescription,
            Model model) {
        String description;
        if (showFullDescription) {
            description = "Chào mừng bạn đến với Đồ Ăn Vặt Ngon – nơi thỏa mãn vị giác của bạn với những món ăn vặt độc đáo và hấp dẫn! Chúng tôi tự hào là địa chỉ uy tín, mang đến trải nghiệm ẩm thực tuyệt vời với đa dạng các món ngon miệng và chất lượng. Với niềm đam mê sâu sắc về ẩm thực đường phố, chúng tôi đã tạo ra một không gian ấm cúng và thân thiện, nơi mà bạn có thể thưởng thức những món ăn vặt phong phú và độc đáo. Tại Đồ Ăn Vặt Ngon, chúng tôi cam kết sử dụng nguyên liệu tươi ngon nhất và tuân thủ nghiêm ngặt các tiêu chuẩn vệ sinh an toàn thực phẩm, để mang lại cho bạn trải nghiệm ẩm thực an toàn và ngon miệng nhất. Hãy đến và khám phá hương vị đặc trưng của các món ăn vặt được chế biến tinh tế từ đội ngũ đầu bếp tài năng của chúng tôi. Chúng tôi tin rằng, với không gian thoải mái, dịch vụ chuyên nghiệp và hương vị độc đáo, Đồ Ăn Vặt Ngon sẽ là điểm đến lý tưởng cho tất cả những ai yêu thích ẩm thực đường phố và muốn tận hưởng những khoảnh khắc ẩm thực đặc biệt.";
        } else {
            description = "Chào mừng bạn đến với Đồ Ăn Vặt Ngon – nơi thỏa mãn vị giác của bạn với những món ăn vặt độc đáo và hấp dẫn! Chúng tôi tự hào là địa chỉ uy tín, mang đến trải nghiệm ẩm thực tuyệt vời với đa dạng các món ngon miệng và chất lượng.";
        }
        model.addAttribute("description", description);
        model.addAttribute("showFullDescription", showFullDescription);
        model.addAttribute("message", message);

        // Thêm sản phẩm vào model
        Pageable pageable = PageRequest.of(0, 4);
        Page<SanPham> sanPhams = sanPhamService.getAllSanPhams(pageable);
        model.addAttribute("sanPhams", sanPhams.getContent());

        TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
        List<ChiTietGioHang> list = chiTietGioHangRepository.findByTaiKhoanGH(taiKhoan);
        sessionService.setAttribute("ListGioHang", list);
        
        return "index";
    }

    @PostMapping("/send-message")
    public String sendMail(Model model, @RequestParam("name") String name, @RequestParam("toEmail") String toEmail,
                           @RequestParam("content") String content) {
        if (mailService.sendOtp(toEmail, "New message from " + name, content)) {
            message = "Gửi tin nhắn thành công";
        } else {
            message = "Lỗi khi gửi tin nhắn";
        }
        return "redirect:/home-index";
    }

}
