/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.master;

import hrms.dao.master.LAMembersDAO;
import hrms.dao.master.OfficiatingDAO;
import hrms.model.lamembers.LAMembers;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Manas
 */
@Controller
public class LAMemberMasterController {
    @Autowired
    LAMembersDAO laMembersDAO;
    @Autowired
    OfficiatingDAO officiatingDAO;
    
    @RequestMapping(value = "getLAMemberList")
    public String getLAMemberList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub){
        model.addAttribute("officiatinglist", officiatingDAO.getOfficiatingList());
        return "/master/LAMemberList";
    }
    @ResponseBody
    @RequestMapping(value = "getLAMemberListJSON")
    public void getLAMemberListJSON(HttpServletRequest request, HttpServletResponse response,@RequestParam("officiating") String officiating) {
        response.setContentType("application/json");
        PrintWriter out = null;
        JSONArray json = null;
        try{            
            List memberlist = laMembersDAO.getLAMembersList(officiating);
            json = new JSONArray(memberlist);
            out = response.getWriter();
            out.write(json.toString());
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            out.flush();
            out.close();
        }       
    }
    @RequestMapping(value = "inActivateMember", method = RequestMethod.POST)
    public void inActivateMember(HttpServletRequest request, HttpServletResponse response,@RequestParam("lmid") int lmid) {
        laMembersDAO.inActivateMember(lmid);
    }
    @RequestMapping(value = "activateMember", method = RequestMethod.POST)
    public void activateMember(HttpServletRequest request, HttpServletResponse response,@RequestParam("lmid") int lmid) {
        laMembersDAO.activateMember(lmid);
    }
    @RequestMapping(value = "saveLAMember", method = RequestMethod.POST)
    public void saveLAMember(@ModelAttribute("LAMembers") LAMembers laMembers, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj,  HttpServletResponse response) throws IOException{        
        laMembersDAO.saveLAMember(laMembers);
    }
}
