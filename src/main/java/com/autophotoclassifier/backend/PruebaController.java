package com.autophotoclassifier.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prueba")
public class PruebaController {

    @GetMapping("/saludo")
    public String saludo() {
        return "Saludo";
    }
}
