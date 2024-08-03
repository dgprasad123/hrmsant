/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.master;

import hrms.dao.master.ModuleDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.master.Module;
import java.io.PrintWriter;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas Jena
 */
@Controller
@SessionAttributes("LoginUserBean")
public class ModuleController {
    @Autowired
    ModuleDAO moduleDAO;
    
    @RequestMapping(value = "moduleList", method = RequestMethod.GET)
    public String moduleList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {//
        String path = "/master/ModuleList";
        model.addAttribute("modulelist", moduleDAO.getModuleList());
        return path;
    }
    @ResponseBody
    @RequestMapping(value = "getModuleList", method = {RequestMethod.GET, RequestMethod.POST})
    public void getModuleGroupList(HttpServletRequest request, HttpServletResponse response, @RequestParam("modulegroup") String modulegroup) {
        response.setContentType("application/json");
        PrintWriter out = null;
        try {
            List li = moduleDAO.getModuleList(modulegroup);
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
    @RequestMapping(value = "newModule", method = RequestMethod.GET)
    public ModelAndView newModule() {//
        String path = "/master/ModuleEdit";        
        return new ModelAndView(path, "command", new Module());
    }
    @RequestMapping(value = "editModule", method = RequestMethod.GET)
    public ModelAndView  editModule(ModelMap model, @ModelAttribute("moduleForm") Module moduleForm, BindingResult result, HttpServletResponse response) {//
        String path = "/master/ModuleEdit";
        
        moduleForm = moduleDAO.getModuleDetail(moduleForm.getModid());        
        return new ModelAndView(path, "command", moduleForm);
    }
    @RequestMapping(value = "saveModule", method = RequestMethod.POST)
    public String saveModule(ModelMap model, @ModelAttribute("moduleForm") Module moduleForm, BindingResult result, HttpServletResponse response) {//        
        String path = "/master/ModuleList";
        
        moduleDAO.saveModule(moduleForm);
        model.addAttribute("modulelist", moduleDAO.getModuleList());        
        return path;
    }
}
