package com.esliceu.PracticaObjects.controllers;

import com.esliceu.PracticaObjects.forms.UserForm;
import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.service.MyService;
import com.esliceu.PracticaObjects.utils.EncriptPass;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AppController {
    @Autowired
    MyService myService;
    @Autowired
    HttpSession session;
    @Autowired
    EncriptPass encriptPass;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/signup")
    public String signUpGet(Model m) {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUpPost(@Valid UserForm userForm, Model m) {
        if (myService.validateUser(userForm.getName())) {
            m.addAttribute("message", "User Already Exists Select Other User Name");
            return "signup";

        } else {
            myService.newUser(userForm.getName(), encriptPass.encritpPass(userForm.getPassword()));
            m.addAttribute("message", "OK");
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginGet(Model m) {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@Valid UserForm userForm, Model m) {
        if (myService.logUser(userForm.getName(), encriptPass.encritpPass(userForm.getPassword()))) {
            User user = new User(userForm.getName(),encriptPass.encritpPass(userForm.getPassword()));
            session.setAttribute("user",user);
            m.addAttribute("message", "OK");
            m.addAttribute("user", userForm.getName());
            return "redirect:/objects";
        } else {
            m.addAttribute("message", "Unknown User or Password");
        }
        return "login";
    }

//    @GetMapping("/settings")
//    public String settings() {
//        return "settings";
//    }
//    @GetMapping("/settings/{deleteUser}")
//    public String deleteUserGet(@Valid UserForm userForm,Model m){
//        return "deleteUser";
//    }
//    @PostMapping("/settings/{deleteUser}")
//    public String deleteUserPost(@Valid UserForm userForm,Model m){
//        if (myService.logUser(userForm.getName(),encriptPass.encritpPass(userForm.getPassword()))){
//            myService.deleteUser(userForm.getName(),encriptPass.encritpPass(userForm.getPassword()));
//            m.addAttribute("message","User Deleted");
//        }else{
//            m.addAttribute("message", "Unknown User or Password");
//        }
//        return "settings";
//    }

}
