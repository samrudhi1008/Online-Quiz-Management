package app;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import java.io.File;
import controller.*;
import util.HibernateUtil;
import org.apache.catalina.servlets.DefaultServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        // Test Hibernate initialization
        System.out.println("Initializing Hibernate...");
        HibernateUtil.getSessionFactory(); // This should create tables if configured correctly
        System.out.println("Hibernate initialized");

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8088);

        String projectRoot = new File(".").getCanonicalPath();
        String docBase = new File(projectRoot, "src/main/webapp").getAbsolutePath();
        File docBaseFile = new File(docBase);
        System.out.println("Project Root: " + projectRoot);
        System.out.println("DocBase: " + docBase);
        System.out.println("DocBase exists: " + docBaseFile.exists());
        System.out.println("DocBase is directory: " + docBaseFile.isDirectory());

        Context context = tomcat.addContext("", docBase);
        context.addWelcomeFile("/auth");

        // Add DefaultServlet for static files
        Tomcat.addServlet(context, "default", new DefaultServlet());
        context.addServletMappingDecoded("/*", "default");

        // Set up resources for static file access
        context.setResources(new org.apache.catalina.webresources.StandardRoot(context));

        // Add servlet mappings
        tomcat.addServlet("", "LoginServlet", new controller.LoginServlet());
        tomcat.addServlet("", "RegisterServlet", new controller.RegisterServlet());
        tomcat.addServlet("", "QuestionServlet", new controller.QuestionServlet());
        

        context.addServletMappingDecoded("/login/*", "LoginServlet");
        context.addServletMappingDecoded("/register/*", "RegisterServlet");
        context.addServletMappingDecoded("/question/*", "QuestionServlet");

        // Start Tomcat server
        tomcat.getConnector();
        System.out.println("Starting Tomcat...");
        tomcat.start();
        System.out.println("Tomcat started on http://localhost:8088");
        tomcat.getServer().await();
    }
}
