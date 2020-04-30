package com.springbootcamp.springsecurity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class EmailService {

    @Autowired
    JavaMailSender mailSender;
    @Async
    public  void sendEmailToCustomer(String email ,String token){

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("imcoolajaykumar2010@gmail.com");
        mail.setSubject("Account registration confirmation mail");
        mail.setText("To get your registration complete click here : "
                +"localhost:8080/register/confirm-customer?token="+token);
        mailSender.send(mail);
    }
    @Async
    public  void sendEmailToSeller(String email ,String token){

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setFrom("imcoolajaykumar2010@gmail.com");
        mail.setSubject("Account registration confirmation mail");
        mail.setText("To get your registration complete click here : "
                +"localhost:8080/register/confirm-seller?token="+token);
        mailSender.send(mail);
    }
    @Async
    public void sendMailPasswordUpdate(String email){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setText("Your Password has been updated.");
        mailMessage.setTo(email);
        mailMessage.setFrom("imcoolajaykumar2010@gmail.com");
        mailMessage.setSubject("Alert : Password Updated");
        mailSender.send(mailMessage);
    }
    @Async
    public void mailNotificationAdminNewProductAdd(){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setText("You have a pending task to activate a new product added by seller.");
        mailMessage.setTo("ajay.kumar1@tothenew.com");
        mailMessage.setFrom("imcoolajaykumar2010@gmail.com");
        mailMessage.setSubject("Alert : Activate new product");
        mailSender.send(mailMessage);

    }

}
