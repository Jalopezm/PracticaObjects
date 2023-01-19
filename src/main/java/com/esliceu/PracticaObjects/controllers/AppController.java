package com.esliceu.PracticaObjects.controllers;

import com.esliceu.PracticaObjects.forms.UserForm;
import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.service.MyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AppController {
    @Autowired
    MyService myService;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }
    @GetMapping("/signup")
    public String signUpGet(Model m){
        return "signup";
    }
    @PostMapping("/signup")
    public String signUpPost(@Valid UserForm userForm, Model m){
         myService.newUser(userForm.getName(),userForm.getPassword());
         m.addAttribute("message","OK");
        return "signup";
    }

    @GetMapping("/login")
    public String loginGet(@Valid UserForm userForm,Model m) {
        myService.logUser(userForm.getName(),userForm.getPassword());
        return "login";
    }

    @PostMapping("/login")
    public String loginPost() {
        return "login";
    }
}
