package com.esliceu.PracticaObjects.controllers;

import com.esliceu.PracticaObjects.forms.BucketForm;
import com.esliceu.PracticaObjects.forms.ObjectForm;
import com.esliceu.PracticaObjects.model.*;
import com.esliceu.PracticaObjects.service.BucketService;
import com.esliceu.PracticaObjects.service.ObjectService;
import com.esliceu.PracticaObjects.utils.HashCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ObjectsController {
    @Autowired
    ObjectService objectService;
    @Autowired
    BucketService bucketService;
    @Autowired
    HttpSession session;
    @Autowired
    HashCode textToHash;

    @GetMapping("/objects")
    public String objects(Model m) {
        User user = (User) session.getAttribute("user");
        m.addAttribute("userName", user.getNickname());
        List<Bucket> bucketList = bucketService.allBuckets(user);
        m.addAttribute("allBuckets", bucketList);
        m.addAttribute("message", session.getAttribute("message"));
        session.setAttribute("message", "");
        return "buckets";
    }

    @PostMapping("/objects")
    public String objectsPost(@Valid BucketForm bucketForm, Model m) {
        if (bucketForm.getUri().startsWith("/")) {
            bucketForm.setUri(bucketForm.getUri().substring(1));
            if (bucketForm.getUri().endsWith("/")) {
                bucketForm.setUri(bucketForm.getUri().substring(0, bucketForm.getUri().length() - 1));
            }
        }
        User user = (User) session.getAttribute("user");
        Bucket bucket = bucketService.bucketOnDb(bucketForm.getUri());
        if (bucket == null) {
            if (!bucketForm.getUri().equals("")) {
                bucketForm.setOwner(user.getNickname());
                bucketService.newBucket(bucketForm.getUri(), bucketForm.getOwner());
                session.setAttribute("message", "Bucket Created");
            }
        } else {
            session.setAttribute("message", "Bucket Already Exists");
        }
        return "redirect:objects";
    }

    @GetMapping("/objects/{bucketName}")
    public String buckets(@PathVariable String bucketName, Model m) {
        User user = (User) session.getAttribute("user");
        m.addAttribute("bucketName", bucketName);
        m.addAttribute("userName", user.getNickname());
        List<String> objectUris = new ArrayList<>();
        if (!bucketName.equals("")) {
            Bucket bucket = bucketService.getBucket(bucketName, user.getNickname());
            if (bucket != null) {
                session.setAttribute("bucket", bucket);
                session.setAttribute("bucketName", bucketName);
                List<Objects> objectsList = objectService.allObjects(user, bucket);
                m.addAttribute("allObjects", objectsList);

                for (Objects o : objectsList) {
                    String uri = o.getUri().split("/")[1];
                    if (!uri.contains(".")) {
                        uri = "/" + uri + "/";
                    } else {
                        uri = "/" + uri;
                    }
                    if (!objectUris.contains(uri)) {
                        objectUris.add(uri);
                    }
                }
            }else{
                return "redirect:/objects";
            }
            m.addAttribute("allUris", objectUris);
        }
        return "objects";
    }

    @PostMapping("/objects/{bucketName}")
    public String bucketsPost(@Valid ObjectForm objectForm, @RequestParam("file") MultipartFile file, Model m) {
        byte[] arrayBytes;

        Bucket bucket = (Bucket) session.getAttribute("bucket");
        m.addAttribute("bucketName", bucket.getUri());
        try {
            arrayBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String hash = null;
        try {
            hash = textToHash.getHashSHA256(Arrays.toString(arrayBytes));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        File createdFile;
        if (!objectService.fileOnDb(hash)) {
            objectService.newFile(arrayBytes, arrayBytes.length, hash);
        } else {
            objectService.updateLink(hash);
        }

        String uri = objectForm.getPath() + file.getOriginalFilename();
        uri = bucket.getUri() + uri;
        Objects object = objectService.getObject(bucket.getId(), uri);
        int version;
        if (object == null) {
            object = objectService.newObject(bucket.getId(), uri, Timestamp.from(Instant.now()), bucket.getOwner(), Timestamp.from(Instant.now()), file.getContentType());
            version = 0;
        }
        createdFile = objectService.getFileFromHash(hash);
        version = objectService.getFileVersion(createdFile, object);
        createdFile.setVersion(version + 1);
        objectService.refFileToObject(object, createdFile);
        return "redirect:" + bucket.getUri();
    }

    @GetMapping("/objects/{bucketName}/**")
    public String getobject(HttpServletRequest req, Model m) {
        Bucket bucket = (Bucket) session.getAttribute("bucket");
        String s = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String sSubstring = s.substring(0, s.length() - 1);
        m.addAttribute("bucketName", sSubstring);
        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(s.indexOf("/") + 1);

        s = s.replace("%20", " ");
        // Detectar si és un directori o un objecte. Els directoris sempre acaben en "/"

        if (s.endsWith("/")) {
            // directori
            User user = (User) session.getAttribute("user");
            List<Objects> objectsList = objectService.allObjects(user, bucket);
            List<Objects> objectsListFinal = new ArrayList<>();
            for (Objects objects : objectsList) {
                if (objects.getUri().contains(s)) {
                    objectsListFinal.add(objects);
                }
            }
            m.addAttribute("allObjects", objectsListFinal);
            List<String> objectUris = new ArrayList<>();
            for (Objects o : objectsListFinal) {
                if (!s.startsWith("/")) {
                    s = "/" + s;
                }
                String uri = o.getUri().split(s)[1];
                uri = uri.split("/")[0];
                if (!uri.contains(".")) {
                    uri = "/" + uri + "/";
                } else {
                    uri = "/" + uri;
                }
                if (!objectUris.contains(uri)) {
                    objectUris.add(uri);
                }
            }

            m.addAttribute("allUris", objectUris);
            System.out.println("Directori");
            return "folder";
        } else {
            // objecte
            if (!s.startsWith("/")) {
                s = "/" + s;
            }
            s = bucket.getUri() + s;
            Objects o = objectService.getObject(bucket.getId(), s);
            List<File> f = objectService.getFileFromObjId(bucket, o);
            List<ObjectToFileRef> of = objectService.getFileToObject(o.getId());

            m.addAttribute("fileList", f);
            m.addAttribute("object", o);
            m.addAttribute("of", of);
            return "oneobject";
        }
    }

    @GetMapping("/download/{objid}/{fid}")
    public ResponseEntity<byte[]> download(@PathVariable String objid, @PathVariable String fid) {
        File file = objectService.getFileFromFileId(Integer.parseInt(fid));
        byte[] content = file.getBody();
        Objects object = objectService.getObjectFromObjId(Integer.parseInt(objid));
        String name = object.getUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(content.length);
        headers.setContentType(MediaType.valueOf(object.getContentType()));
        headers.set("Content-disposition", "attachment ;filename = " + name);
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @PostMapping("/deleteobj/{object}/**")
    public String deleteObj(HttpServletRequest req) {
        String object = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        object = object.substring(object.indexOf("/") + 1);
        object = object.substring(object.indexOf("/") + 1);
        System.out.println(object);
        Bucket bucket = (Bucket) session.getAttribute("bucket");
        objectService.deleteObject(object, bucket.getId());
        return "redirect:" + "/objects";
    }

    @PostMapping("/deletebucket/{bucket}")
    public String deleteBucket(HttpServletRequest req) {
        String object = (String) req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        object = object.substring(object.indexOf("/") + 1);
        object = object.substring(object.indexOf("/") + 1);
        System.out.println(object);
        User user = (User) session.getAttribute("user");
        Bucket bucket = (Bucket) session.getAttribute("bucket");
        bucketService.deleteBucket(user, bucket);
        return "redirect:" + "/objects";
    }
}
