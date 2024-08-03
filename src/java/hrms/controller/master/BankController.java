/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.BankDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Bank;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo pc
 */
@Controller
@SessionAttributes({"LoginUserBean"})
public class BankController {

    @Autowired
    BankDAO bankDao;

    @RequestMapping("bankbranchController")
    public ModelAndView getBankList(ModelMap model, HttpServletResponse response, @ModelAttribute("bankbranchModel") Bank gbank, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        ModelAndView mav = new ModelAndView();
        try {
            if (lub.getLoginusertype().equalsIgnoreCase("A")) {
                ArrayList bank_list = bankDao.getBankList();
                mav.addObject("bank_list", bank_list);
                mav.setViewName("/master/BankList");

            } else {
                mav.setViewName("/under_const");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "bankbranchController", params = {"submit=Save"})
    public ModelAndView getNewBank(ModelMap model, HttpServletResponse response, @ModelAttribute("bankbranchModel") Bank gbank) {

        bankDao.addNewBank(gbank);
        return new ModelAndView("redirect:/bankbranchController.htm");
    }

}
