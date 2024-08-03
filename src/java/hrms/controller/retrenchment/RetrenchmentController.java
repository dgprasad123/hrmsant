/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.retrenchment;

import hrms.dao.retrenchment.RetrenchmentDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.retrenchment.Retrenchment;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
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
public class RetrenchmentController {
    @Autowired
    public RetrenchmentDAO retrenchmentDAO;

    @RequestMapping(value = "Retrenchment")
    public String ResignationList(@ModelAttribute("SelectedEmpObj") Users lub) {
        return "/retrenchment/Retrenchment";

    }
    
    @RequestMapping(value = "saveRetrenchmentData")
    public void saveTerminationData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("retrenchmentForm") Retrenchment retrenchmentForm) throws ParseException {

        response.setContentType("application/json");
        PrintWriter out = null;
        int retrenchmentdata = 0;

        try {
            retrenchmentForm.setEmpid(selectedEmpObj.getEmpId());
            retrenchmentdata = retrenchmentDAO.insertRetrenchmentData(retrenchmentForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     @RequestMapping(value = "editRetrenchment")
    public void editRetrenchment(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {
        Retrenchment retrenchmentform = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            String retrenchmentformId = requestParams.get("notId");
            retrenchmentform = retrenchmentDAO.editRetrenchment(retrenchmentformId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @RequestMapping(value = "deleteRetrenchment")
    public String deleteRetrenchment(@RequestParam("notId") String notId) {
        int deletestatus = 0;
        deletestatus = retrenchmentDAO.deleteRetrenchment(notId);
        
          return "/retrenchment/Retrenchment";
    }
    @RequestMapping(value = "updateRetrenchment")
    public void updateRetrenchmentData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("retrenchmentForm") Retrenchment retrenchmentForm) throws ParseException {
        int retrenchmentFormdata = 0;
        try {
            retrenchmentForm.setEmpid(selectedEmpObj.getEmpId());
            retrenchmentFormdata = retrenchmentDAO.updateRetrenchmentData(retrenchmentForm);
           

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
}
