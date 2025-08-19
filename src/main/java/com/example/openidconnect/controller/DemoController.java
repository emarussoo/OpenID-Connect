package com.example.openidconnect.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/")
    public String home() {
        return "Benvenuto! Vai su /user per vedere i tuoi dati.";
    }

    @GetMapping("/user")
    public String user(@AuthenticationPrincipal OidcUser principal) {
        return "Ciao, " + principal.getFullName() + " (email: " + principal.getEmail() + ")";
    }
}
