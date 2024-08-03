/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.common;

import hrms.common.HRMSServiceConfiguration;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Manas
 */
@Controller
public class RequestMonitoringController {
    @RequestMapping(value = "getTotalRequest", method = RequestMethod.GET)//
    public void getTotalRequest(HttpServletResponse response, @RequestParam ("requestType") String requestType) throws Exception{
        PrintWriter out = response.getWriter();
        out.println("<h1>"+HRMSServiceConfiguration.noofServiceBookRequestedByOffice()+"</h1>");
        out.close();
        out.flush();
    }
}
