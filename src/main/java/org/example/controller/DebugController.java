package org.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

@Controller
public class DebugController {

    @GetMapping("/whoami")
    @ResponseBody
    public String whoAmI(Authentication authentication) {
        if (authentication == null) {
            return "Auth = null";
        }
        return "Name = " + authentication.getName() +
               " | Authorities = " + authentication.getAuthorities();
    }
}
