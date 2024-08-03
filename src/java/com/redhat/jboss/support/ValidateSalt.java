/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redhat.jboss.support;

import com.google.common.cache.Cache;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Manas
 */
public class ValidateSalt implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Assume its HTTP
        HttpServletRequest httpReq = (HttpServletRequest) request;        
        // Validate that the salt is in the cache
        Cache<String, Boolean> csrfPreventionSaltCache = (Cache<String, Boolean>) httpReq.getSession().getAttribute("csrfPreventionSaltCache");
        if (request.getContentType() != null && request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1) {            
            chain.doFilter(request, response);
        } else {
            // Get the salt sent with the request
            String salt = (String) httpReq.getParameter("csrfPreventionSalt");            
            if (csrfPreventionSaltCache != null
                    && salt != null
                    && csrfPreventionSaltCache.getIfPresent(salt) != null) {

                // If the salt is in the cache, we move on
                chain.doFilter(request, response);
            } else {
                // Otherwise we throw an exception aborting the request flow
                throw new ServletException("Potential CSRF detected!! Inform a scary sysadmin ASAP.");
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
