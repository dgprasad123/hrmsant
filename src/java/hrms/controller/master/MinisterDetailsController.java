/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.controller.master.MinisterDetailsController;
import hrms.dao.createEmployee.CreateEmployeeDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.MinisterDetailsDAO;
import hrms.dao.master.OfficiatingDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.MinisterDetails;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Devi
 */
@Controller
public class MinisterDetailsController {

    @Autowired
    OfficiatingDAO officiatingDAO;

    @Autowired
    DepartmentDAO departmentDAO;

    @Autowired
    public CreateEmployeeDAO createEmployeeDao;

    @Autowired
    MinisterDetailsDAO ministerDetailsDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @RequestMapping(value = "MinisterDetails.htm")
    public ModelAndView getMinisterDetailsList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("mindetailsForm") MinisterDetails mindetailsForm) {

        ModelAndView mav = null;
        try {
            List officiatinglist = officiatingDAO.getOfficiatingList();

            mav = new ModelAndView("/master/MinisterDetailsList");
            mav.addObject("Officiatinglist", officiatinglist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "MinisterDetailSearch")
    public ModelAndView searchMinister(ModelMap model, @ModelAttribute("mindetailsForm") MinisterDetails mindetailsForm) {
        ModelAndView mav = new ModelAndView("/master/MinisterDetailsList", "mindetailsForm", mindetailsForm);

        mav.addObject("Officiatinglist", officiatingDAO.getOfficiatingList());
        String officiateID = mindetailsForm.getOff_as();

        mav.addObject("MinisterDetailsList", ministerDetailsDAO.getMinisterDetailsList(officiateID));

        return mav;
    }

    @RequestMapping(value = "AddNewMinisterDetail")
    public ModelAndView addNewMinister(ModelMap model, @ModelAttribute("mindetailsForm") MinisterDetails mindetailsForm) {
        ModelAndView mav = new ModelAndView("/master/AddNewMinisterDetail", "mindetailsForm", mindetailsForm);
        
        mindetailsForm.setHidoff_as(mindetailsForm.getOff_as());    
        
        mav.addObject("Officiatinglist", officiatingDAO.getOfficiatingList());
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empTitleList", createEmployeeDao.empTitleType());
        

        return mav;
    }

    @RequestMapping(value = "editMinisterDetail")
    public ModelAndView editMinister(ModelMap model, @ModelAttribute("mindetailsForm") MinisterDetails mindetailsForm) {

        mindetailsForm = ministerDetailsDAO.editMinisterDetails(mindetailsForm);
        mindetailsForm.setHidoff_as(mindetailsForm.getOff_as());       
        String active = mindetailsForm.getActivemember();       
        mindetailsForm.setActivemember(active);
        ModelAndView mav = new ModelAndView("/master/AddNewMinisterDetail", "mindetailsForm", mindetailsForm);

        mav.addObject("Officiatinglist", officiatingDAO.getOfficiatingList());
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empTitleList", createEmployeeDao.empTitleType());
        
        //int hidlmid=mindetailsForm.getHidlmid();
        //mav.addObject("departmentName", ministerDetailsDAO.getMultipleDeptList(hidlmid));
        
        // mav.addObject("lmidwisedeptList", ministerDetailsDAO.getDeptListCode(hidlmid));
        
        return mav;
    }

    @RequestMapping(value = "saveMinisterDetail", method = {RequestMethod.GET, RequestMethod.POST}, params = {"action=Save Minister"})
    public String saveNewMinister(ModelMap model, @ModelAttribute("mindetailsForm") MinisterDetails mindetailsForm) {

        int lmid = mindetailsForm.getHidlmid();
        mindetailsForm.setHidlmid(lmid);

        if (mindetailsForm.getHidlmid() > 0) {             
            ministerDetailsDAO.updateMinisterDetails(mindetailsForm);
        } 
       else {
            ministerDetailsDAO.saveMinisterDetails(mindetailsForm);
        }     
        return "redirect:/MinisterDetails.htm";
    }

    @RequestMapping(value = "viewMinisterDetail")
    public ModelAndView viewMinister(ModelMap model, @ModelAttribute("mindetailsForm") MinisterDetails mindetailsForm) {

        mindetailsForm = ministerDetailsDAO.editMinisterDetails(mindetailsForm);
        int lmid = mindetailsForm.getHidlmid();
        String newdeptName = ministerDetailsDAO.getMultipleDeptList(lmid);
        mindetailsForm.setDeptname(newdeptName);

        ModelAndView mav = new ModelAndView("/master/viewMinisterDetail", "mindetailsForm", mindetailsForm);      
        mav.addObject("Officiatinglist", officiatingDAO.getOfficiatingList());
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        mav.addObject("empTitleList", createEmployeeDao.empTitleType());

        return mav;
    }
    
    @ResponseBody
    @RequestMapping(value = "getExistingUserid.htm")
    public void getExistingUserid(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("mindetailsForm") MinisterDetails mindetailsForm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ModelAndView mv = new ModelAndView();
        String msg = ministerDetailsDAO.getExistingUserid(mindetailsForm);
        JSONObject obj = new JSONObject();
        obj.append("msg", msg);
        out = response.getWriter();
        out.write(obj.toString());
    }

   /* @ResponseBody
    @RequestMapping(value = "savegetExistingUserid.htm")
    public void savegetExistingUserid(ModelMap model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("mindetailsForm") MinisterDetails mindetailsForm) throws IOException, JSONException {
        response.setContentType("application/json");
        PrintWriter out = null;
        ModelAndView mv = new ModelAndView();
        String msg = ministerDetailsDAO.saveMinisterDetails(mindetailsForm);          
        JSONObject obj = new JSONObject();
        obj.append("msg", msg);       
        out = response.getWriter();
        out.write(obj.toString());
    } */

}
