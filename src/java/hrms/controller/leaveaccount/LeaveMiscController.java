/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.leaveaccount;

import hrms.dao.leave.LeaveDAO;
import hrms.dao.leave.MiscLeaveDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.LeaveTypeDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.leave.LeaveOpeningBalanceBean;
import hrms.model.leave.MiscLeaveBean;
import hrms.model.leave.MiscLeaveForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.master.Office;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj", "SelectedEmpOffice"})
public class LeaveMiscController {

    @Autowired
    public DepartmentDAO deptDAO;
    @Autowired
    LeaveTypeDAO leaveTypeDAO;
    @Autowired
    MiscLeaveDAO MiscLeaveDAO;
    @Autowired
    public OfficeDAO offDAO;
    @Autowired
    SubStantivePostDAO subStantivePostDao;
    @Autowired
    public NotificationDAO notificationDao;

    @RequestMapping(value = "UnavailedLeaveList")
    public ModelAndView UnavailedLeaveList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm miscLeaveForm) {
        miscLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        ModelAndView mv = new ModelAndView("/leaveAccount/UnavailedLeaveList", "MiscLeaveForm", miscLeaveForm);
        mv.addObject("leaveList", MiscLeaveDAO.getLeaveList(selectedEmpObj.getEmpId(), "06"));
        return mv;
    }

    @RequestMapping(value = "AddUnavailedLeave")
    public ModelAndView addUnavailedLeave(HttpServletResponse response, Model model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm miscLeaveForm) {
        List deptlist = deptDAO.getDepartmentList();
        miscLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        ModelAndView mv = new ModelAndView("/leaveAccount/AddUnavailedLeave", "MiscLeaveForm", miscLeaveForm);
        mv.addObject("deptlist", deptlist);
        return mv;
    }

    @RequestMapping(value = "saveUnavailedLeave")
    public ModelAndView saveUnavailedLeave(HttpServletResponse response, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm mLeaveForm, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List leaveList = null;
        mLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("LEAVE");
                nb.setEmpId(mLeaveForm.getEmpId());
                nb.setDateofEntry(new Date());
                nb.setOrdno(mLeaveForm.getOrderNumber());
                nb.setOrdDate(sdf.parse(mLeaveForm.getOrderDate()));
                nb.setSancDeptCode(mLeaveForm.getHidTempDeptCode());
                nb.setSancOffCode(mLeaveForm.getHidTempAuthOffCode());
                nb.setSancAuthCode(mLeaveForm.getHidTempAuthPost());
                nb.setEntryDeptCode(lub.getLogindeptcode());
                nb.setEntryOffCode(lub.getLoginoffcode());
                nb.setEntryAuthCode(lub.getLoginspc());
                nb.setNote(mLeaveForm.getNote());
                if(mLeaveForm.getChkNotSBPrint()!=null && mLeaveForm.getChkNotSBPrint().equals("Y")){
                    nb.setIfVisible("N");
                }else{
                    nb.setIfVisible("Y");
                }

                if (mLeaveForm.getLeaveId() != null && !mLeaveForm.getLeaveId().trim().equals("")) {
                    nb.setNotid(mLeaveForm.getNotificationId());
                    notificationDao.modifyNotificationData(nb);
                    MiscLeaveDAO.updateMiscLeave(mLeaveForm, mLeaveForm.getNotificationId(), mLeaveForm.getLeaveId());
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    MiscLeaveDAO.saveMiscLeave(mLeaveForm, notid, "06");
                }
            } else if (submit != null && submit.equals("Delete")) {

                int retVal = notificationDao.deleteNotificationData(mLeaveForm.getNotificationId(), "LEAVE");

                if (retVal > 0) {
                    //punishmentDAO.deletePunishment(punishmentBean);
                }
            }

            //punishmentList = punishmentDAO.getPunishmentList(punishmentBean.getEmpId());
            mav = new ModelAndView("/leaveAccount/UnavailedLeaveList", "MiscLeaveForm", mLeaveForm);
            mav.addObject("leaveList", MiscLeaveDAO.getLeaveList(selectedEmpObj.getEmpId(), "06"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editUnavailedLeave")
    public ModelAndView editUnavailedLeave(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        ModelAndView mav = null;

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            mlForm = MiscLeaveDAO.getMiscLeaveData(leaveId);

            mlForm.setEmpId(u.getEmpId());
            mlForm.setNotificationId(notificationId);
            mlForm.setLeaveId(leaveId);

            mav = new ModelAndView("/leaveAccount/AddUnavailedLeave", "MiscLeaveForm", mlForm);
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List offlist = offDAO.getTotalOfficeList(mlForm.getHidAuthDeptCode());
            mav.addObject("offlist", offlist);
            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(mlForm.getHidAuthOffCode());
            mav.addObject("postlist", postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewUnavailedLeave")
    public ModelAndView viewUnavailedLeave(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        ModelAndView mav = null;

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            mlForm = MiscLeaveDAO.getMiscLeaveData(leaveId);
            mlForm.setDepartmentName(subStantivePostDao.getDeptName(mlForm.getHidTempDeptCode()));
            Office office = offDAO.getOfficeDetails(mlForm.getHidTempAuthOffCode());
            mlForm.setOfficeName(office.getOffEn());
            mlForm.setEmpId(u.getEmpId());
            mlForm.setNotificationId(notificationId);
            mlForm.setLeaveId(leaveId);

            mav = new ModelAndView("/leaveAccount/ViewUnavailedLeave");
            mav.addObject("leaveDetail", mlForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "DeleteUnavailedLeave")
    public void deleteUnavailedLeave(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            notificationDao.deleteNotificationData(notificationId, "LEAVE");
            MiscLeaveDAO.deleteMiscLeave(leaveId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "AvailedLeaveList")
    public ModelAndView availedLeaveList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm miscLeaveForm) {
        miscLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        ModelAndView mv = new ModelAndView("/leaveAccount/AvailedLeaveList", "MiscLeaveForm", miscLeaveForm);
        mv.addObject("leaveList", MiscLeaveDAO.getLeaveList(selectedEmpObj.getEmpId(), "05"));
        return mv;
    }

    @RequestMapping(value = "AddAvailedLeave")
    public ModelAndView addAvailedLeave(HttpServletResponse response, Model model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm miscLeaveForm) {
        List deptlist = deptDAO.getDepartmentList();
        miscLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        miscLeaveForm.setRadauthtype("GOO");
        ModelAndView mv = new ModelAndView("/leaveAccount/AddAvailedLeave", "MiscLeaveForm", miscLeaveForm);
        mv.addObject("deptlist", deptlist);

        List otherOrgOfflist = offDAO.getOtherOrganisationList();
        mv.addObject("otherOrgOfflist", otherOrgOfflist);

        return mv;
    }

    @RequestMapping(value = "saveAvailedLeave")
    public ModelAndView saveAvailedLeave(HttpServletResponse response, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm mLeaveForm, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        mLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("LEAVE");
                nb.setEmpId(mLeaveForm.getEmpId());
                nb.setDateofEntry(new Date());
                nb.setOrdno(mLeaveForm.getOrderNumber());
                nb.setOrdDate(sdf.parse(mLeaveForm.getOrderDate()));
                nb.setRadpostingauthtype(mLeaveForm.getRadauthtype());
                nb.setSancDeptCode(mLeaveForm.getHidTempDeptCode());
                nb.setSancOffCode(mLeaveForm.getHidTempAuthOffCode());
                if (nb.getRadpostingauthtype() != null && nb.getRadpostingauthtype().equals("GOI")) {
                    nb.setSancAuthCode(mLeaveForm.getHidAuthorityOthSpc());
                } else {
                    nb.setSancAuthCode(mLeaveForm.getHidTempAuthPost());
                }
                nb.setEntryDeptCode(lub.getLogindeptcode());
                nb.setEntryOffCode(lub.getLoginoffcode());
                nb.setEntryAuthCode(lub.getLoginspc());
                nb.setNote(mLeaveForm.getNote());
                if(mLeaveForm.getChkNotSBPrint()!=null && mLeaveForm.getChkNotSBPrint().equals("Y")){
                    nb.setIfVisible("N");
                }else{
                    nb.setIfVisible("Y");
                }

                if (mLeaveForm.getLeaveId() != null && !mLeaveForm.getLeaveId().trim().equals("")) {
                    nb.setNotid(mLeaveForm.getNotificationId());
                    notificationDao.modifyNotificationData(nb);
                    MiscLeaveDAO.updateMiscLeave(mLeaveForm, mLeaveForm.getNotificationId(), mLeaveForm.getLeaveId());
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    MiscLeaveDAO.saveMiscLeave(mLeaveForm, notid, "05");
                }
            } else if (submit != null && submit.equals("Delete")) {

                int retVal = notificationDao.deleteNotificationData(mLeaveForm.getNotificationId(), "LEAVE");

                if (retVal > 0) {
                    //punishmentDAO.deletePunishment(punishmentBean);
                }
            }

            //punishmentList = punishmentDAO.getPunishmentList(punishmentBean.getEmpId());
            mav = new ModelAndView("/leaveAccount/AvailedLeaveList", "MiscLeaveForm", mLeaveForm);
            mav.addObject("leaveList", MiscLeaveDAO.getLeaveList(selectedEmpObj.getEmpId(), "05"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editAvailedLeave")
    public ModelAndView editAvailedLeave(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        ModelAndView mav = null;

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            mlForm = MiscLeaveDAO.getMiscLeaveData(leaveId);

            mlForm.setEmpId(u.getEmpId());
            mlForm.setNotificationId(notificationId);
            mlForm.setLeaveId(leaveId);

            mav = new ModelAndView("/leaveAccount/AddAvailedLeave", "MiscLeaveForm", mlForm);
            
            if (mlForm.getRadauthtype()== null || mlForm.getRadauthtype().equals("")) {
                mlForm.setRadauthtype("GOO");
            }
            
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List offlist = offDAO.getTotalOfficeList(mlForm.getHidAuthDeptCode());
            mav.addObject("offlist", offlist);
            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(mlForm.getHidAuthOffCode());
            mav.addObject("postlist", postlist);

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewAvailedLeave")
    public ModelAndView viewAvailedLeave(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        ModelAndView mav = null;

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            mlForm = MiscLeaveDAO.getMiscLeaveData(leaveId);
            mlForm.setDepartmentName(subStantivePostDao.getDeptName(mlForm.getHidTempDeptCode()));
            Office office = offDAO.getOfficeDetails(mlForm.getHidTempAuthOffCode());
            mlForm.setOfficeName(office.getOffEn());
            mlForm.setEmpId(u.getEmpId());
            mlForm.setNotificationId(notificationId);
            mlForm.setLeaveId(leaveId);

            mav = new ModelAndView("/leaveAccount/ViewAvailedLeave");
            mav.addObject("leaveDetail", mlForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "DeleteAvailedLeave")
    public void deleteAvailedLeave(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            notificationDao.deleteNotificationData(notificationId, "LEAVE");
            MiscLeaveDAO.deleteMiscLeave(leaveId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "ExtensionList")
    public ModelAndView extensionList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm miscLeaveForm) {
        miscLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        ModelAndView mv = new ModelAndView("/leaveAccount/ExtensionList", "MiscLeaveForm", miscLeaveForm);
        mv.addObject("leaveList", MiscLeaveDAO.getLeaveList(selectedEmpObj.getEmpId(), "07"));
        return mv;
    }

    @RequestMapping(value = "AddExtension")
    public ModelAndView addExtension(HttpServletResponse response, Model model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm miscLeaveForm) {
        List deptlist = deptDAO.getDepartmentList();
        miscLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        ModelAndView mv = new ModelAndView("/leaveAccount/AddExtension", "MiscLeaveForm", miscLeaveForm);
        mv.addObject("deptlist", deptlist);
        return mv;
    }

    @RequestMapping(value = "saveExtension")
    public ModelAndView saveExtension(HttpServletResponse response, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm mLeaveForm, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        mLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("LEAVE");
                nb.setEmpId(mLeaveForm.getEmpId());
                nb.setDateofEntry(new Date());
                nb.setOrdno(mLeaveForm.getOrderNumber());
                nb.setOrdDate(sdf.parse(mLeaveForm.getOrderDate()));
                nb.setSancDeptCode(mLeaveForm.getHidTempDeptCode());
                nb.setSancOffCode(mLeaveForm.getHidTempAuthOffCode());
                nb.setSancAuthCode(mLeaveForm.getHidTempAuthPost());
                nb.setEntryDeptCode(lub.getLogindeptcode());
                nb.setEntryOffCode(lub.getLoginoffcode());
                nb.setEntryAuthCode(lub.getLoginspc());
                nb.setNote(mLeaveForm.getNote());
                if(mLeaveForm.getChkNotSBPrint()!=null && mLeaveForm.getChkNotSBPrint().equals("Y")){
                    nb.setIfVisible("N");
                }else{
                    nb.setIfVisible("Y");
                }

                if (mLeaveForm.getLeaveId() != null && !mLeaveForm.getLeaveId().trim().equals("")) {
                    nb.setNotid(mLeaveForm.getNotificationId());
                    notificationDao.modifyNotificationData(nb);
                    MiscLeaveDAO.updateMiscLeave(mLeaveForm, mLeaveForm.getNotificationId(), mLeaveForm.getLeaveId());
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    MiscLeaveDAO.saveMiscLeave(mLeaveForm, notid, "07");
                }
            } else if (submit != null && submit.equals("Delete")) {

                int retVal = notificationDao.deleteNotificationData(mLeaveForm.getNotificationId(), "LEAVE");

                if (retVal > 0) {
                    //punishmentDAO.deletePunishment(punishmentBean);
                }
            }

            //punishmentList = punishmentDAO.getPunishmentList(punishmentBean.getEmpId());
            mav = new ModelAndView("/leaveAccount/ExtensionList", "MiscLeaveForm", mLeaveForm);
            mav.addObject("leaveList", MiscLeaveDAO.getLeaveList(selectedEmpObj.getEmpId(), "07"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editExtension")
    public ModelAndView editExtension(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        ModelAndView mav = null;

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            mlForm = MiscLeaveDAO.getMiscLeaveData(leaveId);

            mlForm.setEmpId(u.getEmpId());
            mlForm.setNotificationId(notificationId);
            mlForm.setLeaveId(leaveId);

            mav = new ModelAndView("/leaveAccount/AddExtension", "MiscLeaveForm", mlForm);
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List offlist = offDAO.getTotalOfficeList(mlForm.getHidAuthDeptCode());
            mav.addObject("offlist", offlist);
            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(mlForm.getHidAuthOffCode());
            mav.addObject("postlist", postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewExtension")
    public ModelAndView viewExtension(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        ModelAndView mav = null;

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            mlForm = MiscLeaveDAO.getMiscLeaveData(leaveId);
            mlForm.setDepartmentName(subStantivePostDao.getDeptName(mlForm.getHidTempDeptCode()));
            Office office = offDAO.getOfficeDetails(mlForm.getHidTempAuthOffCode());
            mlForm.setOfficeName(office.getOffEn());
            mlForm.setEmpId(u.getEmpId());
            mlForm.setNotificationId(notificationId);
            mlForm.setLeaveId(leaveId);

            mav = new ModelAndView("/leaveAccount/ViewExtension");
            mav.addObject("leaveDetail", mlForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "DeleteExtension")
    public void deleteExtension(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            notificationDao.deleteNotificationData(notificationId, "LEAVE");
            MiscLeaveDAO.deleteMiscLeave(leaveId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "AbsentFromDutyList")
    public ModelAndView absentFromDutyList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm miscLeaveForm) {
        miscLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        ModelAndView mv = new ModelAndView("/leaveAccount/AbsentFromDutyList", "MiscLeaveForm", miscLeaveForm);
        mv.addObject("leaveList", MiscLeaveDAO.getLeaveList(selectedEmpObj.getEmpId(), "08"));
        return mv;
    }

    @RequestMapping(value = "AddAbsentFromDuty")
    public ModelAndView addAbsentFromDuty(HttpServletResponse response, Model model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm miscLeaveForm) {
        List deptlist = deptDAO.getDepartmentList();
        miscLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        ModelAndView mv = new ModelAndView("/leaveAccount/AddAbsentFromDuty", "MiscLeaveForm", miscLeaveForm);
        mv.addObject("deptlist", deptlist);
        return mv;
    }

    @RequestMapping(value = "saveAbsentFromDuty")
    public ModelAndView saveAbsentFromDuty(HttpServletResponse response, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscLeaveForm") MiscLeaveForm mLeaveForm, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        mLeaveForm.setEmpId(selectedEmpObj.getEmpId());
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("LEAVE");
                nb.setEmpId(mLeaveForm.getEmpId());
                nb.setDateofEntry(new Date());
                nb.setOrdno(mLeaveForm.getOrderNumber());
                nb.setOrdDate(sdf.parse(mLeaveForm.getOrderDate()));
                nb.setSancDeptCode(mLeaveForm.getHidTempDeptCode());
                nb.setSancOffCode(mLeaveForm.getHidTempAuthOffCode());
                nb.setSancAuthCode(mLeaveForm.getHidTempAuthPost());
                nb.setEntryDeptCode(lub.getLogindeptcode());
                nb.setEntryOffCode(lub.getLoginoffcode());
                nb.setEntryAuthCode(lub.getLoginspc());
                nb.setNote(mLeaveForm.getNote());

                if (mLeaveForm.getLeaveId() != null && !mLeaveForm.getLeaveId().trim().equals("")) {
                    nb.setNotid(mLeaveForm.getNotificationId());
                    notificationDao.modifyNotificationData(nb);
                    MiscLeaveDAO.updateMiscLeave(mLeaveForm, mLeaveForm.getNotificationId(), mLeaveForm.getLeaveId());
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    MiscLeaveDAO.saveMiscLeave(mLeaveForm, notid, "08");
                }
            } else if (submit != null && submit.equals("Delete")) {

                int retVal = notificationDao.deleteNotificationData(mLeaveForm.getNotificationId(), "LEAVE");

                if (retVal > 0) {
                    //punishmentDAO.deletePunishment(punishmentBean);
                }
            }

            //punishmentList = punishmentDAO.getPunishmentList(punishmentBean.getEmpId());
            mav = new ModelAndView("/leaveAccount/AbsentFromDutyList", "MiscLeaveForm", mLeaveForm);
            mav.addObject("leaveList", MiscLeaveDAO.getLeaveList(selectedEmpObj.getEmpId(), "08"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editAbsentFromDuty")
    public ModelAndView editAbsentFromDuty(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        ModelAndView mav = null;

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            mlForm = MiscLeaveDAO.getMiscLeaveData(leaveId);

            mlForm.setEmpId(u.getEmpId());
            mlForm.setNotificationId(notificationId);
            mlForm.setLeaveId(leaveId);

            mav = new ModelAndView("/leaveAccount/AddAbsentFromDuty", "MiscLeaveForm", mlForm);
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List offlist = offDAO.getTotalOfficeList(mlForm.getHidAuthDeptCode());
            mav.addObject("offlist", offlist);
            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(mlForm.getHidAuthOffCode());
            mav.addObject("postlist", postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewAbsentFromDuty")
    public ModelAndView viewAbsentFromDuty(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        ModelAndView mav = null;

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            mlForm = MiscLeaveDAO.getMiscLeaveData(leaveId);
            mlForm.setDepartmentName(subStantivePostDao.getDeptName(mlForm.getHidTempDeptCode()));
            Office office = offDAO.getOfficeDetails(mlForm.getHidTempAuthOffCode());
            mlForm.setOfficeName(office.getOffEn());
            mlForm.setEmpId(u.getEmpId());
            mlForm.setNotificationId(notificationId);
            mlForm.setLeaveId(leaveId);

            mav = new ModelAndView("/leaveAccount/ViewAbsentFromDuty");
            mav.addObject("leaveDetail", mlForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "DeleteAbsentFromDuty")
    public void deleteAbsentFromDuty(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscLeaveForm mlForm = new MiscLeaveForm();

        try {
            mlForm.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            notificationDao.deleteNotificationData(notificationId, "LEAVE");
            MiscLeaveDAO.deleteMiscLeave(leaveId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "LeaveOpeningBalance")
    public ModelAndView leaveOpeningBalance(HttpServletResponse response, Model model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LeaveOpeningBalanceForm") LeaveOpeningBalanceBean loBean) {
        //List ltList = MiscLeaveDAO.getLeaveTypeList();
        loBean.setEmpId(selectedEmpObj.getEmpId());
        ModelAndView mv = new ModelAndView("/leaveAccount/LeaveOpeningBalance", "LeaveOpeningBalanceForm", loBean);
        mv.addObject("obList", MiscLeaveDAO.getOpeningBalanceList(selectedEmpObj.getEmpId()));
        return mv;
    }

    @RequestMapping(value = "SaveOpeningBalance")
    public void saveOpeningBalance(HttpServletResponse response, Model model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) {
        //List ltList = MiscLeaveDAO.getLeaveTypeList();
        //selectedEmpObj.getEmpId()
        String leaveId = requestParams.get("leaveId");
        String lotId = requestParams.get("lotId");
        String openingBalanceDate = requestParams.get("openingBalanceDate");
        String time = requestParams.get("time");
        String openingBalance = requestParams.get("openingBalance");
        LeaveOpeningBalanceBean loBean = new LeaveOpeningBalanceBean();
        loBean.setEmpId(selectedEmpObj.getEmpId());
        loBean.setLeaveId(leaveId);
        loBean.setLobId(lotId);
        loBean.setOpeningBalance(openingBalance);
        loBean.setOpeningBalanceDate(openingBalanceDate);
        loBean.setTime(time);

        if (lotId != null && !lotId.trim().equals("")) {
            //Update
            MiscLeaveDAO.updateOpeningBalance(loBean);
        } else {
            MiscLeaveDAO.addOpeningBalance(loBean);
        }
    }

    @ResponseBody
    @RequestMapping(value = "DeleteOpeningBalance")
    public void deleteOpeningBalance(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {
        MiscLeaveForm mlForm = new MiscLeaveForm();
        try {
            mlForm.setEmpId(u.getEmpId());
            String lobId = requestParams.get("lobId");
            MiscLeaveDAO.deleteOpeningBalance(lobId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
