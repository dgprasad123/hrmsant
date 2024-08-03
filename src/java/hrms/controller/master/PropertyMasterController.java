/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.PropertyMasterDAO;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes("LoginUserBean")
public class PropertyMasterController {
    @Autowired
    public PropertyMasterDAO propertyMasterDAO;
    
    @ResponseBody
    @RequestMapping(value = "getPropertyMaster", method = {RequestMethod.GET, RequestMethod.POST})
    public void getPropertyMaster(HttpServletRequest request, HttpServletResponse response, @RequestParam("propertyTypeId") int propertyTypeId) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = propertyMasterDAO.getPropertyMasterList(propertyTypeId);
            JSONArray json = new JSONArray(li);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            out.flush();
            out.close();
        }
    }
    @ResponseBody
    @RequestMapping(value = "getPropertyOwnerList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getPropertyOwnerList(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = propertyMasterDAO.getPropertyOwnerList();
            JSONArray json = new JSONArray(li);
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
