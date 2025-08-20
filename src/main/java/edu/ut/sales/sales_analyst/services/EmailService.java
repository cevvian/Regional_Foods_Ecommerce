package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.exceptions.AppException;
import edu.ut.sales.sales_analyst.exceptions.ErrorCode;
import edu.ut.sales.sales_analyst.model.dtos.requests.PaymentRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.OTP;
import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.OrderItem;
import edu.ut.sales.sales_analyst.model.entities.Payment;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.model.enums.PaymentMethod;
import edu.ut.sales.sales_analyst.repositories.OrderRepo;
import edu.ut.sales.sales_analyst.services.impl.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Service
@Transactional
public class EmailService implements IEmailService {

    private final OrderRepo orderRepo;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(OrderRepo orderRepo, JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.orderRepo = orderRepo;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendInvoiceEmail(String email, PaymentRequest paymentRequest) throws MessagingException {
        String htmlContent = templateEngine.process("invoice.html",
                buildInvoiceEmailContext(paymentRequest));
        sendEmail(email, "Hóa đơn đặc sản", htmlContent);
    }

    @Override
    public void sendOTPEmail(String email, OTP otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setFrom("dansanViet@gmail.com");
        helper.setSubject("Mã OTP Xác Thực - Đặc Sản Việt Nam");

        Context context = new Context();

        context.setVariable("otpCode", otp.getOtp());

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String formattedExpiryTime = formatter.format(otp.getExpirationTime());
        context.setVariable("expiryTime", formattedExpiryTime);

        long currentTime = System.currentTimeMillis();
        long expiryTime = otp.getExpirationTime().getTime();
        long timeLeft = expiryTime - currentTime;
        long minutesLeft = TimeUnit.MILLISECONDS.toMinutes(timeLeft);
        context.setVariable("minutesLeft", minutesLeft > 0 ? minutesLeft : 0);

        context.setVariable("currentYear", java.time.Year.now().getValue());
        context.setVariable("companyName", "Đặc Sản Việt Nam");
        context.setVariable("supportPhone", "031 333 666 9999");
        context.setVariable("supportEmail", "dansanViet@gmail.com");
        context.setVariable("websiteUrl", "https://www.dacsanviet.com");

        String htmlContent = templateEngine.process("otp", context);

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private void sendEmail(String email, String subject, String body) throws MessagingException {
        validateEmail(email);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setFrom("pdfHub5shareDoc@gmail.com");
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            log.info("Email sent successfully to '{}', subject: '{}'", email, subject);
        } catch (MailException e) {
            log.error("Failed to send email to '{}': {}", email, e.getMessage(), e);
            throw new AppException(ErrorCode.FAILED_TO_SENT_EMAIL);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void validateEmail(String email) {
        if (email == null || !isValidEmail(email)) {
            throw new AppException(ErrorCode.INVALID_EMAIL);
        }
    }

    private Context buildInvoiceEmailContext(PaymentRequest paymentRequest) {
        Order order = orderRepo.findByOrderIdWithItems(paymentRequest.getOrderId());
        String orderId = paymentRequest.getTransactionId();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'tháng' M yyyy", new Locale("vi", "VN"));

        String date = order.getOrderDate().format(formatter);
        String dueDate = order.getOrderDate().format(formatter);

        User user = order.getUser();
        String userName = user.getUserName();
        String email = user.getEmail();
        String phone = order.getAddress().getPhone();
        String address = order.getAddress().getAddressLine() + " " + order.getAddress().getProvince();

        PaymentMethod paymentMethod = paymentRequest.getMethod();
        String method = "";
        if(paymentMethod.equals(PaymentMethod.VNPAY))
            method = "Ví điện tử VNPAY";
        else if (paymentMethod.equals(PaymentMethod.CASH))
            method = "Tiền mặt";
        else
            throw new AppException(ErrorCode.PAYMENT_METHOD_UNSUPPORTED);

        List<OrderItem> orderItems = order.getOrderItems();
        String total = order.getTotalAmount().toString();
        total = total + " VND";

        Context context = new Context();
        context.setVariable("customerName", userName);
        context.setVariable("orderId", orderId);
        context.setVariable("date", date);
        context.setVariable("dueDate", dueDate);
        context.setVariable("paymentMethod", paymentMethod);
        context.setVariable("total", total);
        context.setVariable("orderItems", orderItems);
        context.setVariable("address", address);
        context.setVariable("paymentMethod", method);
        context.setVariable("phone", phone);
        context.setVariable("email", email);

        return context;
    }


}
