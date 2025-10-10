package com.ViviEstu.alojamiento.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class LodgingController {
    @GetMapping
    public String health() { return "alojamiento-ok"; }
}
