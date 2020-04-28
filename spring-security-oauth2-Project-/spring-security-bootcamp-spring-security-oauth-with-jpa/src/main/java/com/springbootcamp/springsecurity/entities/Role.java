package com.springbootcamp.springsecurity.entities;

import com.springbootcamp.springsecurity.entities.users.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
//@Table(name = "ROLE")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
  //  @Column(name = "ROLE_ID")
    private Long id;
    private String authority;

    @ManyToMany(mappedBy = "roleList")
    private List<User> userList;



}
