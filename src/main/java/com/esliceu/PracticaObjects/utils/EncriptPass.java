package com.esliceu.PracticaObjects.utils;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Component
public class EncriptPass {

    public String encritpPass(String password) {
        String encreipted = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8).toString();
        return encreipted;
    }
}
