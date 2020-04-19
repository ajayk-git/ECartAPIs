package com.springbootcamp.springsecurity;

import com.springbootcamp.springsecurity.Entities.Users.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Component
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenid;

    private  static  final int EXPIRYTIME=24*60;

    private Date date;


    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = User.class,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(nullable = false,name = "user_id")
    private User user;


    public ConfirmationToken(User user){
        this.user=user;
        createdDate=new Date();
        confirmationToken= UUID.randomUUID().toString();
    }


    public static Date calculateExpiryDate(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Timestamp(calendar.getTime().getTime()));
        calendar.add(Calendar.MINUTE,EXPIRYTIME);
        return new Date(calendar.getTime().getTime());
    }



}


