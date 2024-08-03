/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.empinfo;

import hrms.common.CommonFunctions;
import hrms.dao.empinfo.EmployeeInformationDAO;
import hrms.dao.employee.EmployeeDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.empinfo.MiscInfoBean;
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
public class MiscellaneousController {

    @Autowired
    public EmployeeInformationDAO empinfoDAO;
    @Autowired
    public NotificationDAO notificationDao;
    @Autowired
    public DepartmentDAO deptDAO;
    @Autowired
    public OfficeDAO offDAO;
    @Autowired
    SubStantivePostDAO subStantivePostDao;
    @Autowired
    ServiceBookLanguageDAO sbDAO;

    @RequestMapping(value = "MiscInfoList")
    public ModelAndView miscInfoList(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscInfoForm") MiscInfoBean miscInfoBean) {
        miscInfoBean.setEmpId(selectedEmpObj.getEmpId());
        ModelAndView mv = new ModelAndView("/empinfo/MiscellaneousInfo", "miscInfoForm", miscInfoBean);
        mv.addObject("miscInfoList", empinfoDAO.getMiscInfoList(selectedEmpObj.getEmpId()));
        return mv;
    }

    @RequestMapping(value = "AddMiscInfo")
    public ModelAndView addUnavailedLeave(HttpServletResponse response, Model model,
            @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscInfoForm") MiscInfoBean miscInfoBean) {

        miscInfoBean.setEmpId(selectedEmpObj.getEmpId());

        if (miscInfoBean.getRadpostingauthtype() == null || miscInfoBean.getRadpostingauthtype().equals("")) {
            miscInfoBean.setRadpostingauthtype("GOO");
        }

        List deptlist = deptDAO.getDepartmentList();
        List otherOrgOfflist = offDAO.getOtherOrganisationList();

        ModelAndView mv = new ModelAndView("/empinfo/AddMiscInfo", "MiscInfoForm", miscInfoBean);

        mv.addObject("deptlist", deptlist);
        mv.addObject("otherOrgOfflist", otherOrgOfflist);

        return mv;
    }

    @RequestMapping(value = "saveMiscInfo")
    public ModelAndView saveMiscInfo(HttpServletResponse response, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("MiscInfoForm") MiscInfoBean miscInfoBean, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        ModelAndView mv = null;
        miscInfoBean.setEmpId(selectedEmpObj.getEmpId());
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("MISCELLANEOUS");
                nb.setEmpId(miscInfoBean.getEmpId());
                nb.setDateofEntry(new Date());
                nb.setOrdno(miscInfoBean.getOrderNumber());
                nb.setOrdDate(sdf.parse(miscInfoBean.getOrderDate()));
                nb.setSancDeptCode(miscInfoBean.getHidTempDeptCode());
                nb.setSancOffCode(miscInfoBean.getHidTempAuthOffCode());
                nb.setSancAuthCode(miscInfoBean.getHidTempAuthPost());
                nb.setEntryDeptCode(lub.getLogindeptcode());
                nb.setEntryOffCode(lub.getLoginoffcode());
                nb.setEntryAuthCode(lub.getLoginspc());
                nb.setNote(miscInfoBean.getNote());
                nb.setIfVisible(miscInfoBean.getIfVisible());
                nb.setRadpostingauthtype(miscInfoBean.getRadpostingauthtype());
                if (miscInfoBean.getNotificationId() > 0) {
                    nb.setNotid(miscInfoBean.getNotificationId());
                    notificationDao.modifyNotificationData(nb);
                    String sblang=sbDAO.getMiscllaneousString(nb, miscInfoBean.getNotificationId(), "MISCELLANEOUS");
                    notificationDao.saveServiceBookLanguage(sblang, miscInfoBean.getNotificationId(), "MISCELLANEOUS");

                } else {
                    int notId = notificationDao.insertNotificationData(nb);
                    String sblang=sbDAO.getMiscllaneousString(nb,notId , "MISCELLANEOUS");
                    notificationDao.saveServiceBookLanguage(sblang, notId, "MISCELLANEOUS");
                }
            } else if (submit != null && submit.equals("Delete")) {

                //int retVal = notificationDao.deleteNotificationData(mLeaveForm.getNotificationId(), "LEAVE");
                /*
                 if (retVal > 0) {
                 //punishmentDAO.deletePunishment(punishmentBean);
                 }*/
            }

            //punishmentList = punishmentDAO.getPunishmentList(punishmentBean.getEmpId());
            mv = new ModelAndView("/empinfo/MiscellaneousInfo", "miscInfoForm", miscInfoBean);
            mv.addObject("miscInfoList", empinfoDAO.getMiscInfoList(selectedEmpObj.getEmpId()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "editMiscInfo")
    public ModelAndView editMiscInfo(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscInfoBean mBean = new MiscInfoBean();
        NotificationBean nb = new NotificationBean();
        ModelAndView mav = null;

        try {

            int notificationId = Integer.parseInt(requestParams.get("notId"));

            nb = notificationDao.dispalyNotificationData(notificationId, "MISCELLANEOUS");
            mBean = empinfoDAO.getMiscInfoData(nb);
            if (mBean.getRadpostingauthtype() == null || mBean.getRadpostingauthtype().equals("")) {
                mBean.setRadpostingauthtype("GOO");
            }
            mBean.setEmpId(u.getEmpId());

            mav = new ModelAndView("/empinfo/AddMiscInfo", "MiscInfoForm", mBean);

            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);

            List otherOrgOfflist = offDAO.getOtherOrganisationList();
            mav.addObject("otherOrgOfflist", otherOrgOfflist);

            List offlist = offDAO.getTotalOfficeList(mBean.getHidAuthDeptCode());
            mav.addObject("offlist", offlist);

            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(mBean.getHidAuthOffCode());
            mav.addObject("postlist", postlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "viewMiscInfo")
    public ModelAndView viewMiscInfo(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscInfoBean mBean = new MiscInfoBean();
        NotificationBean nb = new NotificationBean();
        ModelAndView mav = null;

        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            nb = notificationDao.dispalyNotificationData(notificationId, "MISCELLANEOUS");
            mBean = empinfoDAO.getMiscInfoData(nb);
            mBean.setEmpId(u.getEmpId());
            mBean.setDepartmentName(subStantivePostDao.getDeptName(mBean.getHidTempDeptCode()));
            Office office = offDAO.getOfficeDetails(mBean.getHidTempAuthOffCode());
            mBean.setOfficeName(office.getOffEn());
            mBean.setEmpId(u.getEmpId());
            mBean.setNotificationId(notificationId);
            mav = new ModelAndView("/empinfo/ViewMiscInfo");
            mav.addObject("miscDetail", mBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    //DeleteMiscInfo
    @ResponseBody
    @RequestMapping(value = "DeleteMiscInfo")
    public void deleteMiscInfo(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        MiscInfoBean mBean = new MiscInfoBean();

        try {
            mBean.setEmpId(u.getEmpId());
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            notificationDao.deleteNotificationData(notificationId, "MISCELLANEOUS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
