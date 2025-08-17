package ranto.co.io.endpoint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "layout"; // Affiche layout.html avec index.html inclus
    }
}

