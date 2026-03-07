package com.itis.oris.controller;

import com.itis.oris.annotation.GetMapping;
import com.itis.oris.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class StoreController {

    private StoreService storeService = new StoreService();

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/hello")
    public void hello(HttpServletRequest req,
                      HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println("<h1>Привет это база продуктов</h1>");
    }
}
