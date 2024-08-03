/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.suspension;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.suspension.SuspensionDAO;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.recruitment.RecruitmentModel;
import hrms.model.suspension.Suspension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Surendra
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class EmployeeSuspensionController {

    @Autowired
    public SuspensionDAO suspensionDAO;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @RequestMapping(value = "suspensionList")
    public ModelAndView SuspensionList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") Suspension sus) {

        List suslist = null;

        ModelAndView mav = null;
        try {

            sus.setEmpid(selectedEmpObj.getEmpId());
            suslist = suspensionDAO.findAllSuspension(sus.getEmpid());

            mav = new ModelAndView("/suspension/EmployeeSuspensionList", "command", sus);
            mav.addObject("suspensionList", suslist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addSuspensionController", params = "action=Create Suspension")
    public ModelAndView createSuspension(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") Suspension sus) {

        List suslist = null;

        ModelAndView mav = null;
        try {

            List deptlist = deptDAO.getDepartmentList();

            mav = new ModelAndView("/suspension/EmployeeSuspension", "command", sus);

            mav.addObject("deptlist", deptlist);
            mav.addObject("suspensionList", suslist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addSuspensionController", params = "action=Save")
    public ModelAndView saveSuspension(@ModelAttribute("loginForm") Users loginForm, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") Suspension sus) {

        List suslist = null;

        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        ModelAndView mav = null;
        try {
            nb.setNottype("SUSPENSION");
            nb.setEmpId(sus.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(sus.getOrdno());
            nb.setOrdDate(sdf.parse(sus.getOrdDate()));
            nb.setSancDeptCode(sus.getEntDept());
            nb.setSancOffCode(sus.getEntOffice());
            nb.setSancAuthCode(sus.getEntAuth());
            nb.setNote(sus.getNote());
            nb.setIfVisible(sus.getChkNotSBPrint());

            if (sus.getSuspensionId() != null && !sus.getSuspensionId().equals("")) {
                nb.setNotid(sus.getNotId());
                notificationDao.modifyNotificationData(nb);
                suspensionDAO.updateSuspensionData(sus);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                sus.setNotId(notid);
                sus.setEntAuth(loginForm.getDeptcode());
                sus.setEntOffice(loginForm.getOffcode());
                sus.setEntAuth(loginForm.getCurspc());
                suspensionDAO.insertSuspensionData(sus);
            }

            mav = new ModelAndView("redirect:/suspensionList.htm", "command", sus);

            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);

            mav.addObject("suspensionList", suslist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addSuspensionController", params = "action=Back")
    public ModelAndView return2List(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") Suspension sus) {

        ModelAndView mav = null;
        try {
            mav = new ModelAndView("redirect:/suspensionList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "addSuspensionController", params = "action=Delete")
    public ModelAndView deleteSuspensionData(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") Suspension sus) {

        ModelAndView mav = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Calendar cal = Calendar.getInstance();
            String sysdate = dateFormat.format(cal.getTime());
            Date curDate = new Date(sysdate);

            Date outputDate = dateFormat.parse(sus.getOrdDate());

            if (outputDate.compareTo(curDate) >= 0) {
                suspensionDAO.deleteSuspension(sus.getSuspensionId());
            }
            mav = new ModelAndView("redirect:/suspensionList.htm", "command", sus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editSuspension")
    public ModelAndView editSuspension(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("command") Suspension sus, @RequestParam("susId") String susId) {

        ModelAndView mav = null;
        try {
            sus = suspensionDAO.editSuspension(susId);
            sus.setSuspensionId(susId);
            mav = new ModelAndView("/suspension/EmployeeSuspension", "command", sus);

            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);

            List sancOfflist = offDAO.getTotalOfficeList(sus.getSltDept());
            mav.addObject("offList", sancOfflist);

            List hqOfflist = offDAO.getTotalOfficeList(sus.getSlthqDeaprtment());
            mav.addObject("hqOfflist", hqOfflist);

            List sancPostlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(sus.getSltOffice());
            mav.addObject("postlist", sancPostlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}
