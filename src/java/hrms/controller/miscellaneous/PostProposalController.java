/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.miscellaneous;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.miscellaneous.PostProposalDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.miscellaneous.PostProposal;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manas
 */
@Controller
@SessionAttributes("LoginUserBean")
public class PostProposalController {
    @Autowired
    DepartmentDAO departmentDAO;
    
    @Autowired
    PostProposalDAO postproposalDao;

    @RequestMapping(value = "getProposalList")
    public ModelAndView getProposalList(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("postProposal") PostProposal postProposal, BindingResult result, HttpServletResponse response) {                
        ModelAndView mav = new ModelAndView("/miscellaneous/PostProposal", "postProposal", postProposal);
        List li = postproposalDao.getPostProposalList(lub.getLoginspc());        
        mav.addObject("ProposalList", li);
        return mav;
    }

    @RequestMapping(value = "PostProposalDetail", params = {"action=PROPOSAL"})
    public String PostProposalDetail(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("postProposal") PostProposal postProposal, BindingResult result, HttpServletResponse response) {
        return "/miscellaneous/PostProposalDetails";
    }
    
    @RequestMapping(value = "chooseAuthority", method = RequestMethod.GET)
    public ModelAndView ChooseAuthority(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("postProposal") PostProposal postProposal) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/miscellaneous/ChooseAuthorityPostProposal");
        mav.addObject("departmentList", departmentDAO.getDepartmentList());
        return mav;
    }
    
    @RequestMapping(value = "submitPostProposal", params = {"action=SUBMIT"})
    public String SubmitPostProposal(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("postProposal") PostProposal postProposal) {
        return "/miscellaneous/ChooseAuthorityPostProposal";
    }

    @RequestMapping(value = "SavePostProposalDetail")
    public String SavePostProposalDetail(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("postProposal") PostProposal postProposal, BindingResult result, HttpServletResponse response) {        
        int proposalId = 0;
        postProposal.setSubmittedBySpc(lub.getLoginspc());
        postProposal.setSubmittedBy(lub.getLoginempid());
        if(postProposal.getPorposalId() != 0){
            proposalId = postProposal.getPorposalId();
        }else{
            proposalId = postproposalDao.addProposal(postProposal);
            postProposal.setPorposalId(proposalId);
        }
        postproposalDao.addProposalDetails(postProposal);

        return "/miscellaneous/PostProposal";
    }

    @RequestMapping(value = "getProposalAnnexure1")
    public String ProposalAnnexure1(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {                
        return "/miscellaneous/postproposalannexure1";
    }

    @RequestMapping(value = "getProposalAnnexure2")
    public String ProposalAnnexure2(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {        
        return "/miscellaneous/postproposalannexure2";
    }

    @RequestMapping(value = "getProposalAnnexure3")
    public String ProposalAnnexure3(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, BindingResult result, HttpServletResponse response) {        
        return "/miscellaneous/postproposalannexure3";
    }

    @RequestMapping(value = "editPostProposalDetail", method = RequestMethod.GET)
    public ModelAndView editPostProposalDetail(ModelMap model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("postProposal") PostProposal postProposal) {
        postProposal = postproposalDao.getPostProposalDetails(postProposal.getPorposalId());
        ModelAndView mav = new ModelAndView("/miscellaneous/PostProposalDetails");
        mav.addObject("postProposal", postProposal);
        return mav;
    }
}
