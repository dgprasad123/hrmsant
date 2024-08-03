/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.reemployment;

import hrms.dao.reemployment.ReemploymentDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.reemployment.Reemployment;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes({"Users", "SelectedEmpObj"})
public class ReemploymentController {
    @Autowired
    public ReemploymentDAO reemploymentDAO;
     @RequestMapping(value = "Reemployment")
    public String ReemploymentList(@ModelAttribute("SelectedEmpObj") Users lub) {
        return "/reemployment/Reemployment";
        
    }
    
     @RequestMapping(value = "GetReemploymentListJSON")
    public void GetAdditionalChargeListJSON(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam("page") int page) {

        response.setContentType("application/json");
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        List reemploymentlist = null;
        int additionalchargelistCnt = 0;
        int maxlimit = 10;
        int minlimit = maxlimit * (page - 1);
        try {
            reemploymentlist = reemploymentDAO.findAllReemployment(selectedEmpObj.getEmpId());
            json.put("total", reemploymentlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
    @RequestMapping(value = "saveReemploymentData")
    public void saveReemploymentData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("reemploymentForm") Reemployment reemploymentForm) throws ParseException {

        response.setContentType("application/json");
        PrintWriter out = null;
        int reemployment = 0;

        try {
            reemploymentForm.setEmpid(selectedEmpObj.getEmpId());
            reemployment = reemploymentDAO.insertReemploymentData(reemploymentForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     @RequestMapping(value = "editreemployment")
    public void editreemployment(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {
        Reemployment reemploymentform = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            String reemploymentId = requestParams.get("notId");
            reemploymentform = reemploymentDAO.editReemployment(reemploymentId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
     @RequestMapping(value = "deletereemployment")
    public String deletereemployment(@RequestParam("notId") String notId) {
        int deletestatus = 0;
        deletestatus = reemploymentDAO.deleteReemployment(notId);
        
         return "/reemploymen/Reemployment";
    }
     @RequestMapping(value = "updateReemploymentData")
    public void updateReemploymentData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("reemploymentForm") Reemployment reemploymentForm) throws ParseException {
        int reemployment = 0;
        try {
            reemploymentForm.setEmpid(selectedEmpObj.getEmpId());
            reemployment = reemploymentDAO.updateReemploymentData(reemploymentForm);
           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
