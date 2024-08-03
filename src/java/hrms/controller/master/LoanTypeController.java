/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.LoanTypeDAO;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author lenovo pc
 */
@Controller
public class LoanTypeController {

    @Autowired
    LoanTypeDAO loanTypeDao;

    @RequestMapping(value = "getLoanTypeListJSON.htm", method = {RequestMethod.GET,RequestMethod.POST})
    public void getLoanTypeListJSON(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List loantypelist = loanTypeDao.getLoanTypeList();
            json = new JSONArray(loantypelist);
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
