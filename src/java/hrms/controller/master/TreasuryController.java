/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.BankDAO;
import hrms.dao.master.TreasuryDAO;
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

/**
 *
 * @author lenovo pc
 */
@Controller
public class TreasuryController {
    @Autowired
    TreasuryDAO treasuryDao ;
    
    @ResponseBody
    @RequestMapping(value = "getTreasuryListJSON.htm",  method = {RequestMethod.GET,RequestMethod.POST})
    public void getTreasuryListJSON(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
             List treasurylist = treasuryDao.getTreasuryList();
            json = new JSONArray(treasurylist);
            out = response.getWriter();
            out.write(json.toString());
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    @ResponseBody
    @RequestMapping(value = "getAGTreasuryListJSON.htm",  method = {RequestMethod.GET,RequestMethod.POST})
    public void getAGTreasuryListJSON(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
             List treasurylist = treasuryDao.getAGTreasuryList();
            json = new JSONArray(treasurylist);
            out = response.getWriter();
            out.write(json.toString());
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
    @ResponseBody
    @RequestMapping(value = "getSubTreasuryListJSON.htm")
    public void getParentTreasuryListJSON(HttpServletRequest request, HttpServletResponse response,@RequestParam("parentTrCode") String parentTrCode) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List subTreasurylist = treasuryDao.getSubTreasuryList(parentTrCode);
            json = new JSONArray(subTreasurylist);
            out = response.getWriter();
            out.write(json.toString());
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }
    
}
