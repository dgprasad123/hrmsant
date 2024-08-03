/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.mergeHRMSID;

import hrms.dao.mergeHRMSID.mergeHrmsIDDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.mergeHRMSID.mergeDuplicateHrmsidForm;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class mergeDuplicateHrmsIDController {

    @Autowired
    mergeHrmsIDDAO mergeIDDao;

    public void setMergeIDDao(mergeHrmsIDDAO mergeIDDao) {
        this.mergeIDDao = mergeIDDao;
    }

    @RequestMapping(value = "mergeHrmsid.htm")
    public ModelAndView getMergingPage(ModelMap model, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("mergeDuplicateHrmsid") mergeDuplicateHrmsidForm mergingForm) {
        ModelAndView mav = new ModelAndView();
        if (lub.getLoginusertype().equals("A")) {
            mav = new ModelAndView("/MergeHRMSID/mergeDuplicateHrmsid");
        } else {
            mav.setViewName("/under_const");
        }
        return mav;
    }

    @RequestMapping(value = "mergeDuplicateHrmsid", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getMergingDetails(ModelMap model, HttpServletResponse response, @ModelAttribute("mergeDuplicateHrmsid") mergeDuplicateHrmsidForm mergingForm,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam Map<String, String> requestParams) throws IOException {
        ModelAndView mav = new ModelAndView();
        if (lub.getLoginusertype().equals("A")) {
            mav = new ModelAndView("/MergeHRMSID/mergeDuplicateHrmsid");
            mergingForm = mergeIDDao.getEmpData(mergingForm.getSrcEmpId(), mergingForm.getFinalEmpId());
            mav.addObject("mform", mergingForm);
            mav.addObject("usrtype", lub.getLoginusertype());
            mav.addObject("msgPreviouslyMerged", mergingForm.getMsgCompleted());
        } else {
            mav.setViewName("/under_const");
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "viewLastSalaryJSON", method = {RequestMethod.GET, RequestMethod.POST})
    public void viewLastSalary(ModelMap model, HttpServletResponse response, @ModelAttribute("mergeDuplicateHrmsid") mergeDuplicateHrmsidForm mergingForm, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @RequestParam("empid") String empid) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try {
            List lastSalList = mergeIDDao.getLastSalary(empid);
            json = new JSONArray(lastSalList);
            out = response.getWriter();
            out.write(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.flush();
            out.close();
        }
    }

    @RequestMapping(value = "mergeBothHrmsid", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView mergeBothHrmsid(ModelMap model, HttpServletResponse response, @ModelAttribute("mergeDuplicateHrmsid") mergeDuplicateHrmsidForm mergingForm,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("dupId") String dupId, @RequestParam("finId") String finId) throws IOException {
        ModelAndView mav = new ModelAndView();
        if (lub.getLoginusertype().equals("A")) {
            mav = new ModelAndView("/MergeHRMSID/mergeDuplicateHrmsid");
            mergingForm = mergeIDDao.mergeBothHrmsId(dupId, finId);
            mav.addObject("msgForBank", mergingForm.getMsgForbank());
            mav.addObject("msgForDesignation", mergingForm.getMsgForDesination());
            mav.addObject("msgCompleted", mergingForm.getMsgCompleted());
        } else {
            mav.setViewName("/under_const");
        }
        return mav;
    }

}
