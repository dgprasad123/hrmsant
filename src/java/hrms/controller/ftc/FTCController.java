/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.ftc;

import hrms.dao.ftc.FTCDAO;
import hrms.dao.login.LoginDAOImpl;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.ProcessStatusDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.MaxNotificationIdDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.FTC.sFTCBean;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author DurgaPrasad
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class FTCController {
    
    @Autowired
    public LoginDAOImpl loginDAO;

    @Autowired
    public FTCDAO FTCDAO;

    @Autowired
    public DepartmentDAO departmentDao;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public NotificationDAO NotificationDAO;

    @Autowired
    public MaxNotificationIdDAO maxnotiidDao;

    @Autowired
    ProcessStatusDAO processStatusDAO;
    
    @RequestMapping(value = "newFTCEntry")
    public ModelAndView newFTCEntry(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("sFTCBean") sFTCBean lBean) {

        ModelAndView mav = null;
        try {
            //List depuList = deputationDAO.getDeputationList(lub.getEmpId());
            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            mav = new ModelAndView("/FTC/AddsEmpFTC", "sFTCBean", lBean);

            if (lBean.getRadpostingauthtype() == null || lBean.getRadpostingauthtype().equals("")) {
                lBean.setRadpostingauthtype("GOO");
            }

            List deptlist = departmentDao.getDepartmentList();
            List fieldofflist = offDAO.getFieldOffList(lub.getOffcode());
            List otherOrgfflist = offDAO.getOtherOrganisationList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("currentYear", currentYear);
            mav.addObject("otherOrgfflist", otherOrgfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editFTCEntry")
    public ModelAndView editFTCEntry(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("sFTCBean") sFTCBean lBean, @RequestParam Map<String, String> requestParams) {

        ModelAndView mav = null;
        try {
            String empId = lub.getEmpId();
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            sFTCBean slBean = FTCDAO.getSFTCDetail(empId, notificationId);

            if (slBean.getRadpostingauthtype() == null || slBean.getRadpostingauthtype().equals("")) {
                slBean.setRadpostingauthtype("GOO");
            }

            Calendar cal = Calendar.getInstance();
            int currentYear = cal.get(Calendar.YEAR);
            mav = new ModelAndView("/FTC/AddsEmpFTC", "sFTCBean", slBean);

            List deptlist = departmentDao.getDepartmentList();
            List fieldofflist = offDAO.getFieldOffList(lub.getOffcode());
            List otherOrgfflist = offDAO.getOtherOrganisationList();

            mav.addObject("deptlist", deptlist);
            mav.addObject("fieldofflist", fieldofflist);
            mav.addObject("currentYear", currentYear);

            mav.addObject("otherOrgfflist", otherOrgfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savesFTCEntry", params = "action=Back to FTC List")
    public ModelAndView BackToGDeputationListPage(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("sFTCBean") sFTCBean lBean) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("redirect:/FTCEntryList.htm", "sFTCBean", lBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savesFTCEntry", params = "action=Save FTC")
    public ModelAndView savesFTCEntry(@ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("sFTCBean") sFTCBean lBean) {

        ModelAndView mav = null;

        try {
            //deputationForm.setEmpid(lub.getEmpId());

            // System.out.println("Else");
            lBean.setEmpid(selectedEmpObj.getEmpId());
            if (lBean.getLtcId() != null && !lBean.getLtcId().equals("")) {
                FTCDAO.updateFTCEntry(lBean);
            } else {
                FTCDAO.saveFTCEntry((lBean.getHidNotId() + ""), lBean, loginbean.getLogindeptcode(), loginbean.getLoginoffcode(), loginbean.getLogingpc());
            }

                // transferForm.setSltPostedFieldOff(deputationForm.getSltFieldOffice());
            //  transferDao.saveTransfer(transferForm, notid,loginbean.getLoginempid());
            mav = new ModelAndView("redirect:/FTCEntryList.htm", "sFTCBean", lBean);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "UpdateFTCEntry", params = "action=Back to FTC List")
    public ModelAndView UpdateFTCEntry(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("sFTCBean") sFTCBean lBean) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("redirect:/FTCEntryList.htm", "sFTCBean", lBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "UpdateFTCEntry", params = "action=Update FTC")
    public ModelAndView updateFTCEntry(@ModelAttribute("LoginUserBean") LoginUserBean loginbean, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("sFTCBean") sFTCBean lBean) {

        ModelAndView mav = null;

        try {
            lBean.setEmpid(selectedEmpObj.getEmpId());
            FTCDAO.updateFTCEntry(lBean);

            mav = new ModelAndView("redirect:/FTCEntryList.htm", "sFTCBean", lBean);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "FTCEntryList")
    public ModelAndView FTCEntryList(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("slBean") sFTCBean lBean) {
        ModelAndView mav = null;
        try {
            List ftclist = FTCDAO.getFTCEntryList(lub.getEmpId());

            mav = new ModelAndView("/FTC/FTCEntryList", "sFTCBean", lBean);
            mav.addObject("ftclist", ftclist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}
