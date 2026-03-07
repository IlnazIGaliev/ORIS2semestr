package com.itis.oris.di.component;

import com.itis.oris.di.annotation.Controller;
import com.itis.oris.di.annotation.GetMapping;
import jakarta.servlet.http.*;

import java.io.IOException;

@Controller
public class IndexController {

    @GetMapping("/index")
    public void index(HttpServletRequest req,
                      HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println("<h2>Главная страница</h2>");
    }
}