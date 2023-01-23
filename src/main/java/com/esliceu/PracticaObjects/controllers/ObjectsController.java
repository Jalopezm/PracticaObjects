package com.esliceu.PracticaObjects.controllers;

import com.esliceu.PracticaObjects.forms.BucketForm;
import com.esliceu.PracticaObjects.model.Bucket;
import com.esliceu.PracticaObjects.model.Objects;
import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.service.MyService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ObjectsController {
    @Autowired
    MyService myService;
    @Autowired
    HttpSession session;


    @GetMapping("/objects")
    public String objects(Model m) {
        List<Objects> objectsList = myService.allObjects((User) session.getAttribute("user"));
        m.addAttribute("allObjects", objectsList);

        List<Bucket> bucketList = myService.allBuckets((User) session.getAttribute("user"));
        m.addAttribute("allBuckets", bucketList);
        return "objects";
    }

    @GetMapping("/objects/{bucket}")
    public String buckets(Model m) {
        return "objects";
    }

    @PostMapping("/objects/{bucket}")
    public String bucketsPost(@Valid BucketForm bucketForm, Model m) {
        User user = (User) session.getAttribute("user");
        bucketForm.setIdOwner(myService.getUserID(user.getName()));
        myService.newBucket(bucketForm.getName(), bucketForm.getIdOwner());
        m.addAttribute("message", "Bucket Created");
        m.addAttribute("user", user.getName());
        return "objects";
    }
}
