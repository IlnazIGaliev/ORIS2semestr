package com.itis.oris.web;

import com.itis.oris.annotation.GetMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private final ApplicationContext context;

    // Требование задания
    private final Map<String, Method> handlerMapping = new HashMap<>();
    private final Map<Method, Object> methodControllers = new HashMap<>();

    public DispatcherServlet(ApplicationContext context) {
        this.context = context;
        initHandlerMappings();
    }

    private void initHandlerMappings() {

        Map<String, Object> controllers =
                context.getBeansWithAnnotation(Controller.class);

        for (Object controller : controllers.values()) {

            Class<?> clazz = controller.getClass();

            for (Method method : clazz.getDeclaredMethods()) {

                if (method.isAnnotationPresent(GetMapping.class)) {

                    String path = method.getAnnotation(GetMapping.class).value();

                    handlerMapping.put(path, method);
                    methodControllers.put(method, controller);
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getRequestURI();

        Method method = handlerMapping.get(path);

        // Если не найдено — пробуем отдать статику
        if (method == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().println("404 Not Found");
            return;
        }

        try {
            Object controller = methodControllers.get(method);

            method.invoke(controller, req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}