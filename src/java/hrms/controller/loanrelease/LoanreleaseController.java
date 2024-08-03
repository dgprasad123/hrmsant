/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.loanrelease;

import hrms.dao.loanrelease.LoanreleaseDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.LoanTypeDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.loanrelease.LoanRelease;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class LoanreleaseController {

    @Autowired
    public LoanreleaseDAO loanreleaseDAO;
    @Autowired
    public DepartmentDAO deptDAO;
    @Autowired
    public OfficeDAO offDAO;
    @Autowired
    LoanTypeDAO loanTypeDao;
    @Autowired
    public NotificationDAO notificationDao;
    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @RequestMapping(value = "loanrelease.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loanRelease(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("loanrelease") LoanRelease loanrelease, Map<String, Object> model) {
        ModelAndView mv = null;
        List loanReleaseList = loanreleaseDAO.getLoanreleaseList(user.getEmpId());
        mv = new ModelAndView("/loanrelease/LoanreleaseList", "loanrelease", loanrelease);
        mv.addObject("loanreleaseList", loanReleaseList);
        return mv;
    }

    @RequestMapping(value = "loanrelease.htm", params = "new", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView newLoanRelease(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("loanrelease") LoanRelease loanrelease, Map<String, Object> model) {
        ModelAndView mv = null;
        //List loanReleaseList = loanreleaseDAO.getLoanreleaseList(loanrelease.getTxtEid());
        mv = new ModelAndView("/loanrelease/LoanreleaseEdit", "loanrelease", loanrelease);
        List deptlist = deptDAO.getDepartmentList();
        mv.addObject("deptArray", deptlist);
        List loantypelist = loanTypeDao.getLoanTypeList();
        mv.addObject("loanTypeArray", loantypelist);
       // mv.addObject("loanreleaseList", loanReleaseList);
        return mv;
    }

    @RequestMapping(value = "loanrelease.htm", params = "save", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView saveLoanRelease(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("loanrelease") LoanRelease loanrelease, Map<String, Object> model) {
        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            nb.setNottype("LOAN_TRAN");
            nb.setEmpId(user.getEmpId());
            nb.setDateofEntry(new Date());
            nb.setOrdno(loanrelease.getTxtreorderno());
            nb.setOrdDate(sdf.parse(loanrelease.getTxtreorderdt()));
            nb.setSancDeptCode(loanrelease.getSltdept());
            nb.setSancOffCode(loanrelease.getSltoffice());
            nb.setSancAuthCode(loanrelease.getSltauth());

            nb.setEntryDeptCode(lub.getLogindeptcode());
            nb.setEntryOffCode(lub.getLoginoffcode());
            nb.setEntryAuthCode(lub.getLoginspc());
            nb.setNote(loanrelease.getTxtnote());
            loanrelease.setTxtEid(user.getEmpId());
            if(loanrelease.getChkNotSBPrint()!=null && loanrelease.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
            if (loanrelease.getHrepid() != null && !loanrelease.getHrepid().equals("")) {
                nb.setNotid(loanrelease.getHnid());
                notificationDao.modifyNotificationData(nb);
                loanreleaseDAO.updateLoanrelease(loanrelease);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                loanreleaseDAO.saveLoanrelease(loanrelease, notid);
            }
            mav = new ModelAndView("/loanrelease/LoanreleaseList", "loanrelease", loanrelease);
            List loanReleaseList = loanreleaseDAO.getLoanreleaseList(loanrelease.getTxtEid());
            mav.addObject("loanreleaseList", loanReleaseList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getloanreleasedata.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getLoanReleaseData(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("loanrelease") LoanRelease loanrelease, @RequestParam("repid") String repid, @RequestParam("notid") int notid, Map<String, Object> model) {
        ModelAndView mv = null;
        loanrelease = loanreleaseDAO.getLoanReleaseData(repid, notid);
        loanrelease.setTxtEid(user.getEmpId());
        loanrelease.setHrepid(repid);
        loanrelease.setHnid(notid);
        List deptlist = deptDAO.getDepartmentList();
        List offList = offDAO.getTotalOfficeList(loanrelease.getSltdept());
        List authList = substantivePostDAO.getSanctioningSPCOfficeWiseList(loanrelease.getSltoffice());
        mv = new ModelAndView("/loanrelease/LoanreleaseEdit", "loanrelease", loanrelease);
        mv.addObject("deptArray", deptlist);
        mv.addObject("offArray", offList);
        mv.addObject("authArray", authList);
        List loantypelist = loanTypeDao.getLoanTypeList();
        mv.addObject("loanTypeArray", loantypelist);
        return mv;
    }

    @RequestMapping(value = "deleteloanreleasedata.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteLoanRelease(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("loanrelease") LoanRelease loanrelease, @RequestParam("repid") String repid, @RequestParam("notid") int notid, Map<String, Object> model) {
        ModelAndView mv = null;
        loanreleaseDAO.deleteLoanRelease(repid, notid);
        mv = new ModelAndView("/loanrelease/LoanreleaseList", "loanrelease", loanrelease);
        List loanReleaseList = loanreleaseDAO.getLoanreleaseList(user.getEmpId());
        mv.addObject("loanreleaseList", loanReleaseList);
        return mv;
    }
    public ModelAndView cancelLoanRelease(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("loanrelease") LoanRelease loanrelease, Map<String, Object> model) {
        ModelAndView mv = null;
        mv = new ModelAndView("/loanrelease/LoanreleaseList", "loanrelease", loanrelease);
        List loanReleaseList = loanreleaseDAO.getLoanreleaseList(user.getEmpId());
        mv.addObject("loanreleaseList", loanReleaseList);
        return mv;
    }
}
