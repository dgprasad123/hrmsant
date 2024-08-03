/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.performanceappraisal;

import hrms.common.CommonFunctions;
import hrms.dao.performanceappraisal.SIParCadreDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.parmast.SIParCadreBean;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author hp
 */
@Controller
@SessionAttributes({"LoginUserBean", "Privileges", "SelectedEmpObj", "SelectedEmpOffice", "users", "parreviewedspc"})
public class SIParCadreController {

    @Autowired
    private SIParCadreDAO sipcadredao;
    
    @RequestMapping(value = "SIParCaderControllerPage")
    public String SIParCaderControllerPage(Model model, @ModelAttribute("SIParCader") SIParCadreBean siParCadre) {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "/par/SIParCadre";
    }

    @RequestMapping(value = "SIParCaderPage.htm")
    public ModelAndView LoadSIParCaderPage(Model model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SIParCader") SIParCadreBean siParCadre) {

        ModelAndView mv = new ModelAndView();
        try {
            mv.setViewName("/par/SIParCadre");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "SIParCaderPage.htm", params = "btnSearch=Search", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView LoadSIParCaderPageWithData(Model model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SIParCader") SIParCadreBean siParCadre) {

        ModelAndView mv = new ModelAndView();
        try {
            List empDataList = sipcadredao.getSIParDetailsEmpIdWise(siParCadre.getSltSearchCriteria(), siParCadre.getSearchString());
            model.addAttribute("EmpDataList", empDataList);
            mv.setViewName("/par/SIParCadre");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "SIParCadreEditPage.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView SIParCaderPageGetData(Model model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SIParCadreEdit") SIParCadreBean siParCadre) {

        ModelAndView mv = null;
        try {
            String searchcriteria = siParCadre.getSltSearchCriteria();
            String searchString = siParCadre.getSearchString();
            String hrmsidEnc = CommonFunctions.decodedTxt(siParCadre.getHrmsId());
            SIParCadreBean empData = sipcadredao.getSIParDetailsByEmpId(hrmsidEnc);
            
            empData.setSltSearchCriteria(searchcriteria);
            empData.setSearchString(searchString);
            empData.setSearchString(hrmsidEnc);
            mv = new ModelAndView("/par/SIParCadreEdit", "SIParCadreEdit", empData);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "updateSiParCadre.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView UpdateSIParCaderData(Model model, HttpServletRequest request, @ModelAttribute("LoginUserBean") LoginUserBean lub,
            @ModelAttribute("SIParCadreEdit") SIParCadreBean siParCadre) {
        
        ModelAndView mav = null;
        try {
            String forHrmsIdEnc = CommonFunctions.decodedTxt(siParCadre.getEncHrmsId());
            String updMsg = sipcadredao.updateSIParDetailsEmpIdWise(lub.getLoginempid(), siParCadre);
            SIParCadreBean empData = sipcadredao.getSIParDetailsByEmpId(forHrmsIdEnc);
            mav = new ModelAndView("/par/SIParCadreEdit", "SIParCadreEdit", empData);
            mav.addObject("ShowMsg", updMsg);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return mav;
    }
    
    
}
