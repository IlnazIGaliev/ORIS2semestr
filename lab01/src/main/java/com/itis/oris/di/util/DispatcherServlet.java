package com.itis.oris.di.util;

import com.itis.oris.di.config.Context;
import com.itis.oris.di.annotation.Controller;
import com.itis.oris.di.annotation.GetMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private Context context;
    private Map<String, Handler> handlerMapping = new HashMap<>();

    public DispatcherServlet(Context context) {
        this.context = context;
        initHandlerMappings();
    }

    private void initHandlerMappings() {
        for (Map.Entry<Class<?>, Object> bean : context.getComponents().entrySet()) {

            Class<?> clazz = bean.getKey();

            if (clazz.isAnnotationPresent(Controller.class)) {

                for (Method method : clazz.getDeclaredMethods()) {

                    if (method.isAnnotationPresent(GetMapping.class)) {

                        String path = method.getAnnotation(GetMapping.class).value();
                        handlerMapping.put(path, new Handler(bean.getValue(), method));
                    }
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getRequestURI();

        Handler handler = handlerMapping.get(path);

        if (handler == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Object result = handler.method.invoke(handler.controller);

            resp.setContentType("text/html; charset=utf-8");
            PrintWriter writer = resp.getWriter();
            writer.println(result);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private static class Handler {
        Object controller;
        Method method;

        Handler(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }
    }
}
