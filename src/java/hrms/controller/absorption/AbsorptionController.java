/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.absorption;

import hrms.dao.absorption.AbsorptionDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.model.absorption.AbsorptionModel;
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
 * @author lenovo
 */
@Controller
@SessionAttributes({"Users", "SelectedEmpObj"})
public class AbsorptionController {

    @Autowired
    public AbsorptionDAO absorptionDAO;
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

    @RequestMapping(value = "Absorption")
    public ModelAndView AbsorptionList(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("absorptionForm") AbsorptionModel absorptionForm) {
        List absorptionlist = null;
        ModelAndView mav = null;
        try {

            absorptionlist = absorptionDAO.findAllAbsorption(u.getEmpId());
            mav = new ModelAndView("/absorption/AbsorptionList", "absorptionForm", absorptionForm);
            mav.addObject("absorptionlist", absorptionlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "AbsorptionList")
    public ModelAndView NewAbsorption(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("absorptionForm") AbsorptionModel absorptionForm) throws IOException {

        ModelAndView mav = null;

        try {

            absorptionForm.setEmpid(u.getEmpId());
            mav = new ModelAndView("/absorption/AbsorptionData", "absorptionForm", absorptionForm);
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

    @RequestMapping(value = "saveAbsorption")
    public ModelAndView saveAbsorptionData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("absorptionForm") AbsorptionModel absorptionForm) throws ParseException {

        ModelAndView mav = null;
        List absorptionlist = null;
        NotificationBean nb = new NotificationBean();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            absorptionForm.setEmpid(selectedEmpObj.getEmpId());
            nb.setNottype("ABSORPTION");
            nb.setEmpId(selectedEmpObj.getEmpId());
            nb.setDateofEntry(new Date());
            nb.setOrdno(absorptionForm.getOrdno());
            nb.setOrdDate(sdf.parse(absorptionForm.getOrdDate()));
            nb.setSancDeptCode(absorptionForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(absorptionForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(absorptionForm.getNotifyingSpc());
            nb.setNote(absorptionForm.getNote());
            if(absorptionForm.getChkNotSBPrint()!=null && absorptionForm.getChkNotSBPrint().equals("Y")){
                nb.setIfVisible("N");
            }else{
                nb.setIfVisible("Y");
            }
            
            if (absorptionForm.getHnotid() >0) {
                nb.setNotid(absorptionForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                absorptionDAO.updateAbsorptionData(absorptionForm);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                absorptionDAO.insertAbsorptionData(absorptionForm, notid);
            }
            absorptionlist = absorptionDAO.findAllAbsorption(selectedEmpObj.getEmpId());
            mav = new ModelAndView("/absorption/AbsorptionList", "absorptionForm", absorptionForm);
            mav.addObject("absorptionlist", absorptionlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "editAbsorption")
    public ModelAndView editAbsorption(HttpServletResponse response, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @RequestParam Map<String, String> requestParams) throws IOException {
        AbsorptionModel absorptionform = null;
        ModelAndView mav = null;
        try {
            String empid = selectedEmpObj.getEmpId();
            int notId = Integer.parseInt(requestParams.get("notId"));
            absorptionform = absorptionDAO.editAbsorptionData(notId, empid);
            absorptionform.setHnotid(notId);
            mav = new ModelAndView("/absorption/AbsorptionData", "absorptionForm", absorptionform);
            List deptlist = deptDAO.getDepartmentList();
            List payscalelist = payscaleDAO.getPayScaleList();
            // List allotDescList = promotionDAO.getAllotDescList("PROMOTION");
            List lvlList = cadreDAO.getCadreLevelList();
            mav.addObject("deptlist", deptlist);
            mav.addObject("payscalelist", payscalelist);
            //  mav.addObject("allotDescList", allotDescList);
            mav.addObject("lvlList", lvlList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "deleteAbsorption")
    public ModelAndView deleteAbsorption(@ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("absorptionForm") AbsorptionModel absorptionForm, @RequestParam Map<String, String> requestParams) {
        int deletestatus = 0;
        List absorptionlist = null;
        ModelAndView mav = null;
        int notId = Integer.parseInt(requestParams.get("notId"));
        int payidId = Integer.parseInt(requestParams.get("payId"));
        String cadreId = requestParams.get("cadreId");
        absorptionForm.setEmpid(selectedEmpObj.getEmpId());
        absorptionForm.setNotId(notId);
        absorptionForm.setHpayid(payidId);
        absorptionForm.sethCadId(cadreId);
        deletestatus = absorptionDAO.deleteAbsorptionData(absorptionForm);
        notificationDao.deleteNotificationData(notId, "ABSORPTION");
        
        absorptionlist = absorptionDAO.findAllAbsorption(selectedEmpObj.getEmpId());
        mav = new ModelAndView("/absorption/AbsorptionList", "absorptionForm", absorptionForm);
        mav.addObject("absorptionlist", absorptionlist);
        return mav;
    }

    @RequestMapping(value = "updateAbsorptionData")
    public void updateRecruitmentData(HttpServletResponse response, @ModelAttribute("LoginUserBean") LoginUserBean lub1, @ModelAttribute("SelectedEmpObj") Users selectedEmpObj, @ModelAttribute("absorptionForm") AbsorptionModel absorptionForm) throws ParseException {
        int recruitmentFormdata = 0;
        try {
            absorptionForm.setEmpid(selectedEmpObj.getEmpId());
            recruitmentFormdata = absorptionDAO.updateAbsorptionData(absorptionForm);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
