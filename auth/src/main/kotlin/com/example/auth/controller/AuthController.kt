package com.example.auth.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auths")
@RestController
class AuthController(

) {
    @PostMapping
    fun test() {

    }
}