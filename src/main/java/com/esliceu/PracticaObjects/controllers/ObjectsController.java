package com.esliceu.PracticaObjects.controllers;

import com.esliceu.PracticaObjects.forms.BucketForm;
import com.esliceu.PracticaObjects.forms.ObjectForm;
import com.esliceu.PracticaObjects.model.Bucket;
import com.esliceu.PracticaObjects.model.File;
import com.esliceu.PracticaObjects.model.Objects;
import com.esliceu.PracticaObjects.model.User;
import com.esliceu.PracticaObjects.service.MyService;
import com.esliceu.PracticaObjects.utils.EncriptPass;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ObjectsController {
    @Autowired
    MyService myService;
    @Autowired
    HttpSession session;
    @Autowired
    EncriptPass encriptPass;


    @GetMapping("/objects")
    public String objects(Model m) {
        User user = (User) session.getAttribute("user");
        m.addAttribute("userName", user.getNickname());
        List<Bucket> bucketList = myService.allBuckets(user);
        m.addAttribute("allBuckets", bucketList);
        return "buckets";
    }

    @PostMapping("/objects")
    public String objectsPost(@Valid BucketForm bucketForm, Model m) {
        User user = (User) session.getAttribute("user");
        bucketForm.setOwner(user.getNickname());
        myService.newBucket(bucketForm.getUri(), bucketForm.getOwner());
        m.addAttribute("message", "Bucket Created");
        return "redirect:objects";
    }

    @GetMapping("/objects/{bucketName}")
    public String buckets(@PathVariable String bucketName, Model m) {
        User user = (User) session.getAttribute("user");
        m.addAttribute("bucketName", bucketName);
        m.addAttribute("userName", user.getNickname());
        if (bucketName != "") {
            Bucket bucket = myService.getBucket(bucketName, user.getNickname());
            session.setAttribute("bucket", bucket);
            session.setAttribute("bucketName", bucketName);
            List<Objects> objectsList = myService.allObjects(user, bucket);
            m.addAttribute("allObjects", objectsList);
            List<String> objectUris = new ArrayList<>();
            for (Objects o : objectsList) {
                String uri = (String) o.getUri().split("/")[1];
                if (!uri.contains(".")) {
                    uri = "/" + uri + "/";
                }else{
                    uri = "/" +uri;
                }
                if(!objectUris.contains(uri)) {
                    objectUris.add(uri);
                }

            }
            m.addAttribute("allUris", objectUris);
        }
        return "objects";
    }

    @PostMapping("/objects/{bucketName}")
    public String bucketsPost(@Valid ObjectForm objectForm, @RequestParam("file") MultipartFile file,Model m) {
        byte[] arrayBytes = null;

        Bucket bucket = (Bucket) session.getAttribute("bucket");
        m.addAttribute("bucketName",bucket.getUri());
        try {
            arrayBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String hash = String.valueOf(Arrays.hashCode(arrayBytes));
        File createdFile = null;

        if (!myService.fileOnDb(hash)) {
            myService.newFile(arrayBytes, arrayBytes.length, hash);
        }
        createdFile = myService.getFile(hash);
        createdFile.setVersion(createdFile.getVersion() + 1);
        String uri = objectForm.getPath() + file.getOriginalFilename();
        Objects object = myService.newObject(bucket.getId(), uri, Timestamp.from(Instant.now()), bucket.getOwner(), Timestamp.from(Instant.now()), file.getContentType());
        myService.refFileToObject(object, createdFile);
        return "redirect:" + bucket.getUri();
    }

    @GetMapping("/objects/{bucketName}/**")
    public String getobject(@PathVariable String bucketName,HttpServletRequest req, Model m) {
        Bucket bucket = (Bucket) session.getAttribute("bucket");
        String s = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String sSubstring = s.substring(0,s.length()-1);
        m.addAttribute("bucketName",sSubstring);
        s = s.substring(s.indexOf("/")+1);
        s = s.substring(s.indexOf("/")+1);
        s = s.substring(s.indexOf("/")+1);

        s = s.replace("%20", " ");
        // Detectar si és un directori o un objecte. Els directoris sempre acaben en "/"

        if (s.endsWith("/")) {
            // directori
            User user = (User) session.getAttribute("user");
            List<Objects> objectsList = myService.allObjects(user, bucket);
            List<Objects> objectsListFinal = new ArrayList<>();
            for (int i = 0; i < objectsList.size(); i++) {
                if (objectsList.get(i).getUri().contains(s)){
                    objectsListFinal.add(objectsList.get(i));
                }
            }
            m.addAttribute("allObjects", objectsListFinal);
            List<String> objectUris = new ArrayList<>();
            for (Objects o : objectsListFinal) {
                if (!s.startsWith("/")){
                    s= "/"+s;
                }
                String uri = o.getUri().split(s)[1];
                uri = uri.split("/")[0];
                if (!uri.contains(".")) {
                    uri = "/" + uri + "/";
                }else{
                    uri = "/" +uri;
                }
                if(!objectUris.contains(uri)) {
                    objectUris.add(uri);
                }
            }
            m.addAttribute("allUris", objectUris);
            System.out.println("Directori");
            return "folder";
        } else {
            // objecte
            if (!s.startsWith("/")){
                s = "/"+s;
            }
            Objects o = myService.getObject(bucket.getId(), s);
            m.addAttribute("object", o);
            return "oneobject";
        }
    }
//    @GetMapping("")
//    public ResponseEntity<byte[]> download(){
//        byte[] content = file.getContent();
//        String name = object.getName();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentLength(content.length);
//        headers.setContentType(MediaType.valueOf(""));
//        headers.set("Content-disposition","attachment ;filename = "+ name);
//        return new ResponseEntity<>(content,headers, HttpStatus.OK);
//    }

}
