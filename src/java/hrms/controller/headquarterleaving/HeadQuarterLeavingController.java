/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.headquarterleaving;

import hrms.dao.headquarterleaving.HeadQuarterLeavingDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.LeaveTypeDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.headquarterleaving.HeadQuarterLeaving;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@SessionAttributes({"LoginUserBean", "SelectedEmpObj", "LoginUserBean"})
//@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class HeadQuarterLeavingController {

    @Resource(name = "dataSource")
    protected DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Autowired
    public HeadQuarterLeavingDAO headquarterleavingDao;

    @Autowired
    public DepartmentDAO deptDAO;

    @Autowired
    public OfficeDAO offDAO;
    
     @Autowired
    LeaveTypeDAO leaveTypeDAO ;

    @Autowired
    public NotificationDAO notificationDao;

    @Autowired
    public SubStantivePostDAO substantivePostDAO;

    @RequestMapping(value = "HaedQuarterLeavingList")
    public ModelAndView HaedQuarterLeavingList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("leavingForm") HeadQuarterLeaving leavingForm) {

        List leavingList = null;
        ModelAndView mav = null;
        try {
            
            leavingForm.setEmpid(u.getEmpId());
            leavingList = headquarterleavingDao.HaedQuarterLeavingList(u.getEmpId());

            mav = new ModelAndView("/headquarterleaving/HeadQuarterLeavingList", "leavingForm", leavingForm);
            mav.addObject("LeavingList", leavingList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newPermission")
    public ModelAndView newPermission(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("leavingForm") HeadQuarterLeaving leavingForm) throws IOException {
        ModelAndView mav = null;
        try {
            leavingForm.setEmpid(u.getEmpId());
            mav = new ModelAndView("/headquarterleaving/AddPermission", "leavingForm", leavingForm);
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "savePermission")
    public ModelAndView savePermission(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leavingForm") HeadQuarterLeaving leavingForm) throws IOException {

        try {
            String ent_deptCode = lub.getLogindeptcode();
            String ent_officeCode = lub.getLoginoffcode();
            String ent_auth = lub.getLoginspc();
            List leavinglist = null;
            headquarterleavingDao.savePermission(leavingForm, ent_deptCode, ent_officeCode, ent_auth);
            // leavinglist = headquarterleavingDao.HaedQuarterLeavingList(leavingForm.getEmpid());
            // mav = new ModelAndView("/headquarterleaving/HeadQuarterLeavingList", "leavingForm", leavingForm);
            //  mav.addObject("leavinglist", leavinglist);     

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/HaedQuarterLeavingList.htm");
    }

    @RequestMapping(value = "editPermisison")
    public ModelAndView editPermisison(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        HeadQuarterLeaving leavingForm = new HeadQuarterLeaving();

        ModelAndView mav = null;

        try {
            leavingForm.setEmpid(u.getEmpId());
            String permissionId = requestParams.get("permissionId");
            leavingForm.setPermissionId(permissionId);
            List deptlist = deptDAO.getDepartmentList();                   
            
            leavingForm = headquarterleavingDao.getEmpPermissionData(leavingForm, permissionId, u.getEmpId());
            mav = new ModelAndView("/headquarterleaving/EditPermission", "leavingForm", leavingForm);
            mav.addObject("deptlist", deptlist);          
            List offlist = offDAO.getTotalOfficeList(leavingForm.getHidAuthDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(leavingForm.getHidAuthOffCode());
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "updatePermission")
    public ModelAndView updatePermission(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leavingForm") HeadQuarterLeaving leavingForm) throws IOException {

        try {
            String ent_deptCode = lub.getLogindeptcode();
            String ent_officeCode = lub.getLoginoffcode();
            String ent_auth = lub.getLoginspc();
            List leavinglist = null;
            headquarterleavingDao.updatePermission(leavingForm, ent_deptCode, ent_officeCode, ent_auth);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/HaedQuarterLeavingList.htm");
    }

    @RequestMapping(value = "DetentionList")
    public ModelAndView DetentionList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("leavingForm") HeadQuarterLeaving leavingForm) {

        List detentionList = null;
        ModelAndView mav = null;
        try {
            leavingForm.setEmpid(u.getEmpId());
            detentionList = headquarterleavingDao.DetentionList(u.getEmpId());

            mav = new ModelAndView("/headquarterleaving/DetentionList", "leavingForm", leavingForm);
            mav.addObject("detentionList", detentionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "newDetention")
    public ModelAndView newDetention(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("leavingForm") HeadQuarterLeaving leavingForm) throws IOException {
        ModelAndView mav = null;
        try {
            leavingForm.setEmpid(u.getEmpId());
            mav = new ModelAndView("/headquarterleaving/AddDetention", "leavingForm", leavingForm);
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
             List leavelist = leaveTypeDAO.getLeaveTypeList();    
            mav.addObject("leavelist", leavelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "saveDetention")
    public ModelAndView saveDetention(HttpServletResponse response, Model model, @ModelAttribute("leavingForm") HeadQuarterLeaving leavingForm, @RequestParam("submit") String submit, @ModelAttribute("LoginUserBean") LoginUserBean lub) throws ParseException {

        NotificationBean nb = new NotificationBean();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        ModelAndView mav = null;
        List rewardlist = null;
        try {
            if (submit != null && submit.equals("Save")) {
                String ent_deptCode = lub.getLogindeptcode();
                String ent_officeCode = lub.getLoginoffcode();
                String ent_auth = lub.getLoginspc();
                nb.setNottype("DET_VAC");
                nb.setEmpId(leavingForm.getEmpid());
                nb.setDateofEntry(new Date());
                nb.setOrdno(leavingForm.getOrdno());
                if (leavingForm.getFdate() != null) {
                    nb.setOrdDate(new java.sql.Date(leavingForm.getOrdt().getTime()));
                } else {
                    nb.setOrdDate(null);

                }
                

                nb.setSancDeptCode(leavingForm.getHidAuthDeptCode());
                nb.setSancOffCode(leavingForm.getHidAuthOffCode());
                nb.setSancAuthCode(leavingForm.getAuthSpc());
                nb.setNote(leavingForm.getNote());
                nb.setEntryDeptCode(ent_deptCode);
                nb.setEntryOffCode(ent_officeCode);
                nb.setEntryAuthCode(ent_auth);
                if(leavingForm.getChkNotSBPrintnew()!=null && leavingForm.getChkNotSBPrintnew().equals("Y")){
                    nb.setIfVisible("N");
                }else{
                    nb.setIfVisible("Y");
                }
                if (leavingForm.getHnotid() >0) {
                    nb.setNotid(leavingForm.getHnotid());
                    notificationDao.modifyNotificationData(nb);
                    //  headquarterleavingDao.updateReward(leavingForm);
                } else {
                    int notid = notificationDao.insertNotificationData(nb);
                    headquarterleavingDao.saveDetention(leavingForm, notid);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/DetentionList.htm");
    }
    
    @RequestMapping(value = "editDetention")
    public ModelAndView editDetention(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, Model model, @RequestParam Map<String, String> requestParams) throws IOException {

        HeadQuarterLeaving leavingForm = new HeadQuarterLeaving();

        ModelAndView mav = null;

        try {
            leavingForm.setEmpid(u.getEmpId());
            String detentionId = requestParams.get("detentionId");
            leavingForm.setDv_id(detentionId);
           
            List deptlist = deptDAO.getDepartmentList();
            leavingForm = headquarterleavingDao.getEmpDetentionData(leavingForm, detentionId, u.getEmpId());
            mav = new ModelAndView("/headquarterleaving/EditDetention", "leavingForm", leavingForm);
            mav.addObject("deptlist", deptlist);
            List offlist = offDAO.getTotalOfficeList(leavingForm.getHidAuthDeptCode());
            List postlist = substantivePostDAO.getSanctioningSPCOfficeWiseList(leavingForm.getHidAuthOffCode());
            mav.addObject("offlist", offlist);
            mav.addObject("postlist", postlist);
               List leavelist = leaveTypeDAO.getLeaveTypeList();    
            mav.addObject("leavelist", leavelist);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    @RequestMapping(value = "updateDetention")
    public ModelAndView updateDetention(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("leavingForm") HeadQuarterLeaving leavingForm) throws IOException {

        try {
            String ent_deptCode = lub.getLogindeptcode();
            String ent_officeCode = lub.getLoginoffcode();
            String ent_auth = lub.getLoginspc();
            List leavinglist = null;
            headquarterleavingDao.updateDetention(leavingForm, ent_deptCode, ent_officeCode, ent_auth);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/DetentionList.htm");
    }
	
	

}
