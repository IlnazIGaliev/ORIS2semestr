package com.itis.oris;

import com.itis.oris.config.AppConfig;
import com.itis.oris.web.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;

public class TestEmbeddedTomcat {

    public static void main(String[] args) throws Exception {

        // Spring Context
        ApplicationContext springContext =
                new AnnotationConfigApplicationContext(AppConfig.class);

        // Embedded Tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");

        Connector connector = new Connector();
        connector.setPort(8090);
        tomcat.setConnector(connector);

        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();

        Context tomcatContext =
                tomcat.addContext(contextPath, docBase);

        // Dispatcher
        DispatcherServlet servlet =
                new DispatcherServlet(springContext);

        tomcat.addServlet(contextPath,
                "dispatcherServlet",
                servlet);

        tomcatContext.addServletMappingDecoded(
                "/*",
                "dispatcherServlet"
        );

        tomcat.start();
        tomcat.getServer().await();
    }
}