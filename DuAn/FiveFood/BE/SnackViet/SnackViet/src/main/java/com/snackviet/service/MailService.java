package com.snackviet.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	@Autowired
	JavaMailSender sender;

	// Sử dụng BlockingQueue để quản lý hàng đợi email
    private final BlockingQueue<MimeMessage> queue = new LinkedBlockingQueue<>();

	public boolean sendOtp(String to, String subjec, String content) {
		MimeMessage mimeMessage = sender.createMimeMessage();	
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subjec);
			mimeMessageHelper.setText(content);
            queue.add(mimeMessage); // Đẩy email vào hàng đợi
			return true;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}

	public boolean sendOrderCancelledNotification(String to, Integer orderId,String reason) {
        String subject = "Thông báo: Đơn hàng đã bị hủy";
        String content = "Kính gửi khách hàng,\n\n"
                        + "Đơn hàng của bạn với mã đơn hàng " + orderId + " đã bị hủy với lý do " + reason +"\n"
                        + "Nếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi.\n\n"
                        + "Trân trọng,\nSnackViet";
        return sendNotification(to, subject, content);
    }

    public boolean sendOrderCancelledNotificationForOnlinePayment(String to, Integer orderId, String reason) {
        String subject = "Thông báo hủy đơn hàng và hỗ trợ hoàn tiền";
        String content = "Kính gửi Quý khách hàng,\n\n"
                        + "Chúng tôi xin thông báo rằng đơn hàng của Quý khách với mã đơn hàng " + orderId + " đã bị hủy vì lý do: " + reason + ".\n\n"
                        + "SnackViet thành thật xin lỗi vì sự bất tiện này. Để hỗ trợ hoàn tiền, vui lòng liên hệ với bộ phận chăm sóc khách hàng qua số điện thoại 0799607455.\n\n"
                        + "Chúng tôi rất trân trọng sự thông cảm của Quý khách và cam kết mang lại trải nghiệm tốt nhất trong những lần mua sắm tiếp theo.\n\n"
                        + "Trân trọng,\n"
                        + "Đội ngũ SnackViet";
        return sendNotification(to, subject, content);
    }

    public boolean sendOrderDeliveredNotification(String to, Integer orderId) {
        String subject = "Thông báo: Đơn hàng đã được giao thành công";
        String content = "Kính gửi khách hàng,\n\n"
                        + "Đơn hàng của bạn với mã đơn hàng " + orderId + " đã được giao thành công. "
                        + "Cảm ơn bạn đã mua sắm tại SnackViet!\n\n"
                        + "Trân trọng,\nSnackViet";
        return sendNotification(to, subject, content);
    }

    public boolean sendNewAdmin(String to, String pass) {
        String subject = "Thông báo: Đã tạo thành công tài khoản quản trị";
        String content = "Kính gửi,\n\n"
                        + "Mật khẩu được tạo của bạn là " + pass + " . "
                        + "Lưu ý hãy tự đổi lại mật khẩu tại giao diện cập nhật thông tin nhé!\n\n"
                        + "Trân trọng,\nSnackViet";
        return sendNotification(to, subject, content);
    }

    private boolean sendNotification(String to, String subject, String content) {
        MimeMessage mimeMessage = sender.createMimeMessage();    
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);
			queue.add(mimeMessage); // Đẩy email vào hàng đợi
			System.out.println("Email added to queue: " + subject); // Log thêm vào hàng đợi
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	
	
	@Scheduled(fixedDelay = 1000)
    public void run() {
        int success = 0, error = 0;
        while (!queue.isEmpty()) {
            MimeMessage message = queue.poll(); // Lấy email ra khỏi hàng đợi
            if (message != null) {
                try {
                    sender.send(message);
                    success++;
                } catch (Exception e) {
                    error++;
                    e.printStackTrace();
                }
            }
        }
    }

}
