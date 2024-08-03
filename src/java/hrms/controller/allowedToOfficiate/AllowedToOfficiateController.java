/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.allowedToOfficiate;

import hrms.dao.allowedToOfficiate.AllowedToOfficiateDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.allowedToOfficiate.AllowedToOfficiateModel;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Madhusmita
 */
@Controller
@SessionAttributes({"Users", "SelectedEmpObj"})
public class AllowedToOfficiateController {

    @Autowired
    public AllowedToOfficiateDAO allowedtoOfficiateDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public PayScaleDAO payscaleDAO;

    @Autowired
    public CadreDAO cadreDAO;

    @Autowired
    public SubStantivePostDAO subStantivePostDao;

    @Autowired
    public PostDAO postDAO;

    @RequestMapping(value = "allowedToOfficiationList")
    public ModelAndView AllowedToOfficiateList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("allowedToOfficiateForm") AllowedToOfficiateModel allowedToOfficiateForm) {

        List officiationList = null;
        ModelAndView mv = new ModelAndView();
        try {
            System.out.println("find list");
            officiationList = allowedtoOfficiateDAO.findallOfficiation(u.getEmpId());
            mv = new ModelAndView("/allowedToOfficiate/AllowedToOfficiateList", "allowedToOfficiateForm", allowedToOfficiateForm);
            mv.addObject("officiationList", officiationList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @RequestMapping(value = "AllowedToOfficiateList")
    public ModelAndView NewOfficiation(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("allowedToOfficiateForm") AllowedToOfficiateModel allowedToOfficiateForm)
            throws IOException {

        ModelAndView mav = null;

        try {

            allowedToOfficiateForm.setEmpid(u.getEmpId());
            mav = new ModelAndView("/allowedToOfficiate/AllowedToOfficiateData", "allowedToOfficiateForm", allowedToOfficiateForm);
            List deptlist = deptDAO.getDepartmentList();
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("lvlList", lvlList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveOfficiation")
    public String saveAllowedToOfficiateData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("allowedToOfficiateForm") AllowedToOfficiateModel allowedToOfficiateForm) throws ParseException {

        ModelAndView mav = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String doe = sdf.format(new Date());
        try {
            allowedToOfficiateForm.setEmpid(selectedEmpObj.getEmpId());
            nb.setNottype("OFFICIATE");
            nb.setEmpId(selectedEmpObj.getEmpId());
            nb.setDateofEntry(sdf.parse(doe));
            nb.setOrdno(allowedToOfficiateForm.getOrdno());
            nb.setOrdDate(sdf.parse(allowedToOfficiateForm.getOrdDate()));
            nb.setSancDeptCode(allowedToOfficiateForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(allowedToOfficiateForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(allowedToOfficiateForm.getNotifyingSpc());
            nb.setNote(allowedToOfficiateForm.getNote());

            if (allowedToOfficiateForm.getChkNotSBPrint() != null && allowedToOfficiateForm.getChkNotSBPrint().equals("Y")) {
                nb.setIfVisible("N");
            } else {
                nb.setIfVisible("Y");
            }
            if (allowedToOfficiateForm.getHnotid() > 0) {
                nb.setNotid(allowedToOfficiateForm.getHnotid());
                System.out.println("get not id:"+nb.getNotid());
                notificationDao.modifyNotificationData(nb);
                allowedtoOfficiateDAO.updateOfficiationData(allowedToOfficiateForm, nb.getNotid());
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                allowedtoOfficiateDAO.insertOfficiationData(allowedToOfficiateForm, notid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/allowedToOfficiationList.htm";
    }

    @RequestMapping(value = "deleteOfficiation")
    public String deleteOfficiation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("LoginUserBean") LoginUserBean lub1,
            @ModelAttribute("allowedToOfficiateForm") AllowedToOfficiateModel allowedToOfficiateForm, @RequestParam Map<String, String> requestParams) {
        int deletestatus = 0;
        ModelAndView mav = null;
        int notId = Integer.parseInt(requestParams.get("notId"));
        String cadreId = requestParams.get("cadreId");
        allowedToOfficiateForm.setEmpid(selectedEmpObj.getEmpId());
        allowedToOfficiateForm.setNotId(notId);
        allowedToOfficiateForm.sethCadId(cadreId);
        deletestatus = allowedtoOfficiateDAO.deleteOfficiationData(allowedToOfficiateForm);
        notificationDao.deleteNotificationData(notId, "OFFICIATE");

        return "redirect:/allowedToOfficiationList.htm";
    }

    @RequestMapping(value = "editOfficiation")
    public ModelAndView editOfficiation(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj,
            @ModelAttribute("allowedToOfficiateForm") AllowedToOfficiateModel allowedToOfficiateForm,
            @RequestParam Map<String, String> requestParams) {
        ModelAndView mav = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            int notId = Integer.parseInt(requestParams.get("notId"));
            allowedToOfficiateForm = allowedtoOfficiateDAO.editOfficiationData(notId, empid);
            mav = new ModelAndView("/allowedToOfficiate/AllowedToOfficiateData", "allowedToOfficiateForm", allowedToOfficiateForm);
            List deptlist = deptDAO.getDepartmentList();
            List offList = offDAO.getTotalOfficeList(allowedToOfficiateForm.getSltCadreDept());
            List cadrelist = cadreDAO.getCadreList(allowedToOfficiateForm.getSltCadreDept());
            List notoffList = offDAO.getTotalOfficeList(allowedToOfficiateForm.getHidNotifyingDeptCode());
            List notSpc = subStantivePostDao.getSanctioningSPCOfficeWiseList(allowedToOfficiateForm.getHidNotifyingOffCode());
            List genpostlist = postDAO.getPostList(allowedToOfficiateForm.getSltPostingDept());
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("offList", offList);
            mav.addObject("cadrelist", cadrelist);
            mav.addObject("lvlList", lvlList);
            mav.addObject("notoffList", notoffList);
            mav.addObject("notSpc", notSpc);
            mav.addObject("genpostlist", genpostlist);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

}
