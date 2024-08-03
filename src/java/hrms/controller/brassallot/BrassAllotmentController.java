/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.brassallot;

import hrms.dao.brassallot.BrassAllotDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.brassallot.BrassAllotList;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class BrassAllotmentController {
    
    @Autowired
    public BrassAllotDAO brassallotDao;
    
    @Autowired
    public DepartmentDAO deptDAO;
    
    @Autowired
    public NotificationDAO notificationDao;
    
    @Autowired
    public DistrictDAO districtDAO;
    
    @Autowired
    public OfficeDAO officeDao;
    
    @Autowired
    SubStantivePostDAO subStantivePostDao;
    
    @RequestMapping(value = "empBrassAllotList")
    public ModelAndView getEmpBrassAllotList(ModelMap model, HttpServletResponse response, @ModelAttribute("empBrassList") BrassAllotList bsallot, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        String path = "/brassallot/BrassAllotList";
        
        ModelAndView mav = new ModelAndView();
        ArrayList brassList = brassallotDao.findAllBrassAllot(selectedEmpObj.getEmpId(), bsallot);
        mav.addObject("brassList", brassList);
        mav.setViewName(path);
        return mav;
    }
    
    @RequestMapping(value = "empBrassAllotList", params = {"submit=AddNew"})
    public ModelAndView addNewEmpBrass(ModelMap model, HttpServletResponse response, @ModelAttribute("addNewBrassForm") BrassAllotList bsallot) {
        
        String path = "/brassallot/BrassAllotEdit";
        ModelAndView mav = new ModelAndView();
        List deptlist = deptDAO.getDepartmentList();
        List distlist = districtDAO.getDistrictList();
        mav.addObject("deptlist", deptlist);
        mav.addObject("distlist", distlist);
        mav.setViewName(path);
        return mav;
    }
    
    @RequestMapping(value = "addNewBrass")
    public ModelAndView Save(ModelMap model, HttpServletResponse response, @ModelAttribute("addNewBrassForm") BrassAllotList bsallot, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {
        
        String path = "/brassallot/BrassAllotList";
        NotificationBean nb = new NotificationBean();
        //String hidnotificationId = requestParams.get("hidNotID");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        
        ModelAndView mav = new ModelAndView();
        
        try {
            
            nb.setNottype("BRASS_ALLOT");
            nb.setEmpId(selectedEmpObj.getEmpId());
            nb.setDateofEntry(new Date());
            nb.setOrdno(bsallot.getOrdno());
            nb.setOrdDate(sdf.parse(bsallot.getOrdDate()));
            nb.setSancDeptCode(bsallot.getHidVerifyingDeptCode());
            nb.setSancOffCode(bsallot.getHidVerifyingOffCode());
            nb.setSancAuthCode(bsallot.getHidVerifyingSpc());
            nb.setNote(bsallot.getNote());
            if (bsallot.getChkNotSBPrint() != null && bsallot.getChkNotSBPrint().equals("Y")) {
                nb.setIfVisible("N");
            }
            
            if (bsallot.getNotId() > 0) {
                nb.setNotid(bsallot.getNotId());
                notificationDao.modifyNotificationData(nb);
                brassallotDao.updateBrassAllotData(bsallot, selectedEmpObj.getEmpId());
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                bsallot.setNotId(notid);
                bsallot.setNotType("BRASS_ALLOT");
                brassallotDao.insertBrassAllotData(bsallot, selectedEmpObj.getEmpId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList brassList = brassallotDao.findAllBrassAllot(selectedEmpObj.getEmpId(), bsallot);
        mav.addObject("brassList", brassList);
        mav.setViewName(path);
        return mav;
        
    }
    
    @RequestMapping(value = "addNewBrass", params = "submit=Back")
    public ModelAndView BackToLIst(ModelMap model, HttpServletResponse response, @ModelAttribute("addNewBrassForm") BrassAllotList bsallot, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj) {
        
        String path = "/brassallot/BrassAllotList";
        ArrayList brassList = brassallotDao.findAllBrassAllot(selectedEmpObj.getEmpId(), bsallot);
        ModelAndView mav = new ModelAndView();
        mav.addObject("brassList", brassList);
        mav.setViewName(path);
        
        return mav;
    }
    
    @RequestMapping(value = "editBrassAllot")
    public ModelAndView editBrassAllotment(ModelMap model, HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("addNewBrassForm") BrassAllotList bsallot, @RequestParam Map<String, String> requestParams) throws IOException {
        bsallot = new BrassAllotList();
        
        ModelAndView mav = null;
        
        try {
            
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            
            bsallot = brassallotDao.editBrassAllotData(selectedEmpObj.getEmpId(), notificationId);
            
            mav = new ModelAndView("/brassallot/BrassAllotEdit", "addNewBrassForm", bsallot);
            bsallot.setEmpId(selectedEmpObj.getEmpId());
            bsallot.setNotId(notificationId);
            List deptlist = deptDAO.getDepartmentList();
            mav.addObject("deptlist", deptlist);
            
            List offlist = officeDao.getTotalOfficeList(bsallot.getSltDept());
            mav.addObject("offlist", offlist);
            List offlist1 = officeDao.getTotalOfficeList(bsallot.getHidVerifyingDeptCode());
            mav.addObject("offlist1", offlist1);
            
            List distlist = districtDAO.getDistrictList();
            mav.addObject("distlist", distlist);
            List postlist = subStantivePostDao.getSanctioningSPCOfficeWiseList(bsallot.getHidVerifyingOffCode());
            mav.addObject("postlist", postlist);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}
