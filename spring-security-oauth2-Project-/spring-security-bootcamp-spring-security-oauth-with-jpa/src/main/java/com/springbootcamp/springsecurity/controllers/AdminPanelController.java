package com.springbootcamp.springsecurity.controllers;

import com.springbootcamp.springsecurity.repositories.CategoryRepository;
import com.springbootcamp.springsecurity.services.AdminPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@Controller
public class AdminPanelController {

    @Autowired
    AdminPanelService adminPanelService;

    @GetMapping("/index")
    public String index() {
            return "index";
    }

    @RequestMapping(value = "/all-categories",method = RequestMethod.GET)
    public String getAllCategories(Model model){
         model.addAttribute("totalCategories", adminPanelService.getAllCategories());
         return "index";
    }

    @RequestMapping(value ="/all-users",method =RequestMethod.GET)
    public String  getAllUsers(Model model){
        model.addAttribute("totalUsers", adminPanelService.getAllUsers());
        return "index";
    }

    @RequestMapping(value = "/all-products",method = RequestMethod.GET)
    public String getAllProducts(Model model){
        model.addAttribute("totalProducts",adminPanelService.getAllProducts());
        return "index";
    }


}