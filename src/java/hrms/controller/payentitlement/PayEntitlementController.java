/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hrms.controller.payentitlement;

import hrms.dao.emppayrecord.EmpPayRecordDAO;
import hrms.dao.leaveAccount.ServiceBookLanguageDAO;
import hrms.dao.master.CadreDAO;
import hrms.dao.master.DepartmentDAO;
import hrms.dao.master.DistrictDAO;
import hrms.dao.master.OfficeDAO;
import hrms.dao.master.PayScaleDAO;
import hrms.dao.master.PostDAO;
import hrms.dao.master.SubStantivePostDAO;
import hrms.dao.notification.NotificationDAO;
import hrms.dao.payentitlement.PayEntitlementDAO;
import hrms.model.cadre.Cadre;
import hrms.model.emppayrecord.EmpPayRecordForm;
import hrms.model.login.LoginUserBean;
import hrms.model.login.Users;
import hrms.model.notification.NotificationBean;
import hrms.model.payentitlement.PayEntitlementForm;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
 * @author Pintu-DELL
 */
@Controller
@SessionAttributes({"LoginUserBean", "SelectedEmpObj"})
public class PayEntitlementController {
    
    @Autowired
    public PayEntitlementDAO payEntitlementDAO;
    
    @Autowired
    public DepartmentDAO deptDAO;
    
    @Autowired
    public DistrictDAO districtDAO;
    
    @Autowired
    public OfficeDAO offDAO;
    
    @Autowired
    public PayScaleDAO payscaleDAO;
    
    @Autowired
    public CadreDAO cadreDAO;
    
    @Autowired
    public NotificationDAO notificationDao;
    
    @Autowired
    public EmpPayRecordDAO emppayrecordDAO;
    
    @Autowired
    SubStantivePostDAO subStantivePostDao;
    
    @Autowired
    public PostDAO postDAO;
    
    @Autowired
    public ServiceBookLanguageDAO sbDAO;
    
