package org.rtest.testapp.webapp.server.embedded.main;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.rtest.testapp.webapp.spring.beans.AppConfig;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletException;
import java.io.File;

/**
 * Created by Mark Bramnik on 08/11/2016.
 */
public class Main {
    public static void main(String[] args) throws ServletException, LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        Context context = tomcat.addWebapp("/", new File(".").getAbsolutePath());
        context.addParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        context.addParameter("contextConfigLocation", AppConfig.class.getName());
        context.addApplicationListener(ContextLoaderListener.class.getName());
        context.addApplicationListener(RTestIntegrationListener.class.getName());
        tomcat.start();
        System.out.println("Tomcat Started...");
        tomcat.getServer().await();
    }
}
