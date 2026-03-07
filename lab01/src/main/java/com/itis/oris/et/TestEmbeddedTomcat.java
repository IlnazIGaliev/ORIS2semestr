package com.itis.oris.et;

import com.itis.oris.di.util.DispatcherServlet;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class TestEmbeddedTomcat {
    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        Connector conn = new Connector();
        conn.setPort(8090);
        tomcat.setConnector(conn);

        String contextPath = "";

        String docBase = new File(".").getAbsolutePath();
        Context tomcatContext = tomcat.addContext(contextPath, docBase);

        DispatcherServlet servlet =
                new DispatcherServlet(new com.itis.oris.di.config.Context());


        String servletName = "dispatcherServlet";
        tomcat.addServlet(contextPath, servletName, servlet);
        tomcatContext.addServletMappingDecoded("/*", servletName);

        try {
            tomcat.start();
            tomcat.getServer().await();

        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }

    }
}
