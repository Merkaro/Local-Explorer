package com.mka.demos.localexplorer.resources.healtchcheck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/healthcheck")
public class HealthcheckController {

    @GetMapping
    public String healthcheck(){
        return "Local Explorer is Up and Running !";
    }
}
