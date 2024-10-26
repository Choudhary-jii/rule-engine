
package com.zeotap.ruleengine.controller; // Adjust the package name based on your project's structure

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showHomePage() {
        return "template";  // Return the name of your Thymeleaf template (without the .html extension)
    }
}
