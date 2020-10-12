package com.capgemini.TheaterService.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TestController {

    @GetMapping
    String helloRoot() {
        return "hello from root";
    }

    @GetMapping("error")
    String error() {
        return "Oops, some error occurred";
    }

    @GetMapping("user")
    String hello() {
        return "hello user";
    }

    @GetMapping("admin")
    String helloAdmin() {
        return "hello admin";
    }
}
