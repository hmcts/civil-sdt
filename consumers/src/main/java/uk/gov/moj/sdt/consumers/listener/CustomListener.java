package uk.gov.moj.sdt.consumers.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CustomListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("CustomListener is initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("CustomListener is destroyed");
    }
}
