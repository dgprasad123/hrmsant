/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.empprofile;

import hrms.dao.PensionEmployee.PensionEmployeeDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.model.employee.PensionFamilyList;
import hrms.model.employee.PensionProfile;
import hrms.model.login.LoginUserBean;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author hp
 */
@Controller
public class EmployeePensionController {

    @Autowired
    public PensionEmployeeDAO pensionEmployeeDAO;

    @ResponseBody
    @RequestMapping(value = "/employeePensionData/{empid}", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getEmployeePensionData(HttpServletResponse response, @PathVariable("empid") String empid) throws JSONException {
        PensionProfile empDataList = pensionEmployeeDAO.getEmpPensionDet(empid);
        String profileStatus = pensionEmployeeDAO.checkProfileStatus(empid);

        if (profileStatus != null && profileStatus.equals("Y")) {
            JSONObject jobj = new JSONObject(empDataList);
            return jobj.toString();
        } else {
            JSONObject jobj = new JSONObject();
            jobj.put("message", "Please Update the Pension Profile Data In HRMS having HRMS ID/GPF AC-NO : " + empid + "  Our support team is here to help. Contact us at +91 8763545188 for further assistance.");
            return jobj.toString();
        }
    }
}
