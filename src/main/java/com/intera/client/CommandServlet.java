package com.intera.client;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

/**
 * Created by Freemahn on 7/10/2017.
 */
@WebListener(value = "com.intera.client.HelloListener")
@WebServlet(name = "com.intera.client.CommandServlet", urlPatterns = "/admin/command")
public class CommandServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String command = (String)request.getParameter("command");
        logger.info("Received command: " + command);
        logger.info("=================================");


        getServletContext().setAttribute(command, command);
        for (String key : Collections.list( getServletContext().getAttributeNames())){
            logger.info(key);
        }
        logger.info("=================================");
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Command page</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("Your name: " + request.getSession(false).getAttribute("name"));
        out.println("<form action = \"/admin/command\" method = \"POST\">");
        out.println("Enter command: <input type = \"text\" name = \"command\">");
        out.println("<input type = \"submit\" value = \"Submit\" />");
        out.println("</form>");
        out.println("</body>");
        out.println("</html>");
    }




}
