package org.rtest.testapp.webapp.server.embedded.main;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
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
    private void startTomcat() throws ServletException, LifecycleException{
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        // create an AJP connector
        Connector ajpConnector = createAjpConnector();
        tomcat.getService().addConnector(ajpConnector);
        System.out.println("Tomcat is about to start for context: " + new File(".").getAbsolutePath());
        Context context = tomcat.addWebapp("/sample-app", new File(".").getAbsolutePath());
        configureWebAppContext(context);
        tomcat.start();
        System.out.println("Tomcat Started...");
        tomcat.getServer().await();
    }

    private void configureWebAppContext(Context context) throws ServletException {
        context.addParameter("contextClass", AnnotationConfigWebApplicationContext.class.getName());
        context.addParameter("contextConfigLocation", AppConfig.class.getName());
        context.addApplicationListener(ContextLoaderListener.class.getName());
        context.addApplicationListener(RTestIntegrationListener.class.getName());
        context.setParentClassLoader(Main.class.getClassLoader());
    }

    private Connector createAjpConnector() {
        Connector ajpConnector = new Connector("AJP/1.3");
        ajpConnector.setPort(8009);
        ajpConnector.setRedirectPort(8080);
        ajpConnector.setSecure(false);
        return ajpConnector;
    }

    public static void main(String[] args) throws ServletException, LifecycleException {
       Main main = new Main();
       main.startTomcat();
    }
}
