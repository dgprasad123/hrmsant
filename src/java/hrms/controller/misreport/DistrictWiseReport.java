/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.misreport;

import hrms.dao.master.PostDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.payroll.billbrowser.BillBrowserbean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Manas Jena DistrictWiseMenInPosition
 */
@Controller
@SessionAttributes("LoginUserBean")
public class DistrictWiseReport {

    @Autowired
    PostDAO postDAO;

    @ResponseBody
    @RequestMapping(value = "getpostListDeptwise", method = RequestMethod.POST)
    public void getPostList(HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ArrayList postList = postDAO.getPostList(lub.getLogindeptcode());
        JSONArray json = new JSONArray(postList);
        out = response.getWriter();
        out.write(json.toString());
    }

    @RequestMapping(value = "districtWiseMenInPosition", method = RequestMethod.GET)
    public String getDistrictWiseMeninPositionList(ModelMap model, @ModelAttribute("BillBrowserbean") BillBrowserbean bbbean, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {//
        String path = "/misreport/DistrictWiseMenInPosition";

        return path;
    }
}
