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


    @RequestMapping(value ="/index",method =RequestMethod.GET)
    public String  getAllUsers(Model model,Model model1,Model model2,Model model3){

        model.addAttribute("allUsers", adminPanelService.getAllUsers());

        model1.addAttribute("allProducts",adminPanelService.getAllProducts());

        model2.addAttribute("allCategories", adminPanelService.getAllCategories());
        
        model3.addAttribute("totalNumberOfProducts",adminPanelService.getTotalNumberOfProductCount());
        return "index";
    }




}