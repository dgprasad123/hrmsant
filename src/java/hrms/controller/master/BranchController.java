/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.BankDAO;
import hrms.dao.master.BranchDAO;
import hrms.model.master.Bank;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo pc
 */
@Controller
public class BranchController {

    @Autowired
    BranchDAO branchDao;
    @Autowired
    BankDAO bankDao;

    @ResponseBody
    @RequestMapping(value = "bankbranchlistJSON")
    public void getbranchList(HttpServletResponse response, @RequestParam("bankcode") String bankcode, @ModelAttribute("bankbranchModel") Bank gbank, ModelMap model) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;

        try {
            List branchlist = branchDao.getBranchList(bankcode);
            json = new JSONArray(branchlist);
            out = response.getWriter();
            out.write(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping("branchDetails")
    public ModelAndView getNewBranch(ModelMap model, @ModelAttribute("bankbranchModel") Bank gbank, @RequestParam("bankcode") String bankcode, HttpServletResponse response) throws IOException {
        String path = "/master/BranchList";
        ModelAndView mav = new ModelAndView();
        ArrayList branch_list = branchDao.getBranchList(bankcode);
        model.addAttribute("branch_list", branch_list);
        String bankName = bankDao.getBankName(bankcode);
        model.addAttribute("bankName", bankName);
        
        if (gbank.getHidBranchCode() != null && !gbank.getHidBranchCode().equals("")) {
            branchDao.updateBranch(gbank);
            
            return new ModelAndView("redirect:/branchDetails.htm?bankcode=" + bankcode);

        } else {
            if (gbank.getMdlbranchname() != null && !gbank.getMdlbranchname().equals("")) {
                branchDao.addNewBranch(gbank, bankcode);
                gbank.setMdlbranchname("");
                return new ModelAndView("redirect:/branchDetails.htm?bankcode=" + bankcode);
            }
        }
        return new ModelAndView(path, "bankbranchModel", gbank);
    }

    @RequestMapping(value = "branchDetails", params = "submit=Back")
    public ModelAndView BackToLIst(ModelMap model, HttpServletResponse response, @ModelAttribute("bankbranchModel") Bank gbank) {

        return new ModelAndView("redirect:/bankbranchController.htm");
    }

    @RequestMapping(value = "editBranch", method = {RequestMethod.GET, RequestMethod.POST})
    public void editBranchData(@ModelAttribute("bankbranchModel") Bank bnk, Map<String, Object> model, @RequestParam("branchcode") String branchcode, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            
            bnk = branchDao.getBranch(branchcode);
            bnk.setBranchcode(branchcode);
            JSONObject jos = new JSONObject(bnk);
            out = response.getWriter();
            out.write(jos.toString());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
