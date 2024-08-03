/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.controller.roabsence;

import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.roabsence.RoabsenceDAO;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.roabsence.RoabsenceBean;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class RoabsenceController  implements ServletContextAware{
    @Autowired
    public DepartmentDAO deptDAO;
    @Autowired
    public RoabsenceDAO roabsenceDAO;
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

    @RequestMapping(value = "AddRoabsence")
    public ModelAndView AddRoabsence(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users lub, @ModelAttribute("RoabsenceBean") RoabsenceBean roaBean) {
        ModelAndView mv = new ModelAndView();
        String path = "roabsence/AddRoabsence";
        List deptlist = deptDAO.getDepartmentList();
        
        mv.addObject("deptlist", deptlist);
        
        mv.addObject("empId", lub.getEmpId());
        mv.setViewName(path);
        return mv;

    }
     @RequestMapping(value = "saveRoabsence")
    public ModelAndView savePunishment(HttpServletResponse response, Model model, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("RoabsenceBean") RoabsenceBean bisBean, @RequestParam("submit") String submit) throws ParseException {

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
                    roabsenceDAO.updateRoabsence(bisBean);
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    roabsenceDAO.saveRoabsence(bisBean, notid);
                }
            } else if (submit != null && submit.equals("Delete")) {
                
                int retVal = notificationDao.deleteNotificationData(bisBean.getNotificationId(), "LEAVE");
                
                if (retVal > 0) {
                    roabsenceDAO.deleteRoabsence(bisBean);
                }
            }

            bisList = roabsenceDAO.getRoabsenceList(bisBean.getEmpId());
            mav = new ModelAndView("/roabsence/RoabsenceList", "RoabsenceBean", bisBean);
            mav.addObject("bisList", bisList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    @RequestMapping(value = "RoabsenceList")
    public ModelAndView RoabsenceList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("RoabsenceBean") RoabsenceBean bisBean) {

        List punishmentList = null;

        ModelAndView mav = null;
        try {
            bisBean.setEmpId(u.getEmpId());
            punishmentList = roabsenceDAO.getRoabsenceList(bisBean.getEmpId());

            mav = new ModelAndView("/roabsence/RoabsenceList", "RoabsenceBean", bisBean);
            mav.addObject("bisList", punishmentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
        @RequestMapping(value = "editRoabsence")
    public ModelAndView editPunishment(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams, @ModelAttribute("RoabsenceBean") RoabsenceBean bisBean) throws IOException {

        //BreakinserviceBean bisBean = new BreakinserviceBean();

        ModelAndView mav = null;

        try {
            bisBean.setEmpId(u.getEmpId());

            String leaveId = requestParams.get("leaveId");
            int notificationId = Integer.parseInt(requestParams.get("notId"));

            bisBean = roabsenceDAO.getEmpRoabsenceData(leaveId);

            bisBean.setEmpId(u.getEmpId());
            bisBean.setNotificationId(notificationId);
            bisBean.setLeaveId(leaveId);

            mav = new ModelAndView("roabsence/AddRoabsence", "RoabsenceBean", bisBean);
           
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
