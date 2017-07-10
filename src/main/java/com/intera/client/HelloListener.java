package com.intera.client; /**
 * Created by Freemahn on 7/9/2017.
 */

import com.intera.server.storage.Storage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionBindingEvent;


@WebListener()
public class HelloListener implements ServletContextListener,
        HttpSessionListener, HttpSessionAttributeListener {

    Storage storage;
    private static final Logger logger = LogManager.getLogger();
    // Public constructor is required by servlet spec
    public HelloListener() {
        logger.info("Constructor");
    }

    // -------------------------------------------------------
    // ServletContextListener implementation
    // -------------------------------------------------------
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("contextInitialized");
      /* This method is called when the servlet context is
         initialized(when the Web application is deployed). 
         You can initialize servlet context related data here.
      */
        storage = new Storage();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("contextDestroyed");
      /* This method is invoked when the Servlet Context 
         (the Web application) is undeployed or 
         Application Server shuts down.
      */
    }

    // -------------------------------------------------------
    // HttpSessionListener implementation
    // -------------------------------------------------------
    public void sessionCreated(HttpSessionEvent se) {
        logger.info("sessionCreated");
      /* Session is created. */
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("sessionDestroyed");
      /* Session is destroyed. */
    }

    // -------------------------------------------------------
    // HttpSessionAttributeListener implementation
    // -------------------------------------------------------

    public void attributeAdded(HttpSessionBindingEvent sbe) {
        String attributeName = sbe.getName();
        Object attributeValue = sbe.getValue();
        logger.info("Attribute added : " + attributeName + " : " + attributeValue);
      /* This method is called when an attribute 
         is added to a session.
      */
    }

    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        String attributeName = sbe.getName();
        Object attributeValue = sbe.getValue();
        logger.info("Attribute removed : " + attributeName + " : " + attributeValue);
      /* This method is called when an attribute
         is removed from a session.
      */
    }

    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        String attributeName = sbe.getName();
        Object attributeValue = sbe.getValue();
        logger.info("Attribute replaced : " + attributeName + " : " + attributeValue);
      /* This method is invoked when an attibute
         is replaced in a session.
      */
    }
}
