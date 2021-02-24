package com.example.rs.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: R K Rahu
 */

@RestController
public class HelloController {

    @GetMapping(value = "/Hello")
    public String getResourceHi() {
        return "Hi Authorized Resource";
    }
}