     @RequestMapping(value = "PayEntitlementList")
    public String PayEntitlementList(Model model,@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("payEntitlementForm") PayEntitlementForm payEntitlementForm) {
        
        List payEntitlementList = null;
        
        try {
            payEntitlementList = payEntitlementDAO.getPayEntitlementList(u.getEmpId());
            model.addAttribute("payEntitlementList", payEntitlementList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/payentitlement/PayEntitlementList";
    }
    
    @RequestMapping(value = "newPayEntitlement")
    public ModelAndView newPayEntitlement(@ModelAttribute("SelectedEmpObj") Users u, @ModelAttribute("payEntitlementForm") PayEntitlementForm payEntitlementForm) throws IOException {
        
        ModelAndView mav = null;
        
        try {
            payEntitlementForm.setEmpid(u.getEmpId());
            
            mav = new ModelAndView("/payentitlement/NewPayEntitlement", "payEntitlementForm", payEntitlementForm);
            
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List payscalelist = payscaleDAO.getPayScaleList();
            List lvlList = cadreDAO.getCadreLevelList();
            
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("payscalelist", payscalelist);
            mav.addObject("lvlList", lvlList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "savePayEntitlement", params = {"btnConfirmation=Save"})
    public ModelAndView savePayEntitlement(@ModelAttribute("LoginUserBean") LoginUserBean lub,@ModelAttribute("payEntitlementForm") PayEntitlementForm payEntitlementForm) throws ParseException {
        
        NotificationBean nb = new NotificationBean();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        
        ModelAndView mav = null;
        
        EmpPayRecordForm epayrecordform = new EmpPayRecordForm();
        Cadre cadreForm = new Cadre();
        
        String sblanguage = "";
        try {
            
            nb.setNottype("PAY_ENTITLEMENT");
            nb.setEmpId(payEntitlementForm.getEmpid());
            nb.setDateofEntry(new Date());
            nb.setOrdno(payEntitlementForm.getTxtNotOrdNo());
            nb.setOrdDate(sdf.parse(payEntitlementForm.getTxtNotOrdDt()));
            nb.setSancDeptCode(payEntitlementForm.getHidNotifyingDeptCode());
            nb.setSancOffCode(payEntitlementForm.getHidNotifyingOffCode());
            nb.setSancAuthCode(payEntitlementForm.getNotifyingSpc());
            nb.setNote(payEntitlementForm.getNote());
            
            cadreForm.setEmpId(payEntitlementForm.getEmpid());
            cadreForm.setNotType("PAY_ENTITLEMENT");
            cadreForm.setCadreCode(payEntitlementForm.getHcadId());
            cadreForm.setCadreDept(payEntitlementForm.getSltCadreDept());
            cadreForm.setCadreName(payEntitlementForm.getSltCadre());
            cadreForm.setGrade(payEntitlementForm.getSltGrade());
            cadreForm.setCadreLvl(payEntitlementForm.getSltCadreLevel());
            cadreForm.setDescription(payEntitlementForm.getSltDescription());
            cadreForm.setAllotmentYear(payEntitlementForm.getTxtAllotmentYear());
            cadreForm.setCadreId(payEntitlementForm.getTxtCadreId());
            cadreForm.setPostingDept(payEntitlementForm.getSltPostingDept());
            cadreForm.setPostCode(payEntitlementForm.getSltGenericPost());
            cadreForm.setPostClassification(payEntitlementForm.getRdPostClassification());
            cadreForm.setPostStatus(payEntitlementForm.getRdPostStatus());
            cadreForm.setCadreJoiningWEFDt(payEntitlementForm.getTxtCadreJoiningWEFDt());
            cadreForm.setCadreJoiningWEFTime(payEntitlementForm.getSltCadreJoiningWEFTime());
            
            epayrecordform.setEmpid(payEntitlementForm.getEmpid());
            epayrecordform.setNot_type("PAY_ENTITLEMENT");
            epayrecordform.setPayscale(payEntitlementForm.getSltPayScale());
            epayrecordform.setBasic(payEntitlementForm.getTxtBasic());
            epayrecordform.setGp(payEntitlementForm.getTxtGP());
            epayrecordform.setS_pay(payEntitlementForm.getTxtSP());
            epayrecordform.setP_pay(payEntitlementForm.getTxtPP());
            epayrecordform.setOth_pay(payEntitlementForm.getTxtOP());
            epayrecordform.setOth_desc(payEntitlementForm.getTxtDescOP());
            epayrecordform.setWefDt(payEntitlementForm.getTxtWEFDt());
            epayrecordform.setWefTime(payEntitlementForm.getSltWEFTime());
            
            if (payEntitlementForm.getHnotid() > 0) {
                nb.setNotid(payEntitlementForm.getHnotid());
                notificationDao.modifyNotificationData(nb);
                
                if (payEntitlementForm.getHcadId() != null && !payEntitlementForm.getHcadId().equals("")) {
                    cadreDAO.updateCadreData(cadreForm);
                } else {
                    cadreForm.setNotId(payEntitlementForm.getHnotid());
                    cadreDAO.saveCadreData(cadreForm);
                }
                if (payEntitlementForm.getHpayid() > 0) {
                    emppayrecordDAO.updateEmpPayRecordData(epayrecordform);
                } else {
                    epayrecordform.setNot_id(payEntitlementForm.getHnotid());
                    emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
                }
                sblanguage = sbDAO.getPayEntitlementDetails(payEntitlementForm, payEntitlementForm.getHnotid(), "PAY_ENTITLEMENT");
                payEntitlementDAO.savePayEntitlement(payEntitlementForm, payEntitlementForm.getHnotid(), sblanguage, sblanguage);
            } else {
                int notid = notificationDao.insertNotificationData(nb);
                
                cadreForm.setNotId(notid);
                cadreDAO.saveCadreData(cadreForm);
                
                epayrecordform.setNot_id(notid);
                emppayrecordDAO.saveEmpPayRecordData(epayrecordform);
                
                sblanguage = sbDAO.getPayEntitlementDetails(payEntitlementForm, notid, "PAY_ENTITLEMENT");
                payEntitlementDAO.savePayEntitlement(payEntitlementForm, notid, sblanguage, sblanguage);
            }
            
            mav = new ModelAndView("redirect:/PayEntitlementList.htm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
    @RequestMapping(value = "editPayEntitlement")
    public ModelAndView editPayEntitlement(@ModelAttribute("LoginUserBean") LoginUserBean lub, @ModelAttribute("SelectedEmpObj") Users u, @RequestParam Map<String, String> requestParams) throws IOException {
        
        PayEntitlementForm payEntitlementForm = new PayEntitlementForm();
        
        ModelAndView mav = null;
        
        try {
            int notificationId = Integer.parseInt(requestParams.get("notId"));
            payEntitlementForm.setEmpid(u.getEmpId());
            payEntitlementForm = payEntitlementDAO.getPayEntitlementData(payEntitlementForm, notificationId);
            
            payEntitlementForm.setHnotid(notificationId);
            
            mav = new ModelAndView("/payentitlement/NewPayEntitlement", "payEntitlementForm", payEntitlementForm);
            
            List deptlist = deptDAO.getDepartmentList();
            List distlist = districtDAO.getDistrictList();
            List officelist = offDAO.getTotalOfficeList(payEntitlementForm.getHidNotifyingDeptCode(), payEntitlementForm.getHidNotifyingDistCode());
            List notifyingGpclist = subStantivePostDao.getAuthorityGenericPostList(payEntitlementForm.getHidNotifyingOffCode());
            List spclist = subStantivePostDao.getAuthoritySubstantivePostList(payEntitlementForm.getHidNotifyingOffCode(),payEntitlementForm.getHidNotifyingGPC());
            
            List cadrelist = cadreDAO.getCadreList(payEntitlementForm.getSltCadreDept());
            List gradelist = cadreDAO.getGradeList(payEntitlementForm.getSltCadre());
            List lvlList = cadreDAO.getCadreLevelList();
            List desclist = cadreDAO.getDescription(payEntitlementForm.getSltCadreLevel());
            
            List postlist = postDAO.getPostList(payEntitlementForm.getSltPostingDept());
            
            List payscalelist = payscaleDAO.getPayScaleList();
            
            mav.addObject("deptlist", deptlist);
            mav.addObject("distlist", distlist);
            mav.addObject("officelist", officelist);
            mav.addObject("notifyingGpclist", notifyingGpclist);
            mav.addObject("spclist", spclist);
            
            mav.addObject("cadrelist", cadrelist);
            mav.addObject("gradelist", gradelist);
            mav.addObject("lvlList", lvlList);
            mav.addObject("desclist", desclist);
            
            mav.addObject("postlist", postlist);
            
            mav.addObject("payscalelist", payscalelist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
    
}
