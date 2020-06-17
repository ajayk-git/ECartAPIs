package com.springbootcamp.springsecurity.controllers;

import com.springbootcamp.springsecurity.services.AdminPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
public class AdminPanelController {

    @Autowired
    AdminPanelService adminPanelService;


    @RequestMapping(value ="/index",method =RequestMethod.GET)
    public String  getAllUsers(Model model,Model model1,Model model2,Model model3,Model model4){

        model.addAttribute("allCategories", adminPanelService.getAllCategories());

        model1.addAttribute("allUsers", adminPanelService.getAllUsers());

        model2.addAttribute("allProducts",adminPanelService.getAllProducts());

        model3.addAttribute("totalNumberOfProducts",adminPanelService.getTotalNumberOfProductCount());

        model4.addAttribute("getStats",adminPanelService.getStats);
        return "index";
    }




}