package com.intera.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Freemahn on 7/9/2017.
 */
@javax.servlet.annotation.WebServlet(name = "LoginServlet", urlPatterns = "/login")
public class LoginServlet extends javax.servlet.http.HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        logger.info("LoginServlet: POSTED");
        HttpSession session = request.getSession();
        String username = (String)request.getParameter("name");
        logger.info("LoginServlet: " + username);
        session.setAttribute("name", username);
        response.sendRedirect("/admin/command");
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("login.html");
        view.forward(request, response);

    }
}
