/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.repayment;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.LoanTypeDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.repayment.RepaymentDAO;
import hrms.model.loanrelease.LoanRelease;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.repayment.LoanRepayment;
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
public class RepaymentController {
    @Autowired
    public RepaymentDAO repaymentDAO;
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

    @RequestMapping(value = "loanrepayment.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView loanRepayment(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users user,@ModelAttribute("loanrepayment") LoanRepayment loanrepayment, Map<String, Object> model) {
        ModelAndView mv = null;
        List loanReleaseList = repaymentDAO.getLoanRepaymentList(user.getEmpId());
        mv = new ModelAndView("/repayment/RepaymentList", "loanrepayment", loanrepayment);
        mv.addObject("loanRepaymentList", loanReleaseList);
        return mv;
    }

    @RequestMapping(value = "loanrepayment.htm", params = "new", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView newLoanRepayment(@ModelAttribute("SelectedEmpObj") Users user,@ModelAttribute("loanrepayment") LoanRepayment loanrepayment, Map<String, Object> model) {
        ModelAndView mv = null;
        //List loanReleaseList = loanreleaseDAO.getLoanreleaseList(loanrelease.getTxtEid());
        mv = new ModelAndView("/repayment/RepaymentEdit", "loanrepayment", loanrepayment);
        List deptlist = deptDAO.getDepartmentList();
        mv.addObject("deptArray", deptlist);
        List loantypelist = loanTypeDao.getLoanTypeList();
        mv.addObject("loanTypeArray", loantypelist);
       // mv.addObject("loanreleaseList", loanReleaseList);
        return mv;
    }

    @RequestMapping(value = "loanrepayment.htm", params = "save", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView saveLoanRepayment(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("loanrepayment") LoanRepayment loanrepayment, Map<String, Object> model) {
        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            nb.setNottype("LOAN_TRAN");
            nb.setEmpId(user.getEmpId());
            nb.setDateofEntry(new Date());
            nb.setOrdno(loanrepayment.getTxtreorderno());
            nb.setOrdDate(sdf.parse(loanrepayment.getTxtreorderdt()));
            nb.setSancDeptCode(loanrepayment.getSltdept());
            nb.setSancOffCode(loanrepayment.getSltoffice());
            nb.setSancAuthCode(loanrepayment.getSltauth());

            nb.setEntryDeptCode(lub.getLogindeptcode());
            nb.setEntryOffCode(lub.getLoginoffcode());
            nb.setEntryAuthCode(lub.getLoginspc());
            nb.setNote(loanrepayment.getTxtnote());
            if(loanrepayment.getChkNotSBPrint()!=null && loanrepayment.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
                
            loanrepayment.setTxtEid(user.getEmpId());
            if (loanrepayment.getHrepid() != null && !loanrepayment.getHrepid().equals("")) {
                nb.setNotid(loanrepayment.getHnid());
                notificationDao.modifyNotificationData(nb);
                repaymentDAO.updateLoanRepayment(loanrepayment);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                repaymentDAO.saveLoanRepayment(loanrepayment, notid);
            }
            mav = new ModelAndView("/repayment/RepaymentList", "loanrepayment", loanrepayment);
            List loanReleaseList = repaymentDAO.getLoanRepaymentList(loanrepayment.getTxtEid());
            mav.addObject("loanRepaymentList", loanReleaseList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "getloanrepaymentdata.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView getLoanRepaymentData(@ModelAttribute("SelectedEmpObj") Users user,@ModelAttribute("loanrepayment") LoanRepayment loanrepayment, @RequestParam("repid") String repid, @RequestParam("notid") int notid, Map<String, Object> model) {
        ModelAndView mv = null;
        loanrepayment = repaymentDAO.getLoanRepaymentData(repid, notid);
        loanrepayment.setHrepid(repid);
        loanrepayment.setHnid(notid);
        List deptlist = deptDAO.getDepartmentList();
        List offList = offDAO.getTotalOfficeList(loanrepayment.getSltdept());
        List authList = substantivePostDAO.getSanctioningSPCOfficeWiseList(loanrepayment.getSltoffice());
        mv = new ModelAndView("/repayment/RepaymentEdit", "loanrepayment", loanrepayment);
        mv.addObject("deptArray", deptlist);
        mv.addObject("offArray", offList);
        mv.addObject("authArray", authList);
        List loantypelist = loanTypeDao.getLoanTypeList();
        mv.addObject("loanTypeArray", loantypelist);
        return mv;
    }

    @RequestMapping(value = "deleteloanrepaymentdata.htm", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView deleteLoanRepayment(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("SelectedEmpObj") Users user,@ModelAttribute("loanrepayment") LoanRepayment loanrepayment, @RequestParam("repid") String repid, @RequestParam("notid") int notid, Map<String, Object> model) {
        ModelAndView mv = null;
        repaymentDAO.deleteLoanRepayment(repid, notid);
        mv = new ModelAndView("/repayment/RepaymentList", "loanrepayment", loanrepayment);
        List loanRepaymentList = repaymentDAO.getLoanRepaymentList(user.getEmpId());
        mv.addObject("loanRepaymentList", loanRepaymentList);
        return mv;
    }

    @RequestMapping(value = "loanrepayment.htm", params = "cancel", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView cancelLoanRepayment(@ModelAttribute("SelectedEmpObj") Users user, @ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("loanrepayment") LoanRepayment loanrepayment, Map<String, Object> model) {
        ModelAndView mv = null;
        mv = new ModelAndView("/repayment/RepaymentList", "loanrepayment", loanrepayment);
        List loanRepaymentList = repaymentDAO.getLoanRepaymentList(user.getEmpId());
        mv.addObject("loanRepaymentList", loanRepaymentList);
        return mv;

    }
}
