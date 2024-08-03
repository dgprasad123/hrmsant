/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.breakinservice;

import hrms.common.CommonFunctions;
import hrms.dao.breakinservice.BreakinserviceDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.breakinservice.BreakinserviceBean;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Manoj PC
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class BreakinserviceController implements ServletContextAware {

    @Autowired
    public DepartmentDAO deptDAO;
    @Autowired
    public BreakinserviceDAO breakinserviceDAO;
    @Autowired
    public OfficeDAO offDAO;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    SubStantivePostDAO subStantivePostDao;
    private ServletContext context;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    @RequestMapping(value = "AddBreakinService")
    public ModelAndView AddBreakinService(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("BreakinserviceBean") BreakinserviceBean bsBean) {
        ModelAndView mv = new ModelAndView();
        String path = "breakinservice/AddBreakinService";
        List deptlist = deptDAO.getDepartmentList();
        
        mv.addObject("deptlist", deptlist);
        
        mv.addObject("empId", lub.getEmpId());
        mv.setViewName(path);
        return mv;

    }
     @RequestMapping(value = "saveBreakinService")
    public ModelAndView savePunishment(HttpServletResponse response, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("BreakinserviceBean") BreakinserviceBean bisBean, @RequestParam("submit") String submit) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List bisList = null;
        try {
            if (submit != null && submit.equals("Save")) {
                nb.setNottype("LEAVE");
                nb.setEmpId(bisBean.getEmpId());
                nb.setDateofEntry(new Date());
                nb.setOrdno(bisBean.getOrderNumber());
                nb.setOrdDate(sdf.parse(bisBean.getOrderDate()));
                nb.setSancDeptCode(bisBean.getHidTempDeptCode());
                nb.setSancOffCode(bisBean.getHidTempAuthOffCode());
                nb.setSancAuthCode(bisBean.getHidTempAuthPost());
                nb.setEntryDeptCode(lub.getLogindeptcode());
                nb.setEntryOffCode(lub.getLoginoffcode());
                nb.setEntryAuthCode(lub.getLoginspc());
                nb.setNote(bisBean.getNote());
                if(bisBean.getChkNotSBPrint()!=null && bisBean.getChkNotSBPrint().equals("Y")){
                    nb.setIfVisible("N");
                }else{
                    nb.setIfVisible("Y");
                }
                
                if (bisBean.getLeaveId()!= null && !bisBean.getLeaveId().trim().equals("")) {
                    nb.setNotid(bisBean.getNotificationId());
                    notificationDao.modifyNotificationData(nb);
                    breakinserviceDAO.updateBreakinService(bisBean);
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    breakinserviceDAO.saveBreakinService(bisBean, notid);
                }
            } else if (submit != null && submit.equals("Delete")) {
                
                int retVal = notificationDao.deleteNotificationData(bisBean.getNotificationId(), "LEAVE");
                
                if (retVal > 0) {
                    breakinserviceDAO.deleteBreakinService(bisBean);
                }
            }

            bisList = breakinserviceDAO.getBreakinserviceList(bisBean.getEmpId());
            mav = new ModelAndView("/breakinservice/BreakinserviceList", "BreakinserviceBean", bisBean);
            mav.addObject("bisList", bisList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    @RequestMapping(value = "BreakinServiceList")
    public ModelAndView BreakinServiceList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("BreakinserviceBean") BreakinserviceBean bisBean) {

        List punishmentList = null;

        ModelAndView mav = null;
        try {
            bisBean.setEmpId(u.getEmpId());
            punishmentList = breakinserviceDAO.getBreakinserviceList(bisBean.getEmpId());

            mav = new ModelAndView("/breakinservice/BreakinserviceList", "BreakinserviceBean", bisBean);
            mav.addObject("bisList", punishmentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
        @RequestMapping(value = "editBreakinservice")
    public ModelAndView editPunishment(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams, @ModelAttribute("BreakinserviceBean") BreakinserviceBean bisBean) throws IOException {

        //BreakinserviceBean bisBean = new BreakinserviceBean();

        ModelAndView mav = null;

        try {
            bisBean.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            bisBean = breakinserviceDAO.getEmpBISData(leaveId);

            bisBean.setEmpId(u.getEmpId());
            bisBean.setNotificationId(notificationId);
            bisBean.setLeaveId(leaveId);

            mav = new ModelAndView("breakinservice/AddBreakinService", "BreakinserviceBean", bisBean);
           
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            List offlist = offDAO.getTotalOfficeList(bisBean.getHidAuthDeptCode());
            mav.addObject("offlist", offlist);
            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(bisBean.getHidAuthOffCode());
            mav.addObject("postlist", postlist);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}
