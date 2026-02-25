package com.itis.oris.di.component;

import com.itis.oris.di.annotation.Component;
import com.itis.oris.di.annotation.Controller;
import com.itis.oris.di.annotation.GetMapping;

@Controller
@Component
public class StoreController {

    public StoreController() {
    }

    @GetMapping("/hello")
    public String hello() {
        return "<h1>Hello from Mini Spring MVC</h1>";
    }
}
