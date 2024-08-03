package hrms.controller.report.groupwisesanctionedstrength;

import hrms.dao.payroll.billbrowser.BillGroupDAO;
import hrms.dao.report.annualestablishment.AnnuaiEstablishmentReportDAO;
import hrms.dao.report.officewisesanctionedstrength.OfficeWiseSanctionedStrengthDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.report.officewisesanctionedstrength.OfficeWiseSanctionedStrengthBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("LoginUserBean")
public class OfficeWiseSanctionedPostController {

    @Autowired
    OfficeWiseSanctionedStrengthDAO officeWiseSanctionedStrengthDAO;

    @Autowired
    AnnuaiEstablishmentReportDAO annuaiEstablishmentDao;

    @Autowired
    BillGroupDAO billGroupDAO;

    @RequestMapping(value = "getOfficeWiseSanctionedStrength")
    public String GetOfficeWiseSanctionedStrength(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub) {
        //ModelAndView mav = new ModelAndView();
        List data = officeWiseSanctionedStrengthDAO.getSanctionedStrengthList(lub.getLoginoffcode());
        model.addAttribute("data", data);
        model.addAttribute("offName", lub.getLoginoffname());

        return "report/OfficeWiseSanctionedStrength";
    }

    @RequestMapping(value = "addOfficeWiseSanctionedStrength")
    public ModelAndView AddOfficeWiseSanctionedStrength(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("officebean") OfficeWiseSanctionedStrengthBean officebean) {

        ModelAndView mav = null;
        mav = new ModelAndView("report/AddOfficeWiseSanctionedStrength", "officebean", officebean);

        List billGrpList = new ArrayList();

        if (officebean.getAerId() != null && !officebean.getAerId().equals("")) {
            officeWiseSanctionedStrengthDAO.getSanctionedStrengthData(officebean);
            billGrpList = officeWiseSanctionedStrengthDAO.getBillgroupListNotInEditNewCase(lub.getLoginoffcode(), officebean.getFinancialYear(), officebean.getAerId());
            List<BigDecimal> li = officeWiseSanctionedStrengthDAO.getSelectedBillGroup(officebean.getAerId());

            BigDecimal bg[] = new BigDecimal[li.size()];

            for (int i = 0; i < li.size(); i++) {
                bg[i] = li.get(i);
            }

            officebean.setBillGrpId(bg);
        } else {
            billGrpList = officeWiseSanctionedStrengthDAO.getBillgroupListNotInAddNewCase(lub.getLoginoffcode(), officebean.getFinancialYear());
            
        }
        
        Map<String, String> fylist = officeWiseSanctionedStrengthDAO.getFinancialYearList();
        mav.addObject("fylist", fylist);
        mav.addObject("offName", lub.getLoginoffname());
        mav.addObject("BillGroupList", billGrpList);
        return mav;
    }

    /*@RequestMapping(value = "saveOfficeWiseSanctionedStrength")
     public String SaveOfficeWiseSanctionedStrength(Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("officebean") OfficeWiseSanctionedStrengthBean officebean, @RequestParam("btnAdd") String btnAdd) {
        
     if (btnAdd.equals("Save")) {
     officeWiseSanctionedStrengthDAO.saveSanctionedPostData(lub.getLoginoffcode(), officebean);
     List data = officeWiseSanctionedStrengthDAO.getSanctionedStrengthList(lub.getLoginoffcode());
     model.addAttribute("data", data);
     model.addAttribute("offName", lub.getOffname());
     } else if (btnAdd.equals("Back")) {

     }
     return "report/OfficeWiseSanctionedStrength"; 
     }*/
    @RequestMapping(value = "saveOfficeWiseSanctionedStrength", params = {"action=Save"})
    public ModelAndView Save(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("officebean") OfficeWiseSanctionedStrengthBean officebean) {

        officeWiseSanctionedStrengthDAO.saveSanctionedPostData(lub.getLoginoffcode(), officebean);
        return new ModelAndView("redirect:getOfficeWiseSanctionedStrength.htm");
    }

    @RequestMapping(value = "saveOfficeWiseSanctionedStrength", params = {"action=Back"})
    public ModelAndView Back(@ModelAttribute("officebean") OfficeWiseSanctionedStrengthBean officebean) {

        return new ModelAndView("redirect:getOfficeWiseSanctionedStrength.htm");
    }

    @RequestMapping(value = "saveOfficeWiseSanctionedStrength", params = {"action=Ok"})
    public ModelAndView Ok(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("officebean") OfficeWiseSanctionedStrengthBean officebean) {

        ModelAndView mav = null;
        List billGrpList = new ArrayList();
        mav = new ModelAndView("report/AddOfficeWiseSanctionedStrength", "officebean", officebean);
        
        billGrpList = officeWiseSanctionedStrengthDAO.getBillgroupListNotInAddNewCase(lub.getLoginoffcode(), officebean.getFinancialYear());
        
        Map<String, String> fylist = officeWiseSanctionedStrengthDAO.getFinancialYearList();
        mav.addObject("fylist", fylist);
        mav.addObject("offName", lub.getLoginoffname());
        mav.addObject("BillGroupList", billGrpList);
        return mav;
    }

    /*
     @RequestMapping(value = "duplicateAERFinancialYear")
     public void duplicateAERFinancialYear(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @RequestParam("fy") String fy) {
     //response.setContentType("application/json");
     PrintWriter out = null;
     //JSONArray json = null;
     try {
     String isDuplicate = officeWiseSanctionedStrengthDAO.verifyDuplicate(lub.getLoginoffcode(), fy);
     out = response.getWriter();
     out.write(isDuplicate.toString());
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     out.flush();
     out.close();
     }
     }
    
     */
}
