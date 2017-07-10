package com.intera.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Freemahn on 7/10/2017.
 */
@WebFilter(filterName = "TestFilter", urlPatterns = "/admin/*")
public class TestFilter implements Filter {
    private static final Logger logger = LogManager.getLogger();
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        logger.info("doFilter");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(false);
        logger.info("Session attribute name:" + session.getAttribute("name"));
        if (session.getAttribute("name") == null) {
            logger.info("Redirecting to login...");
            response.sendRedirect("/login");
        }
        chain.doFilter(req, resp);

    }

    public void init(FilterConfig config) throws ServletException {
        logger.info("Init filters");
    }

}
