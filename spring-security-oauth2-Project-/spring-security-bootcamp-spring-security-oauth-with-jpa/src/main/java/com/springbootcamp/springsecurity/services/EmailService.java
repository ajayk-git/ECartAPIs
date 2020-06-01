package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.entities.order.Order;
import com.springbootcamp.springsecurity.entities.order.OrderProduct;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.repositories.OrderProductRepository;
import com.springbootcamp.springsecurity.repositories.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class EmailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Async
    public void sendEmailToCustomer(String email, String token) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("E-Cart");
        mail.setSubject("Account registration confirmation mail");
        mail.setText("To get your registration complete click here : "
                + "localhost:8080/register/confirm?token=" + token);
        mailSender.send(mail);
        log.info("Registration confirmation mail sent successfully");

    }

    @Async
    public void sendEmailToSeller(String email, String token) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("E-Cart");
        mail.setSubject("Account registration confirmation mail");
        mail.setText("To get your registration complete click here : "
                + "localhost:8080/register/confirm?token=" + token);
        mailSender.send(mail);
        log.info("Registration confirmation mail sent successfully");

    }

    @Async
    public void sendMailPasswordUpdate(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText("Your Password has been updated.");
        mailMessage.setTo(email);
        mailMessage.setFrom("E-Cart");
        mailMessage.setSubject("Alert : Password Updated");
        mailSender.send(mailMessage);
        log.info("Password Update mail sent successfully");

    }

    @Async
    public void mailNotificationAdminNewProductAdd() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText("You have a pending task to activate a new product added by seller.");
        mailMessage.setTo("ajay.kumar1@tothenew.com");
        mailMessage.setFrom("E-Cart");
        mailMessage.setSubject("Alert : Activate new product");
        mailSender.send(mailMessage);
        log.info("Product Activation  mail sent to admin successfully");
    }


    //==================send email to inform seller regarding product activation===================================
    @Async
    public void mailNotificationSellerProductActivate(String email, Product product) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText("Your Product is activated by admin.Product id : " + product.getId() + " Product Name : " + product.getName());
        mailMessage.setTo(email);
        mailMessage.setFrom("E-Cart");
        mailMessage.setSubject("Alert : Product Activation");
        mailSender.send(mailMessage);
        log.info("Product Activation  mail sent to seller successfully");

    }

    //==================send email to inform seller regarding product deactivation===================================

    @Async
    public void mailNotificationSellerProductDeactivate(String email, Product product) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText("Your Product is deactivated by admin.Product id : " + product.getId() + " Product Name : " + product.getName());
        mailMessage.setTo(email);
        mailMessage.setFrom("E-Cart");
        mailMessage.setSubject("Alert : Product Deactivation");
        mailSender.send(mailMessage);
        log.info("Product Deactivation  mail sent to seller successfully");

    }

    @Async
    public void sendAccountLockedMail(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setText("Account has been locked kindly wait for 24 hours to get unlocked your account by admin.");
        mailMessage.setTo(email);
        mailMessage.setFrom("E-Cart");
        mailMessage.setSubject("Alert : Account locked");
        mailSender.send(mailMessage);
        log.info("Account locked  mail sent to user successfully");

    }

    public void sendScheduleMailToActivateProduct(List productIdList) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setText("Kindly Activate following product Ids."+productIdList);
        mailMessage.setTo("ajay.kumar1@tothenew.com");
        mailMessage.setFrom("E-Cart");
        mailMessage.setSubject("Alert : Activate Products");
        mailSender.send(mailMessage);
        log.info("Product/User  Activation  scheduled mail sent to admin successfully");


    }

    @Async
    public void resendActivationLinkMail(String email, String confirmationToken) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("E-Cart");
        mail.setSubject("Account registration confirmation mail");
        mail.setText("To get your registration complete click here : "
                + "localhost:8080/register/confirm?token=" + confirmationToken);
        mailSender.send(mail);
        log.info("Account activation mail has sent again to user successfully");


    }

    public String sendOrderPlacedNotificationToCustomer(Long orderId) {

        String message;
        Order order=orderRepository.findById(orderId).get();
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("E-Cart");
        mail.setSubject("Order Update");
        mail.setText("Hey! , your order has been placed successfuly.");
        mail.setTo(order.getCustomer().getEmail());
        mailSender.send(mail);
        message="Mail has been sent to customer.";
        return  message;
    }

    public void sendOrderPlacedNotificationToSeller(Long orderId) {

        Order order=orderRepository.findById(orderId).get();
        log.info("Sending order placed notification mail to seller.");

        List<OrderProduct>orderProductList=orderProductRepository.findByOrderId(orderId);

        for (int i = 0; i < orderProductList.size(); i++) {
            System.out.println(orderProductList.get(i).getProductVariation().getProduct().getSeller().getEmail());
            log.info("Sending order placed notification mail to seller.");
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom("E-Cart");
            mail.setSubject("Order Update");
            mail.setText("Hey! ,Order is placed of your product by customer and OrderId is : "+orderId);
            mail.setTo(orderProductList.get(i).getProductVariation().getProduct().getSeller().getEmail());
            mailSender.send(mail);
        }

    }
}
