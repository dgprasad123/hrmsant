/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.login;

import in.cdac.epramaan.sp.util.EpramaanConnector;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author Manas Jena
 */
public class SSOSamlRequestCreator extends HttpServlet{
    private static final long serialVersionUID = 1L;
	
    public SSOSamlRequestCreator() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get the current application context
		ServletContext context = request.getSession().getServletContext();
		//create the instance for EpramaanConnector
		EpramaanConnector epConnector = new EpramaanConnector(context);
		//generate saml login request
		String samlRequest = epConnector.generateSSOSamlRequest();
		//post the login request to epramaan
		epConnector.sendLoginRequestToEpramaan(samlRequest, response.getOutputStream());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
