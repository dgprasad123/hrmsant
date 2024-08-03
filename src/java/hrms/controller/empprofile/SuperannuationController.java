/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.empprofile;

import hrms.dao.employee.EmployeeDAO;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SuperannuationController {

    @Autowired
    EmployeeDAO employeeDao;

    @ResponseBody
    @RequestMapping(value = "getSuperannuationData.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public void getSuperAnnuationDate(HttpServletRequest request, HttpServletResponse response, @RequestParam("empId") String empId, @RequestParam("retiredYear") String retiredYear) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONObject json = new JSONObject();

        try {
            out = response.getWriter();
            String superanDate = employeeDao.getSuperannuationdate(empId, retiredYear);
            json.put("superandate", superanDate);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
}
