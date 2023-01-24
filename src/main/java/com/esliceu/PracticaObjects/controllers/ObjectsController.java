package com.esliceu.PracticaObjects.controllers;

import com.esliceu.PracticaObjects.forms.BucketForm;
import com.esliceu.PracticaObjects.forms.ObjectForm;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class ObjectsController {
    @Autowired
    MyService myService;
    @Autowired
    HttpSession session;


    @GetMapping("/objects")
    public String objects(Model m) {
        User user = (User) session.getAttribute("user");
        m.addAttribute("userName", user.getNickname());
        List<Objects> objectsList = myService.allObjects(user);
        m.addAttribute("allObjects", objectsList);

        List<Bucket> bucketList = myService.allBuckets(user);
        m.addAttribute("allBuckets", bucketList);
        return "objects";
    }

    @GetMapping("/objects/{bucket}")
    public String buckets(Model m) {
        return "objects";
    }

    @PostMapping("/objects/{bucket}")
    public String bucketsPost(@PathVariable @Valid ObjectForm objectForm, @Valid BucketForm bucketForm, @RequestParam("file")MultipartFile file , Model m) {
        try {
           byte[] arrayBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User user = (User) session.getAttribute("user");
        bucketForm.setIdOwner(myService.getUserID(user.getNickname()));
        myService.newBucket(bucketForm.getName(), bucketForm.getIdOwner());
        myService.newObject(objectForm.getPath(),objectForm.getFitxer());
        m.addAttribute("message", "Bucket Created");
        return "objects";
    }
}
