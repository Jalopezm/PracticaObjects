package com.esliceu.PracticaObjects.controllers;

import com.esliceu.PracticaObjects.forms.UserForm;
import com.esliceu.PracticaObjects.model.Bucket;
import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.service.BucketService;
import com.esliceu.PracticaObjects.service.ObjectService;
import com.esliceu.PracticaObjects.service.UserService;
import com.esliceu.PracticaObjects.utils.EncriptPass;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class UserController {
    @Autowired
    BucketService bucketService;
    @Autowired
    UserService userService;
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
        if (userService.validateUser(userForm.getNickname())) {
            m.addAttribute("message", "User Already Exists Select Other User Name");
            return "signup";
        } else {
            userService.newUser(userForm.getName(),userForm.getNickname(),userForm.getEmail(), encriptPass.encritpPass(userForm.getPassword()));
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
        if (userService.logUser(userForm.getNickname(), encriptPass.encritpPass(userForm.getPassword()))) {
            User user = userService.getUser(userForm.getNickname());
            session.setAttribute("user",user);
            m.addAttribute("message", "OK");
            m.addAttribute("userName", userForm.getNickname());
            return "redirect:/objects";
        } else {
            m.addAttribute("message", "Unknown User or Password");
        }
        return "login";
    }

    @GetMapping("/settings")
    public String settings(Model m) {
        User user = (User) session.getAttribute("user");
        if (user!= null) {
            m.addAttribute("user", user);
            m.getAttribute("user");
        }
        return "settings";
    }
    @PostMapping("/settings")
    public String settingsPost(@Valid UserForm userForm, Model m) {
        User user = (User) session.getAttribute("user");
        m.getAttribute("user");
        if (userForm.getPassword().length() < 8){
            userService.updateUser(userForm.getName(), userForm.getEmail(), user.getPassword(),user.getNickname());
        }else {
            userService.updateUser(userForm.getName(), userForm.getEmail(), encriptPass.encritpPass(userForm.getPassword()), userForm.getNickname());
        }
        session.setAttribute("user",user);
        m.addAttribute("user",user);
        return "redirect:/settings";
    }

    @GetMapping("/settings/{deleteUser}")
    public String deleteUserGet(@Valid UserForm userForm,Model m){
        return "deleteUser";
    }
    @PostMapping("/settings/{deleteUser}")
    public String deleteUserPost(@Valid UserForm userForm,Model m){
        if (userService.logUser(userForm.getName(),encriptPass.encritpPass(userForm.getPassword()))){
            User user = (User) session.getAttribute("user");
            List<Bucket> bucketList = bucketService.allBuckets(user);
            for (Bucket bucket : bucketList) {
                bucketService.deleteBucket(user, bucket);
            }
            userService.deleteUser(user.getNickname(),encriptPass.encritpPass(userForm.getPassword()));
            session.setAttribute("user",null);

            m.addAttribute("message","User Deleted");
        }else{
            m.addAttribute("message", "Unknown User or Password");
        }
        return "redirect:/";
    }

}
