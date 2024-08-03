/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.redeployment;

import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.redeployment.RedeploymentDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.redeployment.Redeployment;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lenovo
 */
@Controller
@SessionAttributes({"Users", "SelectedEmpObj"})
public class RedeploymentController {

    @Autowired
    public RedeploymentDAO redeploymentDAO;
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
    public SubStantivePostDAO substantivePostDAO;

    @Autowired
    public PostDAO postDAO;

    @RequestMapping(value = "Redeployment")
    public ModelAndView RedeploymentList(@ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("redeploymentForm") Redeployment redeploymentForm) {
        ModelAndView mav = null;
        List redeploymentlist = null;
        try {
            redeploymentlist = redeploymentDAO.findAllRedeployment(lub.getEmpId());
            mav = new ModelAndView("/redeployment/RedeploymentList", "redeploymentForm", redeploymentForm);
            mav.addObject("redeploymentlist", redeploymentlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;

    }

    @RequestMapping(value = "RedeploymentList")
    public ModelAndView NewRedeployment(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("redeploymentForm") Redeployment redeploymentForm) throws IOException {

        ModelAndView mav = null;
        try {
            redeploymentForm.setEmpid(u.getEmpId());
            mav = new ModelAndView("/redeployment/RedeploymentData", "redeploymentForm", redeploymentForm);
            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveRedeployment")
    public ModelAndView saveRedeploymentData(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("redeploymentForm") Redeployment redeploymentForm) throws ParseException {
        ModelAndView mav = null;
        List redeploymentlist = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            redeploymentForm.setEmpid(selectedEmpObj.getEmpId());
            nb.setNottype("REDEPLOYMENT");
            nb.setEmpId(selectedEmpObj.getEmpId());
            nb.setDateofEntry(new Date());
            nb.setOrdno(redeploymentForm.getOrdno());
            nb.setOrdDate(sdf.parse(redeploymentForm.getOrdDate()));
            nb.setSancDeptCode(redeploymentForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(redeploymentForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(redeploymentForm.getNotifyingSpc());
            nb.setNote(redeploymentForm.getNote());
            if (redeploymentForm.getChkNotSBPrint() != null && redeploymentForm.getChkNotSBPrint().equals("Y")) {
                nb.setIfVisible("N");
            } else {
                nb.setIfVisible("Y");
            }
            if (redeploymentForm.getHnotid() > 0) {
                nb.setNotid(redeploymentForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                redeploymentDAO.updateRedeploymentData(redeploymentForm);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                redeploymentDAO.insertRedeploymentData(redeploymentForm, notid);
            }
            redeploymentlist = redeploymentDAO.findAllRedeployment(selectedEmpObj.getEmpId());
            mav = new ModelAndView("/redeployment/RedeploymentList", "redeploymentForm", redeploymentForm);
            mav.addObject("redeploymentlist", redeploymentlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editRedeployment")
    public ModelAndView editRedeployment(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {
        Redeployment redeploymentForm = null;
        ModelAndView mav = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            int notId = Integer.parseInt(requestParams.get("notId"));
            redeploymentForm = redeploymentDAO.editRedeployment(notId, empid);
            redeploymentForm.setHnotid(notId);
            mav = new ModelAndView("/redeployment/RedeploymentData", "redeploymentForm", redeploymentForm);
            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List notifyingOfflist = offDAO.getTotalOfficeList(redeploymentForm.getHidNotifyingDeptCode());
            List notifyingPostlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(redeploymentForm.getHidNotifyingOffCode());

            List postedOfflist = offDAO.getTotalOfficeList(redeploymentForm.getHidPostedDeptCode());
            List postedPostlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(redeploymentForm.getHidPostedOffCode());

            // List allotDescList = promotionDAO.getAllotDescList("PROMOTION");
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("notifyingOfflist", notifyingOfflist);
            mav.addObject("notifyingPostlist", notifyingPostlist);
            //mav.addObject("genpostlist", genpostlist);
            //mav.addObject("allotDescList", allotDescList);
            mav.addObject("lvlList", lvlList);
            mav.addObject("postedOfflist", postedOfflist);
            mav.addObject("postedPostlist", postedPostlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;

    }

    @RequestMapping(value = "deleteRedeployment")
    public String deleteRedeployment(@RequestParam("notId") String notId) {
        int deletestatus = 0;
        deletestatus = redeploymentDAO.deleteRedeployment(notId);

        return "/redeployment/Redeployment";
    }

    @RequestMapping(value = "updateRedeployment")
    public void updateRedeploymentData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("redeploymentForm") Redeployment redeploymentForm) throws ParseException {
        int redeploymentdata = 0;
        try {
            redeploymentForm.setEmpid(selectedEmpObj.getEmpId());
            redeploymentdata = redeploymentDAO.updateRedeploymentData(redeploymentForm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
