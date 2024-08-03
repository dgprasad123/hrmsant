/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.login;


import in.cdac.epramaan.sp.util.EpramaanConnector;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Manas Jena
 */

public class SSOSamlResponseConsumer extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServletContext context = request.getSession().getServletContext();
        String samlResponse = request.getParameter("SAMLResponse");

        EpramaanConnector epConnector = new EpramaanConnector(context);
        boolean status = epConnector.parseLoginResponse(request.getSession(), samlResponse);
        
        RequestDispatcher dispatcher = null;
        /*if (status == true) {
            //successful login, you can forward to home page
            dispatcher = context.getRequestDispatcher("/tab/ServiceConditionAdmin");
        } else {
            //login failed, you can forward login error page
            dispatcher = context.getRequestDispatcher("/index");
        }*/
        PrintWriter out = response.getWriter();

        //reading user information 
        String userName = epConnector.getServiceUserId();
        String aadharNumber = epConnector.getAadhaarNumber();
        String verifiedEmailId = epConnector.getVerifiedEmail();
        String verifiedMobileNo = epConnector.getVerifiedMobileNumber();
        
        out.println("userName:"+userName);
        out.println("aadharNumber:"+aadharNumber);
        out.println("verifiedEmailId:"+verifiedEmailId);
        out.println("verifiedMobileNo:"+verifiedMobileNo);
        //dispatcher.forward(request, response);
    }

}
