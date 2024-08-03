/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.common;

import hrms.model.login.LoginUserBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Manoj PC
 */
public class UserAuthenticatedFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;

        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        //res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        //res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        //res.setDateHeader("Expires", 0); // Proxies.
        String requestURI = req.getRequestURL() + "<>";
        String contextPath = req.getContextPath();
        String arrTemp[] = requestURI.split("/");

        String pageURL = arrTemp[arrTemp.length - 1];
        pageURL = pageURL.replace("<>", "");
        if (session == null || session.getAttribute("LoginUserBean") == null) {

            if (pageURL.equals("") || pageURL.equals("index.htm")) {
                
                //res.sendRedirect("index.htm");
                filterChain.doFilter(req, res);
            } else {
                String arrTemp1[] = pageURL.split("\\.");
                String ext = arrTemp1[arrTemp1.length - 1];
                if (ext.equals("htm")) {
                   if(pageURL.equals("login.htm"))
                   {
                        filterChain.doFilter(req, res);
                   }
                   else
                   {
                       res.sendRedirect("index.htm");
                   }
                } else {
                    filterChain.doFilter(req, res);
                }
            }            
        } else {            
            if (pageURL.equals("") || pageURL.equals("index.htm")) {
                LoginUserBean lub = new LoginUserBean();
                lub = (LoginUserBean) session.getAttribute("LoginUserBean");                
                if(lub.getLoginusertype() != null && (lub.getLoginusertype().equals("A") || lub.getLoginusertype().equals("G"))){
                    res.sendRedirect("login.htm");
                }else{
                    filterChain.doFilter(req, res);
                }
            }else if(pageURL.equals("logout.htm")){
                session.invalidate();
                res.sendRedirect("index.htm");
            }else{
                filterChain.doFilter(req, res);
            }
            
        }
        //filterChain.doFilter(req, res);
    }

    public void destroy() {

    }

}
